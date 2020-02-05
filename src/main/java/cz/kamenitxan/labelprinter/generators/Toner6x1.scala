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
abstract class Toner6x1 extends PdfGenerator with AltXAddress {
	val PAGE_SIZE_A4: PDRectangle = PDRectangle.A4
	var eanImage: PDImageXObject = _

	override val wholePageWidth: Float = mmToPoints(210)
	override val wholePageHeight: Float = mmToPoints(297)
	val singleWidth: Float = cmToPoints(19)
	val singleHeight: Float = cmToPoints(3.8f)
	val verticalSpace: Float = cmToPoints(0.6f)
	val horizontalSpace: Float = cmToPoints(1)
	val bottomVerticalSpace: Float = cmToPoints(1.9f)
	override val fontSize = 8

	override def getPosition(line: Int, row: Int): Position = {
		val x = horizontalSpace + singleWidth * row
		val y = bottomVerticalSpace + singleHeight * line + verticalSpace * line
		new Position(x, y toFloat)
	}

	protected def debugRect(pos: Position): Unit = {
		cs.addRect(pos.x, pos.y, singleWidth, singleHeight)
		cs.setStrokingColor(Color.BLACK)
		cs.stroke()
	}
}
