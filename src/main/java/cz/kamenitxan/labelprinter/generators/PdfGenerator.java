package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by tomaspavel on 27.11.16.
 */
public abstract class PdfGenerator {
	static final float pageWidth = 833;
	static final float pageHeight = 586;
	static final float wholePageWidth = 843;
	static final float wholePageHeight = 596;

	public abstract void generatePdf(Product product);

	public void generate(List<Product> products) {
		products.parallelStream().forEach(this::generatePdf);
	}

	public void savePdf(Pdf pdf, String name) {
		try {
			pdf.saveAs("export/" + name);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
