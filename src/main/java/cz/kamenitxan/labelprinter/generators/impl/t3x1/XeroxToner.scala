package cz.kamenitxan.labelprinter.generators.impl.t3x1

import java.awt.Color

import cz.kamenitxan.labelprinter.barcode.Code39
import cz.kamenitxan.labelprinter.generators.{Generators, Toner3x1}
import cz.kamenitxan.labelprinter.models.Position
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}

import scala.language.postfixOps

class XeroxToner extends  Toner3x1 {

	var eanImage: PDImageXObject = _
	var eanImage2: PDImageXObject = _
	var rohs: PDImageXObject = _

	override def getFolderName: String = Generators.TONER_XEROX.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		eanImage = createBarcodeImage(document, product.ean)
		eanImage2 = createBarcodeImage(document, product.ean2, Code39)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		rohs = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/rohs.jpg")))

		for (line <- 0 to 2; row <- 0 to 0) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)

		cs.drawImage(eanImage, pos.x + 20, pos.y + 130, eanImage.getWidth * 0.42 toFloat, eanImage.getHeight * 0.40 toFloat)
		cs.drawImage(eanImage2, pos.x + 20, pos.y + 95, eanImage.getWidth * 0.65 toFloat, eanImage.getHeight * 0.25 toFloat)

	}
}
