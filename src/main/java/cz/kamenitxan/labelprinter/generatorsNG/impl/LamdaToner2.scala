package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color
import javax.imageio.ImageIO

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.{Toner3x1, Toner6x2}
import cz.kamenitxan.labelprinter.models.{Ean13, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

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

	private def drawSingle(pos: Position) = {
		//debugRect(pos)

		cs.drawImage(eanImage, pos.x + 20, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)
		cs.drawImage(logo, pos.x + 20, pos.y + 30, logo.getWidth * 0.30 toFloat, logo.getHeight * 0.30 toFloat)
		cs.drawImage(icons, pos.x + 695, pos.y, logo.getWidth * 0.20 toFloat, logo.getHeight * 0.07 toFloat)

		color(pos)
		desc(pos)
		desc(pos+ (120, 0))
		capacity(pos)
		capacity(pos + (120, 0))
		manufacturer(pos)
		divider(pos)

		/*cs.drawImage(logo, pos.x + 20, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(logo, pos.x + 480, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(icons, pos.x + 375, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.drawImage(icons, pos.x + 695, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 20, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 20, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 115, pos.y + 55)
		cs.printBold(product.name, pos.x + 20, pos.y + 80)
		cs.print(product.capacity, pos.x + 400, pos.y + 55)
		val size: Int = (fontSize * font.getStringWidth(product.productCode) / 1000).asInstanceOf[Int]
		cs.beginText()
		cs.newLineAtOffset(pos.x+460 - size, pos.y + 40)
		cs.showText(product.productCode)
		cs.endText()

		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 480, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 480, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 575, pos.y + 55)
		cs.printBold(product.name, pos.x + 480, pos.y + 80)
		cs.print(product.capacity, pos.x + 720, pos.y + 55)

		val size2: Int = (fontSize * font.getStringWidth(product.productCode) / 1000).asInstanceOf[Int]
		cs.beginText()
		cs.newLineAtOffset(pos.x+780 - size2, pos.y + 40)
		cs.showText(product.productCode)
		cs.endText()

		colorRect(pos + (400, 140))
		colorRect(pos + (720, 140))

		cs.print(product.manufacturer, pos.x + 420, pos.y + 120)
		cs.print(product.manufacturer, pos.x + 740, pos.y + 120)*/
	}

	private def desc(pos: Position) = {
		val lineWidth = 80
		val lineHeight = 8
		val lines = splitByWidth(product.name, lineWidth)
		cs.printCenteredLines(lines, pos + (90, 65), lineHeight, lineWidth)

		cs.print("Productcode:" + product.productCode, pos.x + 130, pos.y + 29)
		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 130, pos.y + 21)
		cs.print("Katalogové číslo: " + product.invNum, pos.x + 130, pos.y + 13)
	}

	private def capacity(pos: Position): Unit = {
		cs.print("Kapacita:"  +product.capacity, pos.x + 130, pos.y + 5)
	}

}