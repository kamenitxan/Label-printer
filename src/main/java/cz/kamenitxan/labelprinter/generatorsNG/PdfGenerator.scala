package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color
import java.io.{File, FileNotFoundException, IOException}

import cz.kamenitxan.labelprinter.models.{Position, Product}
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.{PDDocument, PDPageContentStream}

import scala.collection.mutable

/**
  * Created by tomaspavel on 1.3.17.
  */
abstract class PdfGenerator {
	val wholePageWidth = 843
	val wholePageHeight = 596
	val fontSize: Int
	val PAGE_SIZE_A4: PDRectangle

	var product: Product = _
	protected var cs: PDPageContentStream = _
	protected var font: PDType0Font = _
	protected var boldFont: PDType0Font = _

	def getFolderName: String

	def generatePdf()

	protected def getPosition(line: Int, row: Int): Position

	def generate(product: Product) {
		this.product = product
		generatePdf()
	}

	protected def splitByWidth(text: String, width: Int): List[String] = {
		var myLine: String = ""
		val allowedWidth = 60
		val lineHeight = 10
		var lines: mutable.MutableList[String] = mutable.MutableList()

		// get all words from the text
		// keep in mind that words are separated by spaces -> "Lorem ipsum!!!!:)" -> words are "Lorem" and "ipsum!!!!:)"
		val words: Array[String] = text.split(" ")
		for (w <- words) {
			val word = w + " "
			if (!myLine.isEmpty) {
				myLine += ""
			}
			// test the width of the current line + the current word
			val size: Int = (fontSize * font.getStringWidth(myLine + word) / 1000).asInstanceOf[Int]
			if (size > allowedWidth) {
				// if the line would be too long with the current word, add the line without the current word
				lines += myLine
				// and start a new line with the current word
				myLine = word
			} else {
				// if the current line + the current word would fit, add the current word to the line
				myLine += word
			}
		}
		// add the rest to lines
		lines += myLine
		lines.toList
	}

	protected def savePdf(document: PDDocument) {
		val file = new File("pdf/" + getFolderName + "/" + product.invNum + ".pdf")
		try {
			file.getParentFile.mkdirs
			document.save(file)
		} catch {
			case ex: FileNotFoundException => System.out.println("Nenalezena složka pro výstup!!!")
			case ex: IOException => System.out.println("IO Exception - nelze uložit.")
		}
		try
			document.close()
		catch {
			case ex: IOException => System.out.println("Nelze uzavřít soubor.")
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

		def printBold(text: String, x: Float, y: Float): Unit = {
			cs.setFont(boldFont, fontSize)
			print(text: String, x: Float, y: Float)
			cs.setFont(font, fontSize)
		}

		def printLines(lines: List[String], pos: Position, lh: Int): Unit = {
			var i = 0
			for (line <- lines) {
				cs.beginText()
				cs.newLineAtOffset(pos.x + 60, pos.y - lh * i)
				cs.showText(line + "")
				//cs.moveTextPositionByAmount(60, y)
				//cs.drawString(line)
				cs.endText()
				i += 1
			}
		}

		def printLines(text: String, pos: Position, lh: Int, width: Int): Unit = {
			printLines(splitByWidth(text, width), pos, lh)
		}
	}

}
