package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.generatorsNG.impl.*;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null, "toner_lamda", LamdaToner.class),
	INK_LAMDA(null, "ink_lamda", cz.kamenitxan.labelprinter.generatorsNG.impl.LamdaInk.class),
	TONER_ALLPRINT(null, "toner_allprint", AltxToner.class),
	INK_ALLPRINT(null, "ink_allprint", AltxInk.class),
	INK_TESLA(null, "ink_tesla", TeslaInk.class),
	TONER_TESLA(new TeslaToner(), "toner_tesla", cz.kamenitxan.labelprinter.generatorsNG.impl.TeslaToner.class);

	public PdfGenerator generator;
	public String folder;
	public Class<cz.kamenitxan.labelprinter.generatorsNG.PdfGenerator> genNG;

	Generators(PdfGenerator generator, String folder, Class genNG) {
		this.generator = generator;
		this.folder = folder;
		this.genNG = genNG;
	}
}
