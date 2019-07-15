package cz.kamenitxan.labelprinter.barcode

import java.awt.image.BufferedImage
import java.io.IOException

import org.krysalis.barcode4j.impl.code39.Code39Bean
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider

object Code39 extends BarcodeGenerator {

	override def createEan(ean: String): BufferedImage = {
		val bean = new Code39Bean
		val dpi = 150
		bean.setModuleWidth(0.4)
		bean.doQuietZone(false)
		try {
			val canvas = new BitmapCanvasProvider(null, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0)
			bean.generateBarcode(canvas, ean)
			canvas.finish()
			return canvas.getBufferedImage
		} catch {
			case e: IOException =>
				e.printStackTrace()
		}
		null
	}
}
