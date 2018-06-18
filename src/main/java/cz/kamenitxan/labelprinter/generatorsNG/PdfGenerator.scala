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
	var borders: Boolean = _
	var onlyBorders: Boolean = _
	protected var cs: PDPageContentStream = _
	protected var font: PDType0Font = _
	protected var boldFont: PDType0Font = _

	def getFolderName: String

	def generatePdf()

	protected def getPosition(line: Int, row: Int): Position

	def generate(product: Product, borders: Boolean, onlyBorders: Boolean) {
		this.product = product
		this.borders = borders
		this.onlyBorders = onlyBorders
		generatePdf()
	}

	protected def splitByWidth(text: String, allowedWidth: Int): List[String] = {
		var myLine: String = ""
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
			val size: Int = getStringWidth(myLine + word)
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

	private def getStringWidth(s: String): Int = {
		(fontSize * font.getStringWidth(s) / 1000).asInstanceOf[Int]
	}

	protected def savePdf(document: PDDocument) {
		var filename: String = null
		if (product.manufacturer != null) {
			filename = "pdf/" + getFolderName + "/" + product.manufacturer +"/" + product.invNum + ".pdf"
		} else {
			filename = "pdf/" + getFolderName + "/" + product.invNum + ".pdf"
		}
		val file = new File(filename)
		try {
			file.getParentFile.mkdirs
			document.save(file)
		} catch {
			case _: FileNotFoundException => System.out.println("Nenalezena složka pro výstup!!!")
			case _: IOException => System.out.println("IO Exception - nelze uložit.")
		}
		try
			document.close()
		catch {
			case _: IOException => System.out.println("Nelze uzavřít soubor.")
		}
	}

	/**
	  * @param mm millimeters
	  * @return size in points used by Pdfbox
	  */
	protected def mmToPoints(mm: Float): Float = {
		(1 / (10 * 2.54f) * 72) * mm
	}

	/**
	  * @param cm centimeters
	  * @return size in points used by Pdfbox
	  */
	protected def cmToPoints(cm: Float): Float = {
		mmToPoints(cm * 10)
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

		def printCentered(text: String, pos: Position, lw: Int, bold: Boolean = false): Unit = {
			val width:Int = getStringWidth(text)
			val center:Float = (lw - width) toFloat

			if (bold) {
				cs.setFont(boldFont, fontSize)
			}
			cs.beginText()
			cs.newLineAtOffset(pos.x + 60 + center/2, pos.y)
			cs.showText(text)
			cs.endText()
			cs.setFont(font, fontSize)
		}

		def printCenteredLines(lines: List[String], pos: Position, lh: Int, lineWidth: Int): Unit = {
			var i = 0
			for (line <- lines) {
				val width:Int = getStringWidth(line)
				val center:Float = (lineWidth - width) toFloat

				cs.beginText()
				cs.newLineAtOffset(pos.x + 60 + center/2, pos.y - lh * i)
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

		def drawLine(from: Position, to: Position): Unit = {
			cs.setColor(Color.BLACK)
			cs.moveTo(from.x, from.y)
			cs.lineTo(to.x, to.y)
			cs.stroke()
		}

		def drawCircle(cx: Float, cy: Float, r: Float): Unit = {
			val k = 0.552284749831f
			cs.moveTo(cx - r, cy)
			cs.curveTo(cx - r, cy + k * r, cx - k * r, cy + r, cx, cy + r)
			cs.curveTo(cx + k * r, cy + r, cx + r, cy + k * r, cx + r, cy)
			cs.curveTo(cx + r, cy - k * r, cx + k * r, cy - r, cx, cy - r)
			cs.curveTo(cx - k * r, cy - r, cx - r, cy - k * r, cx - r, cy)
			cs.fill()
		}
	}

}
