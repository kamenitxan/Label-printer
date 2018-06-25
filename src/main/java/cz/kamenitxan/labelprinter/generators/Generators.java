package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.generatorsNG.impl.i9x4.AltxInk;
import cz.kamenitxan.labelprinter.generatorsNG.impl.i9x4.LamdaInk;
import cz.kamenitxan.labelprinter.generatorsNG.impl.i9x4.TeslaInk;
import cz.kamenitxan.labelprinter.generatorsNG.impl.t3x1.LamdaToner;
import cz.kamenitxan.labelprinter.generatorsNG.impl.t6x2.AltxToner;
import cz.kamenitxan.labelprinter.generatorsNG.impl.t6x2.LamdaToner2;
import cz.kamenitxan.labelprinter.generatorsNG.impl.t6x2.TeslaToner;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA("toner_lamda", LamdaToner2.class, "Lamda toner"),
	TONER_LAMDA_BIG("toner_lamda_velky", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.LamdaToner2.class, "Lamda toner velký"),
	TONER_LAMDA_OLD("toner_lamda_old", LamdaToner.class, "Lamda toner - starý"),
	INK_LAMDA("ink_lamda", LamdaInk.class, "Lamda ink"),
	TONER_ALLPRINT("toner_allprint", AltxToner.class, "Allprint toner"),
	TONER_ALLPRINT_BIG("toner_allprint_velky", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.AltxToner.class,"Allprint toner velký"),
	INK_ALLPRINT("ink_allprint", AltxInk.class, "Allprint ink"),
	TONER_TESLA("toner_tesla", TeslaToner.class, "Tesla toner"),
	TONER_TESLA_BIG("toner_tesla_velky", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.TeslaToner.class, "Tesla toner velký"),
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
