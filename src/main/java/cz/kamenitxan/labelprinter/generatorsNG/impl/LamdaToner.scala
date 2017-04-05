package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Toner3x1
import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

/**
  * Created by tomaspavel on 23.3.17.
  */
class LamdaToner extends Toner3x1 {
	var logo: PDImageXObject = _
	var icons: PDImageXObject = _

	def getFolderName: String = Generators.TONER_LAMDA.folder

	def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)


		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		val img = ImageIO.read(new File("img/lamda2.jpg"))
		logo = LosslessFactory.createFromImage(document, img)
		icons = LosslessFactory.createFromImage(document, ImageIO.read(new File("img/label2.jpg")))

		for (line <- 0 to 2; row <- 0 to 0) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position) = {
		debugRect(pos)

		cs.drawImage(logo, pos.x + 2, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(logo, pos.x + 480, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(icons, pos.x + 200, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.drawImage(icons, pos.x + 600, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 20, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 20, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 115, pos.y + 55)
		cs.printBold(product.name, pos.x + 20, pos.y + 80)
		cs.print(product.capacity, pos.x + 400, pos.y + 55)
		cs.print(product.productCode, pos.x+400, pos.y + 40)

		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 480, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 480, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 575, pos.y + 55)
		cs.printBold(product.name, pos.x + 480, pos.y + 80)
		cs.print(product.capacity, pos.x + 720, pos.y + 55)
		cs.print(product.productCode, pos.x+720, pos.y + 40)


	}
}