package cz.kamenitxan.labelprinter.generators.impl.i13x5

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.utils.LamdaAddress

class LamdaBonusInfo extends InkBonusInfo with LamdaAddress{
	override def getFolderName: String = Generators.INK_LAMDA_INFO.folder
	override val firm: String = company
}
