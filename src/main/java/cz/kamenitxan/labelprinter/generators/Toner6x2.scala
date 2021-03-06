package cz.kamenitxan.labelprinter.generators

import java.awt.Color

import cz.kamenitxan.labelprinter.models.Position
import cz.kamenitxan.labelprinter.utils.AltXAddress
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

import scala.language.postfixOps

/**
  * Created by tomaspavel on 5.3.17.
  */
abstract class Toner6x2 extends PdfGenerator with AltXAddress {
	val PAGE_SIZE_A4 = new PDRectangle(wholePageWidth, wholePageHeight)
	var eanImage: PDImageXObject = _

	val singleWidth: Float = cmToPoints(14)
	val singleHeight: Float = cmToPoints(2.8f)
	val verticalSpace: Float = mmToPoints(4.4f)
	val horizontalSpace: Float = mmToPoints(7)
	val horizontalMiddleSpace: Float = mmToPoints(3)
	val bottomVerticalSpace: Float = cmToPoints(1.1f)
	override val fontSize = 8

	override def getPosition(line: Int, row: Int): Position = {
		val x = horizontalSpace + singleWidth * row + horizontalMiddleSpace * row
		val y = bottomVerticalSpace + singleHeight * line + verticalSpace * line
		new Position(x, y toFloat)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}
}
