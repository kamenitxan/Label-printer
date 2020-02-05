package cz.kamenitxan.labelprinter.generators.impl.t6x1

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.Position
import javax.imageio.ImageIO
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory

import scala.language.postfixOps

/**
  * Created by tomaspavel on 13.3.17.
  */
class AltxToner extends TeslaToner {
	override def getFolderName: String = Generators.TONER_ALLPRINT_BIG.folder

	override def rohsImage(pos: Position): Unit = {
		val rohs = LosslessFactory.createFromImage(document, ImageIO.read(getClass.getResourceAsStream("/rohs.jpg")))
		cs.drawImage(rohs, pos.x + 329, pos.y + 65, rohs.getWidth * 0.168 toFloat, rohs.getHeight * 0.168 toFloat)
	}
}
