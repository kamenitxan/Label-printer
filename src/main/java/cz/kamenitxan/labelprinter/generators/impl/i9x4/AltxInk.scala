package cz.kamenitxan.labelprinter.generators.impl.i9x4

import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp

import cz.kamenitxan.labelprinter.barcode.Ean13
import cz.kamenitxan.labelprinter.generators.{Generators, Ink9x4}
import cz.kamenitxan.labelprinter.models.{Position, ProductColor, White}
import cz.kamenitxan.labelprinter.utils.{AltXAddress, Utils}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps
import scala.language.implicitConversions



/**
  * Created by tomaspavel on 1.3.17.
  */
class AltxInk extends Ink9x4 with AltXAddress {
	var eanImage: PDImageXObject = _

	override def getFolderName: String = Generators.INK_ALLPRINT.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		var eanRaw = Ean13.createEan(product.ean)
		val transform = new AffineTransform()
		transform.rotate(Math.PI / 2, eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
		transform.translate(eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
		val op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
		eanRaw = op.filter(eanRaw, null)
		eanRaw = Utils.trimImage(eanRaw)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)

		for (line <- 0 to 8; row <- 0 to 3) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)
		if(!onlyBorders) {
			cs.drawImage(eanImage, pos.x + 2, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)

			//desc
			desc(pos)
			color(pos)

			cs.beginText()
			//color
			cs.newLineAtOffset(pos.x + 60, pos.y + 20)
			cs.showText(product.colorName)
			cs.endText()
			// capacity
			cs.beginText()
			cs.newLineAtOffset(pos.x + 60, pos.y + 10)
			cs.showText(product.capacity)
			cs.endText()
			//pn
			cs.beginText()
			cs.newLineAtOffset(pos.x + 60, pos.y + 10)
			cs.setFont(font, fontSize + 6)
			cs.setTextRotation(Math.toRadians(90), pos.x + 51, pos.y + 2)
			cs.showText(product.invNum)
			cs.setFont(font, fontSize)
			cs.endText()

			cs.print(product.manufacturer, pos.x + 60, pos.y + 3)
		}
	}

	private def color(posB: Position): Unit = {
		val pos = new Position(posB.x + 56, posB.y + 13)

		product.color match {
			case White =>
				cs.setNonStrokingColor(Color.CYAN)
				cs.drawCircle(pos.x, pos.y, 3)

				cs.setNonStrokingColor(Color.MAGENTA)
				cs.drawCircle(pos.x, pos.y + 10, 3)

				cs.setNonStrokingColor(Color.YELLOW)
				cs.drawCircle(pos.x, pos.y + 20, 3)

			case _ =>
				cs.setNonStrokingColor(product.color)
				cs.drawCircle(pos.x, pos.y + 10, 3)
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
	}

	private def desc(pos: Position): Unit = {
		val allowedWidth = 50
		cs.printCenteredAutosizedLines(product.name ,pos + (-10, 70), allowedWidth)
	}


}
