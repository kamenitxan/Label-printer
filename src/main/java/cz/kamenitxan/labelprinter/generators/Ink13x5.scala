package cz.kamenitxan.labelprinter.generators

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.common.PDRectangle

/**
  * Created by tomaspavel on 6.3.17.
  */
abstract class Ink13x5 extends PdfGenerator {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageHeight, wholePageWidth)
	val singleWidth: Int = mmToPoints(38).toInt
	val singleHeight: Int = mmToPoints(21.2f).toInt
	override val fontSize = 10

	override def getPosition(line: Int, row: Int): Position = {
		val x = cmToPoints(1) + singleWidth * row
		val y = cmToPoints(1) + singleHeight * line
		new Position(x, y)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}

}
