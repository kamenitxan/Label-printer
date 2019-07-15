package cz.kamenitxan.labelprinter.generators.impl.t6x2

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.Position
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory

import scala.language.postfixOps

/**
  * Created by tomaspavel on 13.3.17.
  */
class AltxToner extends TeslaToner {
	override def getFolderName: String = Generators.TONER_ALLPRINT.folder

	override def rohsImage(pos: Position): Unit = {
		val rohs = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/rohs.jpg")))
		//cs.drawImage(rohs, pos.x + 215, pos.y + 5, rohs.getWidth * 0.1 toFloat, rohs.getHeight * 0.1 toFloat)
		cs.drawImage(rohs, pos.x + 365, pos.y + 5, rohs.getWidth * 0.12 toFloat, rohs.getHeight * 0.12 toFloat)
	}
}
