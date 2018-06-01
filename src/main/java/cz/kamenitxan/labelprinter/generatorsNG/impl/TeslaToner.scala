package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Toner6x2
import cz.kamenitxan.labelprinter.models.{Ean13, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 5.3.17.
  */
class TeslaToner extends Toner6x2 {
	override def getFolderName: String = Generators.TONER_TESLA.folder

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

		for (line <- 0 to 5; row <- 0 to 1) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)
		if(!onlyBorders) {
			cs.drawImage(eanImage, pos.x + 20, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)
			madein(pos)
			color(pos)
			pn(pos)
			desc(pos)
			desc(pos + (100, 0))
			manufacturer(pos)
			capacity(pos)
			capacity(pos + (100, 0))

			divider(pos)
		}
	}

	private def desc(pos: Position): Unit = {
		val top = pos.y + singleHeight - 15
		val paddingLeft = pos.x + 90
		val lineWidth = 80
		val lineHeight = 8
		cs.printCenteredLines(product.invNum :: Nil, new Position(paddingLeft, top), lineHeight,lineWidth)
		val lines = splitByWidth(product.name, lineWidth)
		cs.printCenteredLines(lines, pos + (90, 55), lineHeight, lineWidth)

	}

	private def pn(pos: Position): Unit = {
		val leftPadding = 20
		val bottomPadding = 32

		cs.setColor(Color.WHITE)
		cs.addRect(pos.x + leftPadding, pos.y + bottomPadding - 2, 100, 12)
		cs.fillAndStroke()
		cs.setColor(Color.GRAY)
		cs.setFont(font, fontSize + 7)
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
		val top = pos.y + singleHeight - 10
		val lh = 4.5

		cs.setFont(font, fontSize - 3)
		cs.print("Contents: 1 Cartrige. Made in Czech Republic.", pos.x + leftPadding, top)
		cs.print("Složení: 1 kazeta. Vyrobeno v České republice.", pos.x + leftPadding, top - lh * 1 toFloat)
		cs.print("Zloženie: 1 kazeta. Vyrobené v Českej republike.", pos.x + leftPadding, top - lh * 2 toFloat)
		cs.print("Tartalom: 1 kazetta. Made in Czech Republic.", pos.x + leftPadding, top - lh * 3 toFloat)
		cs.print("Zawiera: 1 kasete. Wyprodukowane w Czechach.", pos.x + leftPadding, top - lh * 4 toFloat)
		cs.print("Continut: 1 cartus. Produs in Republica Ceha.", pos.x + leftPadding, top - lh * 5 toFloat)
		cs.setFont(font, fontSize)
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
		cs.print(product.capacity, pos.x + 150, pos.y + 5)
	}

}
