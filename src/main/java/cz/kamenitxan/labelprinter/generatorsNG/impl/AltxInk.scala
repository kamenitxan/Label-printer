package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.io.File

import cz.kamenitxan.labelprinter.Utils
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Ink9x4
import cz.kamenitxan.labelprinter.models.{Ean13Test, Position}
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

/**
  * Created by tomaspavel on 1.3.17.
  */
class AltxInk extends Ink9x4 {
	private val PAGE_SIZE_A4 = new PDRectangle(wholePageHeight, wholePageWidth)
	private var eanImage: PDImageXObject = _


	override def getFolderName: String = Generators.INK_ALTX.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		var eanRaw = Ean13Test.createEan(product.ean)
		val transform = new AffineTransform()
		transform.rotate(Math.PI / 2, eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
		transform.translate(eanRaw.getWidth() / 2, eanRaw.getHeight() / 2)
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

	private def drawSingle(pos: Position) = {
		//cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		//cs.setStrokingColor(Color.BLACK)
		//cs.stroke()

		cs.drawImage(eanImage, pos.x + 2, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)

		//desc
		desc(pos)
		color(pos)

		cs.beginText()
		//color
		cs.newLineAtOffset(pos.x + 60, pos.y + 20)
		cs.showText(product.colorName)
		cs.endText()
		// capacity
		cs.beginText()
		cs.newLineAtOffset(pos.x + 60, pos.y + 10)
		cs.showText(product.capacity)
		cs.endText()
		//pn
		cs.beginText()
		cs.newLineAtOffset(pos.x + 60, pos.y + 10)
		cs.setFont(font, fontSize + 6)
		cs.setTextRotation(Math.toRadians(270), pos.x + 40, pos.y + 80)
		cs.showText(product.invNum)
		cs.setFont(font, fontSize)
		cs.endText()
	}

	private def color(posB: Position) = {
		val pos = new Position(posB.x + 53, posB.y + 8)
		product.color match {
			case Color.WHITE =>
				cs.setStrokingColor(Color.CYAN)
				cs.setNonStrokingColor(Color.CYAN)
				cs.addRect(pos.x, pos.y, 5, 6)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.MAGENTA)
				cs.setNonStrokingColor(Color.MAGENTA)
				cs.addRect(pos.x, pos.y + 6, 5, 6)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.YELLOW)
				cs.setNonStrokingColor(Color.YELLOW)
				cs.addRect(pos.x, pos.y + 12, 5, 6)
				cs.fillAndStroke()
			case _ =>
				cs.setStrokingColor(product.color)
				cs.setNonStrokingColor(product.color)
				cs.addRect(pos.x, pos.y, 5, 18)
				cs.fillAndStroke()
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
	}

	private def desc(pos: Position) = {
		val allowedWidth = 60
		val lineHeight = 10

		val lines = splitByWidth(product.name, allowedWidth)

		cs.printLines(lines, pos + (0, 70), lineHeight)

	}


}
