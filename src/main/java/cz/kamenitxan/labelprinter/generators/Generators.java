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
	TONER_LAMDA("toner_lamda_6x2", LamdaToner2.class, "Lamda toner 6x2"),
	TONER_LAMDA_BIG("toner_lamda_6x1", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.LamdaToner2.class, "Lamda toner 6x1"),
	TONER_LAMDA_OLD("toner_lamda_3x1", LamdaToner.class, "Lamda toner 3x1"),
	INK_LAMDA("ink_lamda", LamdaInk.class, "Lamda ink 9x4"),
	TONER_ALLPRINT("toner_allprint_6x2", AltxToner.class, "Allprint toner 6x2"),
	TONER_ALLPRINT_BIG("toner_allprint_6x1", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.AltxToner.class,"Allprint toner 6x1"),
	INK_ALLPRINT("ink_allprint", AltxInk.class, "Allprint ink 9x4"),
	TONER_TESLA("toner_tesla_6x2", TeslaToner.class, "Tesla toner 6x2"),
	TONER_TESLA_BIG("toner_tesla_6x1", cz.kamenitxan.labelprinter.generatorsNG.impl.t6x1.TeslaToner.class, "Tesla toner 6x1"),
	INK_TESLA("ink_tesla", TeslaInk.class, "Tesla ink 9x4");

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
