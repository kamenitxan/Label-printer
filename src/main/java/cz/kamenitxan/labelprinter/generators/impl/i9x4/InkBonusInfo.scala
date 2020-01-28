package cz.kamenitxan.labelprinter.generators.impl.i9x4

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp

import cz.kamenitxan.labelprinter.Utils
import cz.kamenitxan.labelprinter.barcode.Ean13
import cz.kamenitxan.labelprinter.generators.{Generators, Ink9x4}
import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory

class InkBonusInfo extends Ink9x4 {
	override def getFolderName: String = Generators.INK_BONUS_INFO.folder

	override val singleHeight: Int = mmToPoints(21.2f).toInt
	override val singleWidth: Int = mmToPoints(38).toInt
	override val fontSize: Int = 11

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)


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

	private def drawSingle(pos: Position): Unit = {
		if (borders) debugRect(pos)

		cs.print("Výrobce/Výrobca/Producer/", pos.x + 5, pos.y + singleHeight - 5, 5)
		cs.print("Producent/Producator", pos.x + 5, pos.y + singleHeight - 10, 5)

		cs.print("AltX Distribution a.s", pos.x + 5, pos.y + singleHeight - 20)
		cs.print("Lednická 17", pos.x + 5, pos.y + singleHeight - 30)
		cs.print("198 00 Praha 9", pos.x + 5, pos.y + singleHeight - 40)

	}
}
