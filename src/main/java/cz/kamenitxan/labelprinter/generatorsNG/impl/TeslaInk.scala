package cz.kamenitxan.labelprinter.generatorsNG.impl

import cz.kamenitxan.labelprinter.generators.Generators

/**
  * Created by tomaspavel on 5.3.17.
  */
class TeslaInk extends AltxInk {
	override def getFolderName: String = Generators.INK_TESLA.folder
}
