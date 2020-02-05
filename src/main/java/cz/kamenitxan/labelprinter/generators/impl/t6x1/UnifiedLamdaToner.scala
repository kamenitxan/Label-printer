package cz.kamenitxan.labelprinter.generators.impl.t6x1

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.utils.LamdaAddress

class UnifiedLamdaToner extends TeslaToner with LamdaAddress {
	override def getFolderName: String = Generators.TONER_LAMDA_BIG.folder
}
