package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.models.Product;

/**
 * Created by tomaspavel on 4.12.16.
 */
public class TeslaToner extends PdfGenerator {
	public TeslaToner() {
		super("templates/tesla.html");
	}

	@Override
	public String getFolderName() {
		return "toner_tesla";
	}

	@Override
	public void generatePdf(Product product) {

	}
}
