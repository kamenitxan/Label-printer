package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.io.File

import cz.kamenitxan.labelprinter.Utils
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.{Ean13, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.io.Source

/**
  * Created by tomaspavel on 5.3.17.
  */
class TeslaInk extends AltxInk {
	override def getFolderName: String = Generators.INK_TESLA.folder

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
			drawTeslaSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawTeslaSingle(pos: Position) = {
		drawSingle(pos)

		//pn2
		val size: Int = (fontSize * font.getStringWidth(product.productCode) / 1000).asInstanceOf[Int]
		val center = 45 - size toFloat

		cs.beginText()
		cs.newLineAtOffset(pos.x + 60, pos.y + 10)
		cs.setFont(font, fontSize + 6)
		cs.setTextRotation(Math.toRadians(90), pos.x + 128, pos.y + 2 + center)
		cs.showText(product.productCode)
		cs.setFont(font, fontSize)
		cs.endText()


		cs.drawLine(pos + (115, 0), pos + (115, 5))
		cs.drawLine(pos + (115, singleHeight), pos + (115, singleHeight - 5))
	}
}
