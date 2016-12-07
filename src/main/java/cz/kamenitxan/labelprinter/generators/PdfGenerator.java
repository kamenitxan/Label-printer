package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import cz.kamenitxan.labelprinter.PdfWrapper;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by tomaspavel on 27.11.16.
 */
public abstract class PdfGenerator {
	static final float pageWidth = 833;
	static final float pageHeight = 586;
	static final float wholePageWidth = 843;
	static final float wholePageHeight = 596;
	static final String exportFolder = "export";

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
		try {
			Files.createDirectories(Paths.get(exportFolder + File.separator + getFolderName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract String getFolderName();

	public abstract void generatePdf(final Product product);

	public void generate(final List<Product> products) {
		if (compiledTemplate == null) return;
		products.parallelStream().filter(Product::isValid).forEach(this::generatePdf);
	}

	public void savePdf(final PdfWrapper pdf, final String name) {
		if ("linux".equals(System.getProperty("os.name").toLowerCase())) {
			pdf.addParam(new Param("--zoom", "0.78125"));
		}
		pdf.addParam(new Param("--disable-smart-shrinking"), new Param("--javascript-delay", "500"),
				new Param("-B", bottomBorder + "mm"),
				new Param("-L", leftBorder + "mm"),
				new Param("-R", rightBorder + "mm"),
				new Param("-T", topBorder + "mm"));

		try {
			pdf.saveAs(exportFolder + File.separator + getFolderName() + File.separator + name.trim() + ".pdf");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}


}
