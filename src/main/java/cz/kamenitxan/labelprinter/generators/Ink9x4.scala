package cz.kamenitxan.labelprinter.generators

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.common.PDRectangle

/**
  * Created by tomaspavel on 6.3.17.
  */
abstract class Ink9x4 extends PdfGenerator {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageHeight, wholePageWidth)
	val singleWidth = 134
	val singleHeight = 84
	override val fontSize = 8

	override def getPosition(line: Int, row: Int): Position = {
		val x = 30 + singleWidth * row + 4 * row
		val y = 22 + singleHeight * line + 5 * line
		new Position(x, y)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}

}
