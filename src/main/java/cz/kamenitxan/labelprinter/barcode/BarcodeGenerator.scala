package cz.kamenitxan.labelprinter.barcode

import java.awt.image.BufferedImage

trait BarcodeGenerator {
	def createEan (ean: String): BufferedImage
}
