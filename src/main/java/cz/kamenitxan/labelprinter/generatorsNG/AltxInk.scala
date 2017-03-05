package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.io.File

import cz.kamenitxan.labelprinter.Utils
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.Ean13Test
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.collection.mutable

/**
  * Created by tomaspavel on 1.3.17.
  */
class AltxInk extends PdfGenerator {
	private val PAGE_SIZE_A4 = new PDRectangle(wholePageHeight, wholePageWidth)
	private var eanImage: PDImageXObject = _

	private val singleWidth = 134
	private val singleHeight = 84
	private val fontSize = 8


	override def getFolderName: String = Generators.INK_ALTX.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		var eanRaw = Ean13Test.createEan(product.ean)
		val transform = new AffineTransform()
		transform.rotate(Math.PI/2, eanRaw.getWidth()/2, eanRaw.getHeight()/2)
		transform.translate(eanRaw.getWidth()/2, eanRaw.getHeight()/2)
		val op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
		eanRaw = op.filter(eanRaw, null)
		eanRaw = Utils.trimImage(eanRaw)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)

		for (line <- 0 to 8; row <- 0 to 3) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: (Float, Float)) = {
		//cs.addRect(pos._1, pos._2, singleWidth, singleHeight)
		//cs.setStrokingColor(Color.BLACK)
		//cs.stroke()

		cs.drawImage(eanImage, pos._1+2, pos._2+3, eanImage.getWidth*0.35 toFloat, eanImage.getHeight*0.35 toFloat)

		//desc
		desc(pos)
		color(pos)

		cs.beginText()
		//color
		cs.newLineAtOffset(pos._1+60, pos._2+20)
		cs.showText(product.colorName)
		cs.endText()
		// capacity
		cs.beginText()
		cs.newLineAtOffset(pos._1+60, pos._2+10)
		cs.showText(product.capacity)
		cs.endText()
		//pn
		cs.beginText()
		cs.newLineAtOffset(pos._1+60, pos._2+10)
		cs.setFont(font, fontSize+6)
		cs.setTextRotation(Math.toRadians(270) , pos._1+40, pos._2+80)
		cs.showText(product.invNum)
		cs.setFont(font, fontSize)
		cs.endText()
	}

	private def color(posB: (Float, Float)) = {
		val pos = (posB._1+53, posB._2+8)
		product.color match {
			case Color.WHITE =>
				cs.setStrokingColor(Color.CYAN)
				cs.setNonStrokingColor(Color.CYAN)
				cs.addRect(pos._1, pos._2, 5, 6)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.MAGENTA)
				cs.setNonStrokingColor(Color.MAGENTA)
				cs.addRect(pos._1, pos._2+6, 5, 6)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.YELLOW)
				cs.setNonStrokingColor(Color.YELLOW)
				cs.addRect(pos._1, pos._2+12, 5, 6)
				cs.fillAndStroke()
			case _ =>
				cs.setStrokingColor(product.color)
				cs.setNonStrokingColor(product.color)
				cs.addRect(pos._1, pos._2, 5, 18)
				cs.fillAndStroke()
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
	}

	private def desc(pos: (Float, Float)) = {
		var myLine: String = ""
		val allowedWidth = 60
		val lineHeight = 10
		var y = singleHeight

		var lines: mutable.MutableList[String] = mutable.MutableList()

		// get all words from the text
		// keep in mind that words are separated by spaces -> "Lorem ipsum!!!!:)" -> words are "Lorem" and "ipsum!!!!:)"
		val words: Array[String] = product.name.split(" ")
		for (word <- words) {
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
		for (line <- lines) {
			cs.beginText()
			cs.newLineAtOffset(pos._1+60, pos._2+y-lineHeight)
			cs.showText(line)
			//cs.moveTextPositionByAmount(60, y)
			//cs.drawString(line)
			cs.endText()
			y -= lineHeight
		}

	}

	private def getPosition(line: Int, row: Int) = {
		val x = 28 + singleWidth*row + 4*row
		val y = 24 + singleHeight*line + 5*line
		(x toFloat, y toFloat)
	}
}
