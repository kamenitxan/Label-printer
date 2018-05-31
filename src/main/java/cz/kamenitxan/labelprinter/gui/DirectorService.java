package cz.kamenitxan.labelprinter.gui;

import cz.kamenitxan.labelprinter.Director;
import cz.kamenitxan.labelprinter.generators.Generators;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

/**
 * Created by tomaspavel on 6.3.17.
 */
public class DirectorService extends Service<Void> {
	public  File file;
	public  Generators generator;
	public boolean borders = false;
	public boolean onlyBorders = false;


	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Director.generate(file, generator, borders, onlyBorders);
				return null;
			}
		};
	}

}
