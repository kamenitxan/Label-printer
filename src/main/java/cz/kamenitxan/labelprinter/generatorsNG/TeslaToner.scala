package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color
import java.io.File

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.{Ean13Test, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

/**
  * Created by tomaspavel on 5.3.17.
  */
class TeslaToner extends Toner6x2 {
	override def getFolderName: String = Generators.TONER_TESLA.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)


		val eanRaw = Ean13Test.createEan(product.ean)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		for (line <- 0 to 5; row <- 0 to 1) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position) = {
		debugRect(pos)

		madein(pos)
		color(pos)
		cs.drawImage(eanImage, pos.x + 20, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)
	}

	private def color(pos: Position) = {
		product.color match {
			case Color.WHITE =>
			//TODO
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

	private def colorText(pos: Position) = {
		cs.beginText()
		cs.setTextRotation(Math.toRadians(270), pos.x+5, pos.y+50)
		cs.showText(product.colorName)
		cs.endText()
	}

	private def madein(pos: Position): Unit = {
		val leftPadding = 20
		val top = pos.y + singleHeight - 10
		val lh = 5

		cs.setFont(font, fontSize - 3)
		cs.print("Contents: 1 Cartrige. Made in Czech Republic.", pos.x + leftPadding, top)
		cs.print("Složení: 1 kazeta. Vyrobeno v České republice.", pos.x + leftPadding, top - lh * 1)
		cs.print("Zloženie: 1 kazeta. Vyrobené v Českej republike.", pos.x + leftPadding, top - lh * 2)
		cs.print("Tartalom: 1 kazetta. Made in Czech Republic.", pos.x + leftPadding, top - lh * 3)
		cs.print("Zawiera: 1 kasete. Wyprodukowane w Czechach.", pos.x + leftPadding, top - lh * 4)
		cs.print("Continut: 1 cartus. Prudus in Republica Ceha.", pos.x + leftPadding, top - lh * 5)
		cs.setFont(font, fontSize)
	}

}
