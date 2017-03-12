package cz.kamenitxan.labelprinter.generatorsNG

import cz.kamenitxan.labelprinter.models.Position

/**
  * Created by tomaspavel on 6.3.17.
  */
abstract class Ink9x4 extends PdfGenerator {
	val singleWidth = 134
	val singleHeight = 84
	override val fontSize = 8

	override def getPosition(line: Int, row: Int): Position = {
		val x = 30 + singleWidth * row + 4 * row
		val y = 24 + singleHeight * line + 5 * line
		new Position(x, y)
	}
}
