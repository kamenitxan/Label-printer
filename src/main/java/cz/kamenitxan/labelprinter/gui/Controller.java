package cz.kamenitxan.labelprinter.gui;

import cz.kamenitxan.labelprinter.generators.Generators;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

	@FXML
	private Button generateBtn;
	@FXML
	private Button fileBtn;
	@FXML
	private ChoiceBox<Generators> type;
	@FXML
	private TextField file;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Generators> items = Arrays.stream(Generators.values()).filter(g -> g.genNG != null).collect(Collectors.toList());
		type.setItems(FXCollections.observableList(items));
	}
}
