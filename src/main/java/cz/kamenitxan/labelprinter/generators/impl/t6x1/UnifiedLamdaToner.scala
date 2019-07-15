package cz.kamenitxan.labelprinter.generators.impl.t6x1

import cz.kamenitxan.labelprinter.generators.Generators

class UnifiedLamdaToner extends TeslaToner {
	override def getFolderName: String = Generators.TONER_LAMDA_BIG.folder
}
