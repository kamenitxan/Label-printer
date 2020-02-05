package cz.kamenitxan.labelprinter.generators.impl.t6x2

import java.awt.Color

import cz.kamenitxan.labelprinter.generators.{Generators, Toner6x2}
import cz.kamenitxan.labelprinter.models.Position
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 5.3.17.
  */
class TeslaToner extends Toner6x2 {
	var document: PDDocument = _
	var ceImage: PDImageXObject = _

	override def getFolderName: String = Generators.TONER_TESLA.folder

	override def generatePdf(): Unit = {
		document = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)


		eanImage = createBarcodeImage(document, product.ean)
		ceImage = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/ce-mark.png")))

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		for (line <- 0 to 5; row <- 0 to 1) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)
		if(!onlyBorders) {
			cs.drawImage(eanImage, pos.x + 20, pos.y + 6, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)

			color(pos)
			pn(pos)
			desc(pos)
			desc(pos + (140, 0))
			manufacturer(pos)
			capacity(pos)
			capacity(pos + (140, 0))
			rohsImage(pos)
			ceImage(pos)
			divider(pos)
			madein(pos)
		}
	}

	private def desc(pos: Position): Unit = {
		val lineWidth = 100
		val fs = cs.calculateAutosizeFontSize(product.name, lineWidth)
		val lineHeight = cs.getLineHeight(fs)
		cs.printCentered(product.invNum, pos + (65, singleHeight - 5 - lineHeight), 100, bold = false,  fs)
		cs.printCenteredLinesWithFs(product.name, pos + (65, singleHeight - 5 - lineHeight * 2.8f), lineWidth, fs)

	}

	private def pn(pos: Position): Unit = {
		val leftPadding = 20
		val bottomPadding = 35

		cs.setColor(Color.WHITE)
		cs.addRect(pos.x + leftPadding, pos.y + bottomPadding - 2, 100, 12)
		cs.fillAndStroke()
		cs.setColor(Color.GRAY)
		cs.setFont(font, fontSize + 5)
		cs.print(product.invNum, pos.x + leftPadding, pos.y + bottomPadding)
		cs.setFont(font, fontSize)
		cs.setColor(Color.BLACK)
	}

	def color(pos: Position): Unit = {
		product.color match {
			case Color.WHITE =>
				val third = singleHeight / 3
				cs.setColor(Color.YELLOW)
				cs.addRect(pos.x, pos.y, 15, third)
				cs.fillAndStroke()
				cs.setColor(Color.MAGENTA)
				cs.addRect(pos.x, pos.y + third, 15, third)
				cs.fillAndStroke()
				cs.setColor(Color.CYAN)
				cs.addRect(pos.x, pos.y + 2 * third, 15, third)
				cs.fillAndStroke()
				cs.setColor(Color.BLACK)
			case _ =>
				cs.setColor(product.color)
				cs.addRect(pos.x, pos.y, 15, singleHeight)
				cs.fillAndStroke()
		}

		cs.setColor(Color.BLACK)

		product.color match {
			case Color.WHITE | Color.CYAN | Color.YELLOW =>
				colorText(pos)
			case _ =>
				cs.setColor(Color.WHITE)
				colorText(pos)
				cs.setColor(Color.BLACK)
		}

	}

	private def colorText(pos: Position): Unit = {
		cs.beginText()
		cs.setTextRotation(Math.toRadians(270), pos.x + 5, pos.y + 50)
		cs.showText(product.colorName)
		cs.endText()
	}

	private def madein(pos: Position): Unit = {
		val leftPadding = 20
		val top = pos.y + singleHeight - 6
		val lh = 6
		val fs = 5
		cs.print(manufacturer1, pos.x + leftPadding, top, fs)
		cs.print(manufacturer2, pos.x + leftPadding, top - lh * 1, fs)
		cs.print(company, pos.x + leftPadding, top - lh * 2, fs)
		cs.print(street, pos.x + leftPadding, top - lh * 3, fs)
		cs.print(city, pos.x + leftPadding, top - lh * 4, fs)
		cs.print(companyId, pos.x + leftPadding, top - lh * 5, fs)
	}

	def divider(pos: Position): Unit = {
		val left = 244
		cs.drawLine(pos + (left, 0), pos + (left, 10))
		cs.drawLine(pos + (left, singleHeight), pos + (left, singleHeight - 10))
	}

	def manufacturer(pos: Position): Unit = {
		cs.print(product.manufacturer, pos.x + 105, pos.y + 10)
	}

	private def capacity(pos: Position): Unit = {
		cs.print(product.capacity, pos.x + 150, pos.y + 7)
	}

	def rohsImage(pos: Position): Unit = {}

	def ceImage(pos: Position): Unit = {
		val scale = 0.1
		cs.drawImage(ceImage, pos.x + singleWidth - 26, pos.y + 6,  ceImage.getWidth * scale toFloat, ceImage.getHeight * scale toFloat)
	}
}
