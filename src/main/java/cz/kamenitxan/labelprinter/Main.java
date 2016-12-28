package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.generators.Generators;
import cz.kamenitxan.labelprinter.generators.LamdaInk;
import cz.kamenitxan.labelprinter.models.Product;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main  {
	static Logger logger = Logger.getLogger(Main.class);
    private static final double startTime = System.nanoTime();
    public static boolean debug = false;
	public static String workDir;

    public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		workDir = Paths.get(".").toAbsolutePath().normalize().toString();


		String filename = "";
		Boolean limit = false;
		Generators generator = null;
        //launch(args);
        for (String arg : args) {
			if (arg.contains("-file=")) {
				arg = arg.replace("-file=", "");
				filename = arg;
			}
			if (arg.contains("-limit")) {
				limit = true;
			}
			if (arg.contains("-generator")) {
				arg = arg.replace("-generator=", "");
				generator = Generators.valueOf(arg);
			}
			if (arg.contains("-debug")) {
				debug = true;
			}

		}
		if (filename.equals("")) {
        	logger.fatal("Nezadáno jméno souboru jako parametr (-file=cesta k souboru)");
			System.out.println("Nezadáno jméno souboru jako parametr (-file=cesta k souboru)");
			return;
		}
		if (generator == null) {
        	logger.fatal("Nezadán typ jako parametr (-generator={TONER_LAMDA|INK_ALTX})");
			System.out.println("Nezadán typ jako parametr (-generator={TONER_LAMDA|INK_ALTX})");
			return;
		}

		List<Product> products = ExcelReader.importFile(filename, generator);
		if (limit) {
			products = products.subList(0, 5);
		}

		switch (generator) {
			case INK_ALTX:
			case TONER_TESLA: {
				generator.generator.generate(products);
				break;
			}
			case TONER_LAMDA: {
				//products.forEach(System.out::println);
				LamdaInk.manufacturers = ExcelReader.importManufacturers(filename);
				products.parallelStream().forEach(a -> {
					LamdaInk g = new LamdaInk();
					g.generatePdf(a);
				});
				break;
			}
		}

		logger.info(getTime());
		logger.info("Uloženo " + (products.size() * products.size()) + " PDF");
		System.exit(0);
    }

	/**
	 * @return elapsed time of generation
	 */
	private static String getTime(){
		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		final Date today = Calendar.getInstance().getTime();
		final String reportDate = df.format(today);
		final double cas = (System.nanoTime() - Main.startTime) / 1000000000;

		return "Generováno " + reportDate + ". Export trval " + cas + " s";
	}
    
}

