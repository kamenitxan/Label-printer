package cz.kamenitxan.labelprinter.generators;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.page.PageType;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import com.mitchellbosecke.pebble.error.PebbleException;
import cz.kamenitxan.labelprinter.Main;
import cz.kamenitxan.labelprinter.PdfWrapper;
import cz.kamenitxan.labelprinter.models.Product;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by tomaspavel on 4.12.16.
 */
public class TeslaToner extends PdfGenerator {
	public TeslaToner() {
		super("templates/teslaToner.html");
		super.topBorder = 15;
		super.bottomBorder = 6;
		super.leftBorder = 7;
	}

	@Override
	public String getFolderName() {
		return "toner_tesla";
	}

	@Override
	public void generatePdf(Product product) {
		Writer writer = new StringWriter();
		Map<String, Object> context = product.getContext();
		context.put("path", Main.workDir);
		context.put("separator", File.separator);
		context.put("height", 132);
		try {
			compiledTemplate.evaluate(writer, context);
		} catch (PebbleException | IOException e) {
			e.printStackTrace();
		}

		if (Main.debug) {
			try {
				Files.write(Paths.get("html/" + product.invNum.trim() + ".html"), writer.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		PdfWrapper pdf = new PdfWrapper(); //--zoom 1.33 --dpi 130
		pdf.addParam(new Param("--orientation", "Landscape"));
		pdf.addPage(writer.toString(), PageType.htmlAsString);
		savePdf(pdf, product.invNum);
	}
}
