package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.generators.Generators;
import cz.kamenitxan.labelprinter.generators.LamdaInk;
import cz.kamenitxan.labelprinter.models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static spark.Spark.*;

/*
C:\Users\IEUser\Desktop\lb>java -jar Labelprinter.one-jar.jar -zoom=1 -file=TESLA_code_creator_INK.xlsm -generator=INK_ALTX -cmd="C:\Program Files\wkhtmltopdf\bin\wkhtmltopdf.exe" -debug
 */

public class Main extends Application {
	static Logger logger = Logger.getLogger(Main.class);
	private static final double startTime = System.nanoTime();
	public static boolean debug = false;
	public static String workDir;
	public static String cmd;
	public static String separator = "\\";
	public static String zoom = "0.78125";
	public static boolean gui = true;
	public static Stage primaryStage = null;

	static {
		if ("linux".equals(System.getProperty("os.name").toLowerCase())) {
			separator = "/";
		}
	}

	public static void main(String[] args) {
		logger.info("Labelprinter started");
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
			if (arg.contains("-cmd")) {
				arg = arg.replace("-cmd=", "");
				cmd = arg;
			}
			if (arg.contains("-zoom")) {
				arg = arg.replace("-zoom=", "");
				zoom = arg;
			}
			if (arg.contains("-nogui")) {
				gui = false;
			}

		}
		if (!gui) {
			if (filename.equals("")) {
				logger.fatal("Nezadáno jméno souboru jako parametr (-file=cesta k souboru)");
				System.err.println("Nezadáno jméno souboru jako parametr (-file=cesta k souboru)");
				return;
			}
			if (generator == null) {
				logger.fatal("Nezadán typ jako parametr (-generator={TONER_LAMDA|INK_ALTX})");
				System.err.println("Nezadán typ jako parametr (-generator={TONER_LAMDA|INK_ALTX})");
				return;
			}

			/*staticFiles.externalLocation(workDir + "/img");
			port(9400);
			before((request, response) -> {
				response.header("Access-Control-Allow-Origin", "*");
			});
			get("/", (req, res) -> "Hello World");*/
		}


		if (!gui) {
			List<Product> products = ExcelReader.importFile(filename, generator);
			if (limit) {
				products = products.subList(0, 5);
			}

			switch (generator) {
				case TONER_LAMDA: {
					//products.forEach(System.out::println);
					LamdaInk.manufacturers = ExcelReader.importManufacturers(filename);
					products.parallelStream().forEach(a -> {
						LamdaInk g = new LamdaInk();
						g.generatePdf(a);
					});
					break;
				}
				default: {
					generator.generator.generate(products);
					final Generators generatorF = generator;
					products.parallelStream().filter(Product::isValid).forEach(p -> {
						try {
							generatorF.genNG.newInstance().generate(p);
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					});


					break;
				}

			}

			logger.info(getTime());
			logger.info("Uloženo " + products.stream().filter(Product::isValid).count() + " PDF");
			System.exit(0);
		} else {
			logger.info("Gui started");
			logger.info("Params: " + Arrays.toString(args));
			launch(args);
		}
	}

	/**
	 * @return elapsed time of generation
	 */
	private static String getTime() {
		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		final Date today = Calendar.getInstance().getTime();
		final String reportDate = df.format(today);
		final double cas = (System.nanoTime() - Main.startTime) / 1000000000;

		return "Generováno " + reportDate + ". Export trval " + cas + " s";
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		primaryStage.setTitle("LabelPrinter");
		Parent root = FXMLLoader.load(Main.class.getResource("/main.fxml"));

		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

