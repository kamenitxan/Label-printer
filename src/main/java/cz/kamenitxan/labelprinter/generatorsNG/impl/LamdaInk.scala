package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.io.File

import cz.kamenitxan.labelprinter.Utils
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Ink9x4
import cz.kamenitxan.labelprinter.models.{Ean13Test, Position}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

/**
  * Created by tomaspavel on 8.4.17.
  */
class LamdaInk extends Ink9x4 {
	override def getFolderName: String = Generators.INK_LAMDA.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		var eanRaw = Ean13Test.createEan(product.ean)
		val transform = new AffineTransform()
		transform.rotate(Math.PI / 2, eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
		transform.translate(eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
		val op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
		eanRaw = op.filter(eanRaw, null)
		eanRaw = Utils.trimImage(eanRaw)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)

		for (line <- 0 to 8; row <- 0 to 3) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position) = {

	}
}
