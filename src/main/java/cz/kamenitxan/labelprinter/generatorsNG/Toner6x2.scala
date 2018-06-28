package cz.kamenitxan.labelprinter.generatorsNG

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

import scala.language.postfixOps

/**
  * Created by tomaspavel on 5.3.17.
  */
abstract class Toner6x2 extends PdfGenerator {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageWidth, wholePageHeight)
	var eanImage: PDImageXObject = _

	val singleWidth: Float = cmToPoints(14)
	val singleHeight: Float = cmToPoints(2.8f)
	val verticalSpace: Float = (wholePageHeight - (6 * singleHeight)) / 7
	val horizontalSpace: Float = (wholePageWidth - (2 * singleWidth)) / 3
	override val fontSize = 8

	override def getPosition(line: Int, row: Int): Position = {
		val x = horizontalSpace + singleWidth * row + horizontalSpace * row
		val y = verticalSpace + singleHeight * line + verticalSpace * line
		new Position(x, y toFloat)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}
}
