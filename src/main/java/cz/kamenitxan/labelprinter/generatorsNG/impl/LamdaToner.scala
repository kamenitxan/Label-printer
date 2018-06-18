package cz.kamenitxan.labelprinter.generatorsNG.impl

import java.awt.Color

import javax.imageio.ImageIO
import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.generatorsNG.Toner3x1
import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 23.3.17.
  */
class LamdaToner extends Toner3x1 {
	var logo: PDImageXObject = _
	var icons: PDImageXObject = _

	def getFolderName: String = Generators.TONER_LAMDA_OLD.folder

	def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)


		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		logo = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/lamda2.jpg")))
		icons = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/label2.jpg")))

		for (line <- 0 to 2; row <- 0 to 0) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)

		divider(pos)

		cs.drawImage(logo, pos.x + 20, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(logo, pos.x + 480, pos.y + 100, logo.getWidth * 0.55 toFloat, logo.getHeight * 0.55 toFloat)
		cs.drawImage(icons, pos.x + 375, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.drawImage(icons, pos.x + 695, pos.y + 10, logo.getWidth * 0.27 toFloat, logo.getHeight * 0.1 toFloat)
		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 20, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 20, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 115, pos.y + 55)
		cs.printBold(product.name, pos.x + 20, pos.y + 80)
		cs.print(product.capacity, pos.x + 400, pos.y + 55)
		val size: Int = (fontSize * font.getStringWidth(product.productCode) / 1000).asInstanceOf[Int]
		cs.beginText()
		cs.newLineAtOffset(pos.x+460 - size, pos.y + 40)
		cs.showText(product.productCode)
		cs.endText()

		cs.print("Výrobce: Lamdaprint cz a.s.", pos.x + 480, pos.y + 40)
		cs.print("Katalogové číslo: ", pos.x + 480, pos.y + 55)
		cs.printBold(product.invNum, pos.x + 575, pos.y + 55)
		cs.printBold(product.name, pos.x + 480, pos.y + 80)
		cs.print(product.capacity, pos.x + 720, pos.y + 55)

		val size2: Int = (fontSize * font.getStringWidth(product.productCode) / 1000).asInstanceOf[Int]
		cs.beginText()
		cs.newLineAtOffset(pos.x+780 - size2, pos.y + 40)
		cs.showText(product.productCode)
		cs.endText()

		colorRect(pos + (400, 140))
		colorRect(pos + (720, 140))

		cs.print(product.manufacturer, pos.x + 420, pos.y + 120)
		cs.print(product.manufacturer, pos.x + 740, pos.y + 120)
	}

	private def colorRect(pos: Position): Unit = {
		val height = 25
		val width = 60

		product.color match {
			case Color.WHITE =>
				cs.setStrokingColor(Color.CYAN)
				cs.setNonStrokingColor(Color.CYAN)
				cs.addRect(pos.x, pos.y, width/3, height)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.MAGENTA)
				cs.setNonStrokingColor(Color.MAGENTA)
				cs.addRect(pos.x + width/3, pos.y, width/3, height)
				cs.fillAndStroke()
				cs.setStrokingColor(Color.YELLOW)
				cs.setNonStrokingColor(Color.YELLOW)
				cs.addRect(pos.x + 2*(width/3), pos.y, width/3, height)
				cs.fillAndStroke()

			case _ =>
				cs.setStrokingColor(product.color)
				cs.setNonStrokingColor(product.color)
				cs.addRect(pos.x, pos.y, width, height)
				cs.fillAndStroke()
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)

		val size: Int = (fontSize * font.getStringWidth(product.colorName) / 1000).asInstanceOf[Int]
		val center = ((50 - size) / 2).toFloat
		product.color match {
			case Color.BLACK =>
				cs.setColor(Color.WHITE)
				cs.print(product.colorName, pos.x+center+5, pos.y+7)
			case _ =>
				cs.setColor(Color.BLACK)
				cs.print(product.colorName, pos.x+center+5, pos.y+7)
		}
		cs.setStrokingColor(Color.BLACK)
		cs.setNonStrokingColor(Color.BLACK)
	}

	private def divider(pos: Position): Unit = {
		cs.drawLine(pos + (470, 0), pos + (470, 10))
		cs.drawLine(pos + (470, singleHeight), pos + (470, singleHeight - 10))
	}
}