package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.common.PDRectangle

/**
  * Created by tomaspavel on 23.3.17.
  */
abstract class Toner3x1 extends PdfGenerator {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageWidth, wholePageHeight)

	val singleWidth = 805
	val singleHeight = 185
	override val fontSize = 12

	override def getPosition(line: Int, row: Int): Position = {
		val x = 20 + singleWidth * row + 10 * row
		val y = 10 + singleHeight * line + 10 * line
		new Position(x, y)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}
}
