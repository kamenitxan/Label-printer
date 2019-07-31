package cz.kamenitxan.labelprinter.models

import java.awt.Color

import scala.language.implicitConversions

sealed trait ProductColor {
	val color: Color
	val textColor: Color = Color.BLACK
	val colorNames: Seq[String] = Seq()
}

object ProductColor {
	def fromString(color: String): ProductColor = {
		color match {
			case "Yellow" => Yellow
			case "Cyan" | "Photo Cyan" => Cyan
			case "Magenta" | "Photo Magenta" => Magenta
			case "CMYK" | "Color"  | "Photo" =>White
			case "Grey" => Gray
			case "Light Cyan" => LightCyan
			case "Light Magenta" => LightMagenta
			case "Red/Black" => RedBlack
			case "Violett" => Violet
			case _ => Black
		}
	}

	implicit def asOldColor(c: ProductColor): Color = c.color
}

//noinspection SpellCheckingInspection
case object Black extends ProductColor {
	override val color: Color = Color.BLACK
	override val textColor: Color = Color.WHITE
	override val colorNames: Seq[String] = Seq("Black", "Černý", "Čierný", "Fakete", "Czarny", "Negru")
}

case object White extends ProductColor {
	override val color: Color = Color.WHITE
}

//noinspection SpellCheckingInspection
case object Yellow extends ProductColor {
	override val color: Color = Color.YELLOW
	override val colorNames: Seq[String] = Seq("Yellow", "Žlutý", "Žltý", "Sárga", "Żółty", "Galben")
}

//noinspection SpellCheckingInspection
case object Cyan extends ProductColor {
	override val color: Color = Color.CYAN
	override val colorNames: Seq[String] = Seq("Cyan", "Modrý", "Niebieski", "Cián", "Albastru", "Cyjan")
}

//noinspection SpellCheckingInspection
case object Magenta extends ProductColor {
	override val color: Color = Color.MAGENTA
	override val colorNames: Seq[String] = Seq("Magenta", "Červený", "Purpurowy", "Rosu")
}

case object Gray extends ProductColor {
	override val color: Color = Color.GRAY
}

case object LightCyan extends ProductColor {
	override val color: Color = Color.CYAN.brighter()
}

case object LightMagenta extends ProductColor {
	override val color: Color = Color.MAGENTA.brighter()
}

case object RedBlack extends ProductColor {
	override val color: Color = Color.RED
}

case object Violet extends ProductColor {
	override val color: Color = Color.MAGENTA.darker()
}
