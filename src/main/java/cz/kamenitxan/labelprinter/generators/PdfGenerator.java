package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import cz.kamenitxan.labelprinter.PdfWrapper;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.File;
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

	final PebbleEngine engine = new PebbleEngine.Builder().build();
	PebbleTemplate compiledTemplate;

	int leftBorder = 8;
	int rightBorder = 8;
	int topBorder = 10;
	int bottomBorder = 4;

	public PdfGenerator(final String templateName) {
		if (templateName == null) return;
		try {
			compiledTemplate = engine.getTemplate(templateName);
		} catch (PebbleException e) {
			e.printStackTrace();
		}
	}

	public abstract void generatePdf(final Product product);

	public void generate(final List<Product> products) {
		if (compiledTemplate == null) return;
		products.parallelStream().forEach(this::generatePdf);
	}

	public void savePdf(final PdfWrapper pdf, final String name) {
		pdf.addParam(new Param("--zoom", "0.78125"));
		pdf.addParam(new Param("--disable-smart-shrinking"),
				new Param("-B", bottomBorder + "mm"),
				new Param("-L", leftBorder + "mm"),
				new Param("-R", rightBorder + "mm"),
				new Param("-T", topBorder + "mm"));

		try {
			pdf.saveAs("export" + File.separator + name.trim() + ".pdf");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
