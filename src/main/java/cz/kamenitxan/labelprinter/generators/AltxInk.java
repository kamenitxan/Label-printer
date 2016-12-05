package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.page.PageType;
import com.mitchellbosecke.pebble.error.PebbleException;
import cz.kamenitxan.labelprinter.PdfWrapper;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by tomaspavel on 30.11.16.
 */
public class AltxInk extends PdfGenerator {

	public AltxInk() {
		super("templates/altx.html");
	}

	@Override
	public void generatePdf(Product product) {
		Writer writer = new StringWriter();
		Map<String, Object> context = product.getContext();

		if (product.name.length() > 15) {
			context.put("fs", "14px");
		}
		if (product.name.length() > 20) {
			context.put("fs", "12px");
		}
		if (product.name.length() > 30) {
			context.put("fs", "8px");
		}
		try {
			compiledTemplate.evaluate(writer, context);
		} catch (PebbleException | IOException e) {
			e.printStackTrace();
		}

		try {
			Files.write(Paths.get("html/" + product.invNum.trim() + ".html"), writer.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		PdfWrapper pdf = new PdfWrapper(); //--zoom 1.33 --dpi 130
		pdf.addPage(writer.toString(), PageType.htmlAsString);
		savePdf(pdf, product.invNum);
		/*
		try {
			pdf.saveAs("export/" + product.invNum.trim() + ".pdf");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}*/

	}

}
