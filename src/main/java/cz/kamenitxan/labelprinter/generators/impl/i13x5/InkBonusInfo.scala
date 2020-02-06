package cz.kamenitxan.labelprinter.generators.impl.i13x5

import cz.kamenitxan.labelprinter.generators.{Generators, Ink13x5}
import cz.kamenitxan.labelprinter.models.Position
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

abstract class InkBonusInfo extends Ink13x5 {

	val firm: String
	val street: String = "Lednická 17"
	val town: String = "198 00 Praha 9"
	var ceImage: PDImageXObject = _

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		ceImage = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/ce-mark.png")))

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)

		for (line <- 0 to 12; row <- 0 to 4) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if (borders) debugRect(pos)
		if(!onlyBorders) {
			cs.print("Výrobce/Výrobca/Producer/", pos.x + 5, pos.y + singleHeight - 7, 5)
			cs.print("Producent/Producator", pos.x + 5, pos.y + singleHeight - 12, 5)

			cs.print(firm, pos.x + 5, pos.y + singleHeight - 22)
			cs.print(street, pos.x + 5, pos.y + singleHeight - 32)
			cs.print(town, pos.x + 5, pos.y + singleHeight - 42)

			val scale = 0.1
			cs.drawImage(ceImage, pos.x + singleWidth - 23, pos.y + 10,  ceImage.getWidth * scale toFloat, ceImage.getHeight * scale toFloat)
		}
	}

}
