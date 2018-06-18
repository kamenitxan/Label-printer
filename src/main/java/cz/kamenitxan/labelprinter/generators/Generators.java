package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.generatorsNG.impl.*;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA("toner_lamda", LamdaToner2.class, "Lamda toner"),
	TONER_LAMDA_OLD("toner_lamda_old", LamdaToner.class, "Lamda toner - starý"),
	INK_LAMDA("ink_lamda", LamdaInk.class, "Lamda ink"),
	TONER_ALLPRINT("toner_allprint", AltxToner.class, "Allprint toner"),
	INK_ALLPRINT("ink_allprint", AltxInk.class, "Allprint ink"),
	TONER_TESLA("toner_tesla", TeslaToner.class, "Tesla toner"),
	INK_TESLA("ink_tesla", TeslaInk.class, "Tesla ink");

	public String folder;
	public Class<cz.kamenitxan.labelprinter.generatorsNG.PdfGenerator> genNG;
	public String title;

	Generators(String folder, Class genNG, String title) {
		this.folder = folder;
		this.genNG = genNG;
		this.title = title;
	}

	@Override
	public String toString() {
		return title;
	}
}
