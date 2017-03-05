package cz.kamenitxan.labelprinter.models;

import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tomaspavel on 1.3.17.
 */
public class Ean13Test {
	public static void main(String[] args) throws Exception {
		//Create the barcode bean
		EAN13Bean bean = new EAN13Bean();

		final int dpi = 150;

		//Configure the barcode generator
		bean.setModuleWidth(0.4); //makes the narrow bar
		//width exactly one pixel
		//bean.doQuietZone(false);

		//Open output file
		File outputFile = new File("out.png");
		try (OutputStream out = new FileOutputStream(outputFile)) {
			//Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			//Generate the barcode
			bean.generateBarcode(canvas, "8595617316412");

			//Signal end of generation
			canvas.finish();
		}
	}

	public static BufferedImage createEan(String ean) {
		EAN13Bean bean = new EAN13Bean();
		final int dpi = 150;

		//Configure the barcode generator
		bean.setModuleWidth(0.4); //makes the narrow bar
		//width exactly one pixel
		bean.doQuietZone(false);

		try {
			//Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(null, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			//Generate the barcode
			bean.generateBarcode(canvas, ean);

			//Signal end of generation
			canvas.finish();
			return canvas.getBufferedImage();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
