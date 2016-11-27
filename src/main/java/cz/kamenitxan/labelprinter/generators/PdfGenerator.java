package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.models.Product;

/**
 * Created by tomaspavel on 27.11.16.
 */
public abstract class PdfGenerator {
	static final float pageWidth = 833;
	static final float pageHeight = 586;
	static final float wholePageWidth = 843;
	static final float wholePageHeight = 596;

	public abstract void generatePdf(Product product);
}
