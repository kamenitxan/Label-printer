package cz.kamenitxan.labelprinter.generatorsNG
import java.io.File

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.Product
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

/**
  * Created by tomaspavel on 1.3.17.
  */
class AltxInk extends PdfGenerator {

	private val PAGE_SIZE_A4 = new PDRectangle(wholePageHeight, wholePageWidth)

	override def getFolderName: String = Generators.INK_ALTX.folder

	override def generatePdf(product: Product): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)
		page.setRotation(90)

		val contentStream = new PDPageContentStream(document, page)
		val font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"))
		val boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"))

		for (line <- 1 to 9; row <- 1 to 4) {
			drawSingle(product, contentStream, getPosition(line, row))
		}
		savePdf(product.name, document)
	}

	private def drawSingle(product: Product, cs: PDPageContentStream, pos: (Int, Int)) = {
		cs.beginText()
		cs.newLineAtOffset(pos._1, pos._2)
		cs.showText("x")
		cs.endText()
	}

	private def getPosition(line: Int, row: Int) = {
		var x = 10 + 150*line + 5*line
		var y = 10 + 50*line + 5*line
		(x, y)
	}
}
