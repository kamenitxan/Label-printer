package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color

import javax.imageio.ImageIO
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Ink9x4
import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 8.4.17.
  */
class LamdaInk extends Ink9x4 {
	override def getFolderName: String = Generators.INK_LAMDA.folder

	var logo: PDImageXObject = _
	var icons: PDImageXObject = _

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)

		logo = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/lamda2.jpg")))
		icons = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/label2.jpg")))

		for (line <- 0 to 8; row <- 0 to 3) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)
		if(!onlyBorders) {
			cs.setColor(Color.BLACK)

			cs.drawImage(logo, pos.x + 15, pos.y + 5, logo.getWidth * 0.1 toFloat, logo.getHeight * 0.1 toFloat)
			cs.drawImage(icons, pos.x + 70, pos.y + 5, logo.getWidth * 0.18 toFloat, logo.getHeight * 0.08 toFloat)
			cs.print(product.name, pos.x + 15, pos.y + singleHeight - 10)
			cs.print("Kat. č. " + product.invNum, pos.x + 15, pos.y + 40)
			cs.print(product.capacity, pos.x + 15, pos.y + 25)
			color(pos)

			cs.beginText()
			cs.setColor(product.getColorRectTextColor)
			cs.newLineAtOffset(pos.x + 0, pos.y + 10)
			cs.setTextRotation(Math.toRadians(270), pos.x + 3, pos.y + 60)
			cs.showText(product.colorName)
			cs.setFont(font, fontSize)
			cs.endText()
			cs.setColor(Color.BLACK)

			manufaturer(pos)
			lamda(pos)
		}
	}

	private def color(pos: Position): Unit = {
		val width = 12
		val height = singleHeight
		product.color match {
			case Color.WHITE =>
				cs.setStrokingColor(Color.YELLOW)
				cs.setNonStrokingColor(Color.YELLOW)
				cs.addRect(pos.x, pos.y, width, height/3)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.MAGENTA)
				cs.setNonStrokingColor(Color.MAGENTA)
				cs.addRect(pos.x, pos.y + height/3, width, height/3)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.CYAN)
				cs.setNonStrokingColor(Color.CYAN)
				cs.addRect(pos.x, pos.y + (height/3)*2, width, height/3)
				cs.fillAndStroke()
			case _ =>
				cs.setStrokingColor(product.color)
				cs.setNonStrokingColor(product.color)
				cs.addRect(pos.x, pos.y, width, height)
				cs.fillAndStroke()
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)



	}

	private def manufaturer(pos: Position): Unit = {
		cs.setNonStrokingColor(Color.WHITE)
		cs.setStrokingColor(Color.BLACK)
		cs.addRect(pos.x + singleWidth - 22, pos.y + 28, 15, 10)
		cs.fillAndStroke()
		cs.setColor(Color.BLACK)
		cs.print(product.manufacturer, pos.x + singleWidth - 20, pos.y + 30)
	}

	private def lamda(pos: Position): Unit = {
		cs.setFont(font, 6)
		cs.print("Lamdaprint s.r.o.", pos.x + 80, pos.y + 20)
		cs.setFont(font, fontSize)
	}
}
