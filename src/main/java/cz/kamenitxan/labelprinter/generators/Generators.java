package cz.kamenitxan.labelprinter.generators;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null, null, null),
	INK_LAMDA(null, null, null),
	TONER_ALTX(null, null, null),
	INK_ALTX(new AltxInk(), "ink_altx", cz.kamenitxan.labelprinter.generatorsNG.AltxInk.class),
	INK_TESLA(new TeslaInk(), null, null),
	TONER_TESLA(new TeslaToner(), null, null);

	public PdfGenerator generator;
	public String folder;
	public Class<cz.kamenitxan.labelprinter.generatorsNG.PdfGenerator> genNG;

	Generators(PdfGenerator generator, String folder, Class genNG) {
		this.generator = generator;
		this.folder = folder;
		this.genNG = genNG;
	}
}
