package cz.kamenitxan.labelprinter.generatorsNG.impl.t6x2

import cz.kamenitxan.labelprinter.generators.Generators

/**
  * Created by tomaspavel on 13.3.17.
  */
class AltxToner extends TeslaToner {
	override def getFolderName: String = Generators.TONER_ALLPRINT.folder
}
