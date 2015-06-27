package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
		String filename = "";
        //launch(args);
        for (String arg : args) {
			if (arg.contains("-file=")) {
				arg = arg.replace("-file=", "");
				filename = arg;
				break;
			}
		}
		if (filename.equals("")) {
			System.out.println("Nezadáno jméno suboru jako parametr (-file=cesta k souboru)");
		}
		List<Product> products = ExcelReader.importFile(filename);

		products.forEach(System.out::println);
    }
}
