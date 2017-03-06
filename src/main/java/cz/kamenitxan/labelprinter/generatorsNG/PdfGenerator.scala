package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color
import java.io.{File, FileNotFoundException, IOException}

import cz.kamenitxan.labelprinter.models.Product
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPageContentStream}

/**
  * Created by tomaspavel on 1.3.17.
  */
abstract class PdfGenerator {
	val wholePageWidth = 843
	val wholePageHeight = 596

	var product: Product = _
	var cs: PDPageContentStream = _
	var font: PDType0Font = _
	var boldFont: PDType0Font = _

	def getFolderName: String

	def generatePdf()

	def generate(product: Product) {
		this.product = product
		generatePdf()
	}

	def savePdf(document: PDDocument) {
		val file = new File("pdf/" + getFolderName + "/" + product.invNum + ".pdf")
		try {
			file.getParentFile.mkdirs
			document.save(file)
		} catch {
			case ex: FileNotFoundException =>System.out.println("Nenalezena složka pro výstup!!!")
			case ex: IOException => System.out.println("IO Exception - nelze uložit.")
		}
		try
			document.close()
		catch {
			case ex: IOException =>System.out.println("Nelze uzavřít soubor.")
		}
	}

	implicit class PDPageContentStreamExtensions(val cs: PDPageContentStream) {
		def setColor(color: Color): Unit = {
			cs.setStrokingColor(color)
			cs.setNonStrokingColor(color)
		}

		def print(text: String, x: Float, y: Float): Unit = {
			cs.beginText()
			cs.newLineAtOffset(x, y)
			cs.showText(text)
			cs.endText()
		}
	}

}
