package cz.kamenitxan.labelprinter.generators

import java.awt.Color
import java.io.{File, FileNotFoundException, IOException}
import java.util.Calendar

import cz.kamenitxan.labelprinter.barcode.{BarcodeGenerator, Ean13}
import cz.kamenitxan.labelprinter.models.{Position, Product}
import cz.kamenitxan.labelprinter.utils.AltXAddress
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPageContentStream}

import scala.collection.mutable
import scala.language.postfixOps

/**
  * Created by tomaspavel on 1.3.17.
  */
abstract class PdfGenerator {
	val wholePageWidth: Float = 843
	val wholePageHeight: Float = 596
	val fontSize: Int
	val PAGE_SIZE_A4: PDRectangle

	var product: Product = _
	var borders: Boolean = _
	var onlyBorders: Boolean = _
	protected var cs: PDPageContentStream = _
	protected var font: PDType0Font = _
	protected var boldFont: PDType0Font = _

	def getFolderName: String

	def generatePdf(): Unit

	protected def getPosition(line: Int, row: Int): Position

	def generate(product: Product, borders: Boolean, onlyBorders: Boolean): Unit = {
		this.product = product
		this.borders = borders
		this.onlyBorders = onlyBorders
		generatePdf()
	}

	protected def splitByWidth(text: String, allowedWidth: Int, fs: Int = fontSize): List[String] = {
		var myLine: String = ""
		var lines: mutable.Buffer[String] = mutable.Buffer()

		// get all words from the text
		// keep in mind that words are separated by spaces -> "Lorem ipsum!!!!:)" -> words are "Lorem" and "ipsum!!!!:)"
		val words: Array[String] = text.split(" ")
		for (w <- words) {
			val word = w + " "
			if (!myLine.isEmpty) {
				myLine += ""
			}
			// test the width of the current line + the current word
			val size: Int = getStringWidth(myLine + word, fs)
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

	private def getStringWidth(s: String, fs: Int = fontSize): Int = {
		(fs * font.getStringWidth(s) / 1000).asInstanceOf[Int]
	}

	protected def savePdf(document: PDDocument): Unit = {
		val di = document.getDocumentInformation
		di.setCreationDate(Calendar.getInstance())
		di.setModificationDate(Calendar.getInstance())
		di.setCreator("Labelprinter")
		di.setTitle(product.invNum)

		var filename: String = null
		if (product.manufacturer != null) {
			filename = "pdf/" + getFolderName + "/" + product.manufacturer + "/" + product.invNum + ".pdf"
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

	protected def createBarcodeImage(document: PDDocument, ean: String, generator: BarcodeGenerator = Ean13, doQuietZone: Boolean = false): PDImageXObject = {
		if (ean == null || ean.isEmpty) {
			return null
		}
		val eanRaw = generator.createEan(ean, doQuietZone)
		LosslessFactory.createFromImage(document, eanRaw)
	}

	implicit class PDPageContentStreamExtensions(val cs: PDPageContentStream) {
		def setColor(color: Color): Unit = {
			cs.setStrokingColor(color)
			cs.setNonStrokingColor(color)
		}

		def print(text: String, x: Float, y: Float, fs: Int = fontSize): Unit = {
			cs.setFont(font, fs)
			cs.beginText()
			cs.newLineAtOffset(x, y)
			cs.showText(text)
			cs.endText()
			cs.setFont(font, fontSize)
		}

		def printBold(text: String, x: Float, y: Float): Unit = {
			cs.setFont(boldFont, fontSize)
			print(text: String, x: Float, y: Float)
			cs.setFont(font, fontSize)
		}

		/**
		  * @param lines radky k vypsani
		  * @param pos   pozice
		  * @param lh    vyska radku
		  * @param fs    velikost pisma
		  */
		def printLines(lines: Seq[String], pos: Position, lh: Int, fs: Float = fontSize): Unit = {
			var i = 0
			cs.setFont(font, fs)
			for (line <- lines) {
				cs.beginText()
				cs.newLineAtOffset(pos.x + 60, pos.y - lh * i)
				cs.showText(line + "")
				//cs.moveTextPositionByAmount(60, y)
				//cs.drawString(line)
				cs.endText()
				i += 1
			}
			cs.setFont(font, fontSize)
		}

		def printLines(text: String, pos: Position, lh: Int, width: Int): Unit = {
			printLines(splitByWidth(text, width), pos, lh)
		}

		def printLines(text: String, pos: Position, lh: Int, width: Int, fs: Float): Unit = {
			printLines(splitByWidth(text, width), pos, lh, fs)
		}

		def printCentered(text: String, pos: Position, lw: Int, bold: Boolean = false): Unit = {
			printCentered(text, pos, lw, bold, fontSize)
		}

		def printCentered(text: String, pos: Position, lw: Int, bold: Boolean, fs: Int): Unit = {
			val width: Int = getStringWidth(text)
			val center: Float = (lw - width) toFloat

			if (bold) {
				cs.setFont(boldFont, fs)
			} else {
				cs.setFont(font, fs)
			}
			cs.beginText()
			cs.newLineAtOffset(pos.x + 60 + center / 2, pos.y)
			cs.showText(text)
			cs.endText()
			cs.setFont(font, fontSize)
		}

		def printCenteredLines(lines: List[String], pos: Position, lh: Float, lineWidth: Int): Unit = {
			var i = 0
			for (line <- lines) {
				val width: Int = getStringWidth(line)
				val center: Float = (lineWidth - width) toFloat

				cs.beginText()
				cs.newLineAtOffset(pos.x + 60 + center / 2, pos.y - lh * i)
				cs.showText(line + "")
				//cs.moveTextPositionByAmount(60, y)
				//cs.drawString(line)
				cs.endText()
				i += 1
			}
		}

		def printCenteredLinesWithFs(text: String, pos: Position, lineWidth: Int, fs: Int): Unit = {
			val lines: List[String] = splitByWidth(text, lineWidth, fs)
			cs.setFont(font, fs)
			val lineHeight = font.getFontDescriptor.getCapHeight / 1000 * fs
			printCenteredLines(lines, pos, lineHeight + lineHeight / 2, lineWidth)
			cs.setFont(font, fontSize)
		}

		def calculateAutosizeFontSize(text: String, lineWidth: Int, fs: Int = fontSize, maxLines: Int = 3): Int = {
			val lines: List[String] = splitByWidth(text, lineWidth, fs)
			if (lines.size >= maxLines) {
				fs
			} else {
				calculateAutosizeFontSize(text, lineWidth, fs + 1, lines.size + 1)
			}
		}

		/** Pokud bude mít popis 3 a více řádku, nechat velikost písma původní, pokud bude mít popis do dvou řádku písmo zvětšit do maximální velikosti. */
		def printCenteredAutosizedLines(text: String, pos: Position, lineWidth: Int, fs: Int = fontSize): Unit = {
			val lines: List[String] = splitByWidth(text, lineWidth, fs)
			if (lines.size < 3) {
				printCenteredAutosizedLinesInt(text, pos, lineWidth, fs, lines.size + 1)
			} else {
				printCenteredAutosizedLinesInt(text, pos, lineWidth, fs, lines.size)
			}
		}

		private def printCenteredAutosizedLinesInt(text: String, pos: Position, lineWidth: Int, fs: Int = fontSize, minLines: Int): Unit = {
			val lines: List[String] = splitByWidth(text, lineWidth, fs)
			if (lines.size >= minLines) {
				cs.setFont(font, fs)
				val lineHeight = font.getFontDescriptor.getCapHeight / 1000 * fs
				printCenteredLines(lines, pos, lineHeight + lineHeight / 2, lineWidth)
				cs.setFont(font, fontSize)
			} else {
				printCenteredAutosizedLinesInt(text, pos, lineWidth, fs + 1, minLines)
			}
		}

		def getLineHeight(fs: Int): Float = {
			cs.setFont(font, fs)
			val lineHeight = font.getFontDescriptor.getCapHeight / 1000 * fs
			cs.setFont(font, fontSize)
			lineHeight
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

		def drawRoundedRectangle(pos: Position, width: Int, height: Int, r: Int): Unit = {
			cs.moveTo(pos.x, pos.y + r)
			cs.lineTo(pos.x, pos.y + height - r) // levá nahoru
			cs.curveTo(
				pos.x, pos.y + height - r / 2,
				pos.x + r / 2, pos.y + height,
				pos.x + r, pos.y + height
			)
			cs.lineTo(pos.x + width - r, pos.y + height) // horni doprava
			cs.curveTo(
				pos.x + width - r / 2, pos.y + height,
				pos.x + width, pos.y + height - r / 2,
				pos.x + width, pos.y + height - r
			)
			cs.lineTo(pos.x + width, pos.y + r) // prava dolu
			cs.curveTo(
				pos.x + width, pos.y + r /2 ,
				pos.x + width - r /2 , pos.y ,
				pos.x + width -r, pos.y
			)
			cs.lineTo(pos.x + r, pos.y) // dolni doleva
			cs.curveTo(
				pos.x + r /2 , pos.y ,
				pos.x  , pos.y + r /2,
				pos.x, pos.y + r
	 		)
			cs.fill()
		}

		def withColor(color: Color)(fun: => Unit): Unit = {
			cs.setColor(color)
			fun
			cs.setColor(Color.BLACK)
		}
	}

}
