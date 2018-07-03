package cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1

import java.awt.Color

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.{Ean13, Position}
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.graphics.image.{LosslessFactory, PDImageXObject}
import org.apache.pdfbox.pdmodel.{PDDocument, PDPage, PDPageContentStream}

import scala.language.postfixOps

/**
  * Created by tomaspavel on 23.3.17.
  */
class LamdaToner2 extends TeslaToner {
	var logo: PDImageXObject = _
	var icons: PDImageXObject = _

	override def getFolderName: String = Generators.TONER_LAMDA_BIG.folder

	override def generatePdf(): Unit = {
		val document: PDDocument = new PDDocument
		val page: PDPage = new PDPage(PAGE_SIZE_A4)
		document.addPage(page)

		val eanRaw = Ean13.createEan(product.ean)
		eanImage = LosslessFactory.createFromImage(document, eanRaw)

		cs = new PDPageContentStream(document, page)
		font = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Regular.ttf"))
		boldFont = PDType0Font.load(document, getClass.getResourceAsStream("/OpenSans-Bold.ttf"))
		cs.setFont(font, fontSize)
		cs.setStrokingColor(Color.BLACK)

		logo = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/lamda2.jpg")))
		icons = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/label2.jpg")))

		for (line <- 0 to 5; row <- 0 to 0) {
			drawSingle(getPosition(line, row))
		}
		cs.close()
		savePdf(document)
	}

	private def drawSingle(pos: Position): Unit = {
		if(borders) debugRect(pos)
		if(!onlyBorders) {
			cs.drawImage(eanImage, pos.x + 20, pos.y + 3, eanImage.getWidth * 0.35 toFloat, eanImage.getHeight * 0.35 toFloat)
			cs.drawImage(logo, pos.x + 19, pos.y + 30, logo.getWidth * 0.30 toFloat, logo.getHeight * 0.30 toFloat)
			//cs.drawImage(icons, pos.x + 177, pos.y + 2, logo.getWidth * 0.20 toFloat, logo.getHeight * 0.07 toFloat)
			cs.drawImage(icons, pos.x + cmToPoints(16.5f), pos.y + 2, logo.getWidth * 0.20 toFloat, logo.getHeight * 0.07 toFloat)

			color(pos)
			desc(pos - (20, 0))
			desc(pos + (180, 0), withPn = false)
			manufacturer(pos)
			divider(pos)
		}
	}

	private def desc(pos: Position, withPn: Boolean = true): Unit = {
		val lineWidth = 140
		val lineHeight = 8
		val lines = splitByWidth(product.name, lineWidth)
		cs.printCenteredLines(lines, pos + (cmToPoints(3.5f), cmToPoints(3)), lineHeight, lineWidth)

		if(withPn) cs.printCentered("Kat.č: " + product.invNum, pos + (120, 21), 100, bold = true)
		//cs.print("Productcode:" + product.productCode, pos.x + 130, pos.y + 29)
		cs.print(product.capacity, pos.x + 195, pos.y + 13)
		cs.print("Lamdaprint cz", pos.x + 196, pos.y + 5)

	}

}