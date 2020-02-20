package cz.kamenitxan.labelprinter.generators.impl.i13x5

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.utils.AltXAddress

class AltxBonusInfo extends InkBonusInfo with AltXAddress {
	override def getFolderName: String = Generators.INK_ALTX_INFO.folder
	override val firm: String = company
}
