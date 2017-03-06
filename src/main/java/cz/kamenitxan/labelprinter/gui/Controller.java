package cz.kamenitxan.labelprinter.gui;

import cz.kamenitxan.labelprinter.Director;
import cz.kamenitxan.labelprinter.Main;
import cz.kamenitxan.labelprinter.generators.Generators;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
	static Logger logger = Logger.getLogger(Controller.class);


	@FXML
	private Button generateBtn;
	@FXML
	private Button fileBtn;
	@FXML
	private ChoiceBox<Generators> type;
	@FXML
	private TextField file;
	@FXML
	ProgressIndicator progressIndicator;

	private File selectedFile;
	DirectorService ds = new DirectorService();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Generators> items = Arrays.stream(Generators.values()).filter(g -> g.genNG != null).collect(Collectors.toList());
		type.setItems(FXCollections.observableList(items));
		progressIndicator.setVisible(false);

		ds.setOnSucceeded(e -> {
			progressIndicator.setVisible(false);
			//reset service
			ds.reset();
			logger.info("Export done");
		});

	}

	@FXML
	private void sourceFileChooseAction() {
		final FileChooser directoryChooser = new FileChooser();
		selectedFile = directoryChooser.showOpenDialog(Main.primaryStage);
		if (selectedFile != null) {
			file.setText(selectedFile.getName());
		}
	}

	@FXML
	private void generateAction() {
		Generators generator = type.getValue();
		logger.info("Starting export");
		progressIndicator.setVisible(true);

		ds.file = selectedFile;
		ds.generator = generator;

		if(!ds.isRunning()) {
			ds.start();
		}


	}
}
