package cz.kamenitxan.labelprinter.models

/**
  * Created by tomaspavel on 5.3.17.
  */
class Position(val x: Float, val y: Float) {

	def +(pos: Position): Position = {
		new Position(this.x + pos.x, this.y + pos.y)
	}

	def +(pos: (Float, Float)): Position = {
		new Position(this.x + pos._1, this.y + pos._2)
	}
}
