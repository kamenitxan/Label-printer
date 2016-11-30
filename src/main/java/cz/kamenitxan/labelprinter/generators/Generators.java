package cz.kamenitxan.labelprinter.generators;

/**
 * Created by tomaspavel on 30.11.16.
 */
public enum Generators {
	TONER_LAMDA(null),
	INK_LAMDA(null),
	TONER_ALTX(null),
	INK_ALTX(new AltxInk()),
	TONER_TESLA(null);

	public PdfGenerator generator;

	Generators(PdfGenerator generator) {
		this.generator = generator;
	}
}
