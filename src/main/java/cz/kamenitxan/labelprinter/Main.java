package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.generators.Generators;
import cz.kamenitxan.labelprinter.generators.LamdaInk;
import cz.kamenitxan.labelprinter.generators.PdfGenerator;
import cz.kamenitxan.labelprinter.models.Manufacturer;
import cz.kamenitxan.labelprinter.models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main extends Application {
    private static final double startTime = System.nanoTime();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
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

		}
		if (filename.equals("")) {
			System.out.println("Nezadáno jméno souboru jako parametr (-file=cesta k souboru)");
			return;
		}
		/*if (!System.getProperty("os.name").equals("Mac OS X")) {
			filename = "C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\Lamda-import.xlsx";
		}*/

		List<Product> products = ExcelReader.importFile(filename, generator);
		if (limit) {
			products = products.subList(0, 50);
		}

		switch (generator) {
			case INK_ALTX: {
				generator.generator.generate(products);
				break;
			}
			case TONER_LAMDA: {
				final ArrayList<Manufacturer> manufacturers = ExcelReader.importManufacturers(filename);
				//products.forEach(System.out::println);
				LamdaInk.manufacturers = manufacturers;
				products.parallelStream().forEach(a -> {
					LamdaInk g = new LamdaInk();
					g.generatePdf(a);
				});
				break;
			}
		}


        System.out.println(getTime());
		//System.out.println("Uloženo " + (products.size() * manufacturers.size()) + " PDF");
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

