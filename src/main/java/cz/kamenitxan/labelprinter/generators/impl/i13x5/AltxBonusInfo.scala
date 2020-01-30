package cz.kamenitxan.labelprinter.generators.impl.i13x5

import cz.kamenitxan.labelprinter.generators.Generators

class AltxBonusInfo extends InkBonusInfo {
	override def getFolderName: String = Generators.INK_ALTX_INFO.folder
	override val firm: String = "AltX Distribution a.s"
}
