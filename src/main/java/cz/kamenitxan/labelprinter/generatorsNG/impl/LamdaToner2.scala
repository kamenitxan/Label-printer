package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color

import javax.imageio.ImageIO
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.{Ean13, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 23.3.17.
  */
class LamdaToner2 extends TeslaToner {
	var logo: PDImageXObject = _
	var icons: PDImageXObject = _

	override def getFolderName: String = Generators.TONER_LAMDA.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		val eanRaw = Ean13.createEan(product.ean)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		logo = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/lamda2.jpg")))
		icons = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/label2.jpg")))

		for (line <- 0 to 5; row <- 0 to 1) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)

		cs.drawImage(eanImage, pos.x + 20, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)
		cs.drawImage(logo, pos.x + 19, pos.y + 30, logo.getWidth * 0.30 toFloat, logo.getHeight * 0.30 toFloat)
		cs.drawImage(icons, pos.x + 177, pos.y + 2, logo.getWidth * 0.20 toFloat, logo.getHeight * 0.07 toFloat)
		cs.drawImage(icons, pos.x + 325, pos.y + 2, logo.getWidth * 0.20 toFloat, logo.getHeight * 0.07 toFloat)

		color(pos)
		desc(pos - (10, 0))
		desc(pos + (120, 0))
		manufacturer(pos)
		divider(pos)
	}

	private def desc(pos: Position): Unit = {
		val lineWidth = 80
		val lineHeight = 8
		val lines = splitByWidth(product.name, lineWidth)
		cs.printCenteredLines(lines, pos + (90, 65), lineHeight, lineWidth)

		cs.printCentered(product.invNum, pos + (80, 21), 100, bold = true)
		//cs.print("Productcode:" + product.productCode, pos.x + 130, pos.y + 29)
		cs.print(product.capacity, pos.x + 130, pos.y + 13)
		cs.print("Lamdaprint cz", pos.x + 130, pos.y + 5)

	}

}