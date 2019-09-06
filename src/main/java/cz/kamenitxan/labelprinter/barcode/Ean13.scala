package cz.kamenitxan.labelprinter.barcode

import org.krysalis.barcode4j.impl.upcean.EAN13Bean
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
  * Created by tomaspavel on 1.3.17.
  */
object Ean13 extends BarcodeGenerator {
	@throws[Exception]
	def main(args: Array[String]): Unit = { //Create the barcode bean
		val bean = new EAN13Bean
		bean.doQuietZone(true)
		val dpi = 150
		//Configure the barcode generator
		bean.setModuleWidth(0.4) //makes the narrow bar
		//width exactly one pixel
		//bean.doQuietZone(false);
		//Open output file
		val outputFile = new File("out.png")

		val out = new FileOutputStream(outputFile)
		try { //Set up the canvas provider for monochrome PNG output
			val canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0)
			//Generate the barcode
			bean.generateBarcode(canvas, "8595617316412")
			//Signal end of generation
			canvas.finish()
		} finally if (out != null) out.close()

	}

	def createEan(ean: String, doQuietZone: Boolean): BufferedImage = {
		val bean = new EAN13Bean
		val dpi = 150
		bean.setModuleWidth(0.4)
		bean.doQuietZone(doQuietZone)
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