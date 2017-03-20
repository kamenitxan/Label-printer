package cz.kamenitxan.labelprinter.gui;

import cz.kamenitxan.labelprinter.Main;
import cz.kamenitxan.labelprinter.generators.Generators;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
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
	private Preferences prefs;
	private static final String LAST_TYPE = "LAST_TYPE";
	private static final String LAST_FILE = "LAST_FILE";
	private static boolean usePrefs = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Generators lastGenerator = null;
		if (usePrefs) {
			prefs = Preferences.userNodeForPackage(this.getClass());
			String lastTypeName = prefs.get(LAST_TYPE, null);
			try {
				lastGenerator = Generators.valueOf(lastTypeName);
			} catch (IllegalArgumentException ex) {
				logger.error("Could not load saved type: " + lastTypeName, ex);
			}
			String lastFilePath = prefs.get(LAST_FILE, null);
			if (lastFilePath != null) {
				File lastFile = new File(lastFilePath);
				if (lastFile.exists()) {
					selectedFile = lastFile;
					file.setText(selectedFile.getName());
				}
			}
		}

		List<Generators> items = Arrays.stream(Generators.values()).filter(g -> g.genNG != null).collect(Collectors.toList());
		type.setItems(FXCollections.observableList(items));
		if (lastGenerator != null) {
			type.getSelectionModel().select(lastGenerator);
		}

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
			if (usePrefs) {
				prefs.put(LAST_FILE, selectedFile.getAbsolutePath());
			}
		}
	}

	@FXML
	private void generateAction() {
		Generators generator = type.getValue();
		if (usePrefs) {
			prefs.put(LAST_TYPE, generator.name());
		}
		logger.info("Starting export");

		if (selectedFile == null || !selectedFile.exists()) {
			String fileName = selectedFile != null ? selectedFile.getAbsolutePath() : "";
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Nebyl vybran zdrojový soubor");
			alert.setHeaderText(null);
			alert.setContentText("Zdrojový soubor nebyl vybrán nebo nebyl nalezen. Zkontrolujte správnost výběru.\n" + fileName);

			alert.showAndWait();
			return;
		}

		progressIndicator.setVisible(true);
		ds.file = selectedFile;
		ds.generator = generator;

		if (!ds.isRunning()) {
			ds.start();
		}


	}
}
