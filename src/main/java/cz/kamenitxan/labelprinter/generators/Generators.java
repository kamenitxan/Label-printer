package cz.kamenitxan.labelprinter.generators;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null, null),
	INK_LAMDA(null, null),
	TONER_ALTX(null, null),
	INK_ALTX(new AltxInk(), "ink_altx"),
	INK_TESLA(new TeslaInk(), null),
	TONER_TESLA(new TeslaToner(), null);

	public PdfGenerator generator;
	public String folder;


	Generators(PdfGenerator generator, String folder) {
		this.generator = generator;
		this.folder = folder;
	}
}
