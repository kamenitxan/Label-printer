package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.generatorsNG.impl.*;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA("toner_lamda", LamdaToner.class),
	INK_LAMDA("ink_lamda", cz.kamenitxan.labelprinter.generatorsNG.impl.LamdaInk.class),
	TONER_ALLPRINT("toner_allprint", AltxToner.class),
	INK_ALLPRINT("ink_allprint", AltxInk.class),
	INK_TESLA("ink_tesla", TeslaInk.class),
	TONER_TESLA("toner_tesla", cz.kamenitxan.labelprinter.generatorsNG.impl.TeslaToner.class);

	public String folder;
	public Class<cz.kamenitxan.labelprinter.generatorsNG.PdfGenerator> genNG;

	Generators(String folder, Class genNG) {
		this.folder = folder;
		this.genNG = genNG;
	}
}
