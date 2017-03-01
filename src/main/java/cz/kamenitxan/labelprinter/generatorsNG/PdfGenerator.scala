package cz.kamenitxan.labelprinter.generatorsNG

import java.io.{File, FileNotFoundException, IOException}

import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param
import cz.kamenitxan.labelprinter.Main
import cz.kamenitxan.labelprinter.models.Product

import scala.collection.JavaConverters._
import java.util

import org.apache.pdfbox.pdmodel.PDDocument

/**
  * Created by tomaspavel on 1.3.17.
  */
abstract class PdfGenerator {

	val wholePageWidth = 843
	val wholePageHeight = 596

	def getFolderName: String

	def generatePdf(product: Product)

	def generate(products: util.List[Product]) {
		products.asScala.filter(p => p.isValid).par.foreach(p => generatePdf(p))
	}

	def savePdf(name: String, document: PDDocument) {
		try
			val file = new File("pdf/" + getFolderName + "/" + name + ".pdf")
			file.getParentFile.mkdirs
			document.save(file)
		catch {
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
