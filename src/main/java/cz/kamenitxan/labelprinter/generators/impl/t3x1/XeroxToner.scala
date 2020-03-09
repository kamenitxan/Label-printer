package cz.kamenitxan.labelprinter.generators.impl.t3x1

import java.awt.Color

import cz.kamenitxan.labelprinter.Main
import cz.kamenitxan.labelprinter.barcode.Code39
import cz.kamenitxan.labelprinter.generators.{Generators, Toner3x1}
import cz.kamenitxan.labelprinter.models.Position
import cz.kamenitxan.labelprinter.utils.AltXAddress
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

class XeroxToner extends Toner3x1 with AltXAddress {

	override val singleWidth: Float = mmToPoints(297)
	override val singleHeight: Float = cmToPoints(21/3)
	override val fontSize: Int = 7
	val leftStart = 40
	val rightStart = 367
	var eanImage: PDImageXObject = _
	var eanImage2: PDImageXObject = _
	var rohs: PDImageXObject = _
	var ceImage: PDImageXObject = _

	override def getFolderName: String = Generators.TONER_XEROX.folder

	override def getPosition(line: Int, row: Int): Position = {
		val x = 0
		val y = singleHeight * line
		new Position(x, y)
	}

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		if (Main.debug) {
			product.productCode = "CB380A"
			product.name = "HP CLJ CP6015"
		}

		eanImage = createBarcodeImage(document, product.ean, doQuietZone = true)
		eanImage2 = createBarcodeImage(document, product.invNum, Code39)
		ceImage = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/ce-mark.png")))

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/arial.ttf"))
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
		if (borders) debugRect(pos)

		cs.drawImage(eanImage, leftStart, pos.y + 130, eanImage.getWidth * 0.42 toFloat, eanImage.getHeight * 0.40 toFloat)
		cs.drawImage(eanImage2, leftStart, pos.y + 95, eanImage.getWidth * 0.65 toFloat, eanImage.getHeight * 0.25 toFloat)
		cs.drawImage(rohs, pos.x + singleWidth - 162, pos.y + 11, rohs.getWidth * 0.28 toFloat, rohs.getHeight * 0.28 toFloat)
		ceImage(pos)
		staticText(pos)
		variableText(pos)
	}

	private def staticText(pos: Position): Unit = {
		alternativeText(pos)
		xeroxText(pos)
		contentsText(pos + (-22, 73))
		manufacturer(pos + (590, -137))
	}

	private def alternativeText(pos: Position): Unit = {
		cs.printLines(XeroxToner.ALT, pos + (86, 88), 6, 5)
		cs.withColor(Color.GRAY) {
			cs.printLines(XeroxToner.ALT, pos + (rightStart-60, 50), 12, 14)
		}
	}

	private def xeroxText(pos: Position): Unit = {
		cs.printLines(XeroxToner.XEROX, pos + (-22, 24), 6, 340, 5.5f)
	}

	private def contentsText(pos: Position): Unit = {
		cs.printLines(XeroxToner.CONTENTS, pos, 8, 8)
		cs.printLines(XeroxToner.MADEIN, pos + (108, 0), 8, 8)
	}

	private def variableText(pos: Position): Unit = {
		val top = 47
		colorBox(pos + (258, top + 6), 54, 65, pos + (215, 105), 7)
		colorBox(pos + (singleWidth - 98, top - 8), 66, 78, pos + (singleWidth - 135, 98), 8)

		cs.print(product.invNum, pos.x + 141, pos.y + singleHeight - 45, 33)
		cs.print(product.productCode, pos.x + 146, pos.y + singleHeight - 69, 15)
		cs.withColor(Color.GREEN.darker()) {
			cs.print(product.name, pos.x + rightStart, pos.y + singleHeight - 39, 27)
		}
		cs.withColor(Color.GRAY) {
			cs.print(product.productCode, pos.x + rightStart, pos.y + 12, 26)
			val v = product.color.colorNames
			val colorText = {
				if (v.nonEmpty) {
					v.init.map(n => n + " toner • ").mkString + v.last + " toner"
				} else {
					""
				}
			}
			cs.printLines(colorText, pos + (rightStart - 60, 102), 25, 140, 16)
		}
	}

	private def colorBox(pos: Position, width: Int, height: Int, textPos: Position, fs: Int): Unit = {
		cs.setColor(product.color.color)
		cs.drawRoundedRectangle(pos, width, height, 6)
		cs.setColor(product.color.textColor)
		//cs.setColor(Color.RED)
		cs.printLines(product.color.colorNames, textPos, 9, fs)
		cs.setColor(Color.BLACK)
	}

	def ceImage(pos: Position): Unit = {
		val scale = 0.12
		cs.drawImage(ceImage, pos.x + singleWidth - 73, pos.y + 12,  ceImage.getWidth * scale toFloat, ceImage.getHeight * scale toFloat)
	}

	def manufacturer(pos: Position): Unit = {
		val leftPadding = 20
		val top = pos.y + singleHeight - 6
		val lh = 6
		val fs = 5
		cs.print(manufacturer1, pos.x + leftPadding, top, fs)
		cs.print(manufacturer2, pos.x + leftPadding, top - lh * 1, fs)
		cs.print(company, pos.x + leftPadding, top - lh * 2, fs)
		cs.print(street, pos.x + leftPadding, top - lh * 3, fs)
		cs.print(city, pos.x + leftPadding, top - lh * 4, fs)
		cs.print(companyId, pos.x + leftPadding, top - lh * 5, fs)
	}
}

//noinspection SpellCheckingInspection
object XeroxToner {
	val ALT = List("Replaces/Alternativa/Alternatíva/", "Alternatywa dla/Echivalent")
	val XEROX = "©2018 Xerox Corporation. All rights reserved. Xerox® and Xerox and Design® are trademarks of Xerox Corporation in the United States and/or other countries. HP®, LaserJet, and CB380A are trademarks of Hawlett Packard Industries Ltd."
	val CONTENTS = List(
		"Contents: 1 Cartrige",
		"Složení: 1 kazeta",
		"Zloženie: 1 kazeta",
		"Tartalom: 1 kazetta",
		"Zawoera: 1 kasete",
		"Continut: 1 cartus"
	)
	val MADEIN = List(
		"Made in Czech Republic",
		"Vyrobeno v České republice",
		"Vyroboné v Čechách",
		"Made in Czech Republic",
		"Wyprodukowane w Czechach",
		"Produs in Republica Ceha"
	)
}
