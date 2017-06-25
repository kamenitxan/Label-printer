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

		divider(pos)

		cs.drawImage(logo, pos.x + 20, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(logo, pos.x + 480, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(icons, pos.x + 375, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.drawImage(icons, pos.x + 695, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
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

		colorRect(pos + (400, 140))
		colorRect(pos + (720, 140))

	}

	private def colorRect(pos: Position) = {
		val height = 25
		val width = 60
		product.color = Color.WHITE
		product.color match {
			case Color.WHITE =>
				cs.setStrokingColor(Color.CYAN)
				cs.setNonStrokingColor(Color.CYAN)
				cs.addRect(pos.x, pos.y, width/3, height)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.MAGENTA)
				cs.setNonStrokingColor(Color.MAGENTA)
				cs.addRect(pos.x + width/3, pos.y, width/3, height)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.YELLOW)
				cs.setNonStrokingColor(Color.YELLOW)
				cs.addRect(pos.x + 2*(width/3), pos.y, width/3, height)
				cs.fillAndStroke()

			case _ =>
				cs.setStrokingColor(product.color)
				cs.setNonStrokingColor(product.color)
				cs.addRect(pos.x, pos.y, width, height)
				cs.fillAndStroke()
				cs.print(product.colorName, pos.x, pos.y)
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
		// TODO: centrovat text
		product.color match {
			case Color.BLACK =>
				cs.setColor(Color.WHITE)
				cs.print(product.colorName, pos.x+5, pos.y+7)
			case _ =>
				cs.setColor(Color.BLACK)
				cs.print(product.colorName, pos.x+5, pos.y+7)
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
	}

	private def divider(pos: Position) = {
		cs.setColor(Color.BLACK)
		cs.moveTo(pos.x + 470, pos.y)
		cs.lineTo(pos.x + 470, pos.y + 10)
	  	cs.stroke()
		cs.moveTo(pos.x + 470, pos.y + singleHeight)
		cs.lineTo(pos.x + 470, pos.y + singleHeight - 10)
	  	cs.stroke()
	}
}