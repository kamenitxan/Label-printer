package cz.kamenitxan.labelprinter.generators;

import cz.kamenitxan.labelprinter.generatorsNG.impl.AltxToner;
import cz.kamenitxan.labelprinter.generatorsNG.impl.LamdaToner;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null, "toner_lamda", LamdaToner.class),
	INK_LAMDA(null, null, null),
	TONER_ALTX(null, "toner_altx", AltxToner.class),
	INK_ALTX(new AltxInk(), "ink_altx", cz.kamenitxan.labelprinter.generatorsNG.impl.AltxInk.class),
	INK_TESLA(new TeslaInk(), "ink_tesla", cz.kamenitxan.labelprinter.generatorsNG.impl.TeslaInk.class),
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
