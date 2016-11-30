package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.page.PageType;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import cz.kamenitxan.labelprinter.PdfWrap;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by tomaspavel on 30.11.16.
 */
public class AltxInk extends PdfGenerator {
	private static final PebbleEngine engine = new PebbleEngine.Builder().build();
	private static final PebbleTemplate compiledTemplate;

	static {
		PebbleTemplate tmpl = null;
		try {
			tmpl = engine.getTemplate("templates/altx.html");
		} catch (PebbleException e) {
			e.printStackTrace();
		}
		compiledTemplate = tmpl;
	}

	@Override
	public void generatePdf(Product product) {
		Writer writer = new StringWriter();
		try {
			compiledTemplate.evaluate(writer, product.getContext());
		} catch (PebbleException | IOException e) {
			e.printStackTrace();
		}

		try {
			Files.write(Paths.get("html/" + product.invNum.trim() + ".html"), writer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		PdfWrap pdf = new PdfWrap(); //--zoom 1.33 --dpi 130
		pdf.addPage(writer.toString(), PageType.htmlAsString);
		pdf.addParam(new Param("--zoom", "0.78125"));
		pdf.addParam(new Param("--disable-smart-shrinking"), new Param("-B", "5mm"),
				new Param("-L", "5mm"), new Param("-R", "5mm"), new Param("-T", "5mm"));

		try {
			pdf.saveAs("export/" + product.invNum.trim() + ".pdf");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
