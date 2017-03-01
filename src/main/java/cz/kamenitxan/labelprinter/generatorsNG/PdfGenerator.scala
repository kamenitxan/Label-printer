package cz.kamenitxan.labelprinter.generatorsNG

import java.io.{File, FileNotFoundException, IOException}

import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param
import cz.kamenitxan.labelprinter.Main
import cz.kamenitxan.labelprinter.models.Product

import scala.collection.JavaConverters._
import java.util

import org.apache.pdfbox.pdmodel.{PDDocument, PDPageContentStream}

/**
  * Created by tomaspavel on 1.3.17.
  */
abstract class PdfGenerator {
	val wholePageWidth = 843
	val wholePageHeight = 596

	var product: Product = _
	var cs: PDPageContentStream = _

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

}
