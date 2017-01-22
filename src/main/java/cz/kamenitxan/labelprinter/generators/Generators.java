package cz.kamenitxan.labelprinter.generators;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null),
	INK_LAMDA(null),
	TONER_ALTX(null),
	INK_ALTX(new AltxInk()),
	INK_TESLA(new TeslaInk()),
	TONER_TESLA(new TeslaToner());

	public PdfGenerator generator;

	Generators(PdfGenerator generator) {
		this.generator = generator;
	}
}
