package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

/**
  * Created by tomaspavel on 5.3.17.
  */
abstract class Toner6x2 extends PdfGenerator {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageWidth, wholePageHeight)
	var eanImage: PDImageXObject = _

	val singleWidth = 390
	val singleHeight = 78
	val fontSize = 8

	def getPosition(line: Int, row: Int): Position = {
		val x = 20 + singleWidth*row + 10*row
		val y = 37 + singleHeight*line + 10*line
		new Position(x, y)
	}

	def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}
}
