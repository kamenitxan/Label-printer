package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.models.Manufacturer;
import cz.kamenitxan.labelprinter.models.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class PdfGenerator {
    public static final float pageWidth = 833;
    public static final float pageHeight = 586;
    public static final float wholePageWidth = 843;
    public static final float wholePageHeight = 596;
    public static final PDRectangle PAGE_SIZE_A4 = new PDRectangle( wholePageHeight, wholePageWidth);

    public static final float lamdaImageWidth = 90;
    public static final float lamdaImageHeight  = 196;
    public static final float labelImageWidth = 15;
    public static final float labelImageHeight  = 67;

    public static final float margin = 5;
    public static ArrayList<Manufacturer> manufacturers = new ArrayList<>();
    
    private boolean capacityMove = false;


    public PdfGenerator() {
    }

    public void generatePdf(Product product){
        for (Manufacturer manufacturer : manufacturers) {
            PDDocument document = new PDDocument();

            PDPage page = new PDPage(PAGE_SIZE_A4);
            document.addPage(page);
            page.setRotation(90);


            try {
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                PDType0Font font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"));
                PDType0Font boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"));
                //obrazky
                PDImageXObject lamdaImage = PDImageXObject.createFromFile("img/lamda.jpg", document);
                PDImageXObject labelImage = PDImageXObject.createFromFile("img/label.jpg", document);

                float firstH = 0;
                float secondH = pageHeight/3;
                float thirdH = 2*secondH;


                contentStream.drawImage(lamdaImage, 0+margin, 25, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, 0+margin, ((6 * pageWidth) / 10)+15, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight / 3) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight / 3) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);

                contentStream.drawImage(lamdaImage, (pageHeight / 3)+2, 25, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, (pageHeight / 3)+2, ((6 * pageWidth) / 10)+15, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((2 * (pageHeight / 3)) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((2 * (pageHeight / 3)) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);

                contentStream.drawImage(lamdaImage, (2 * (pageHeight / 3))+2, 25, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, (2 * (pageHeight / 3))+2, ((6 * pageWidth) / 10)+15, lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);

                colorRectangle(getProductColor(product.color), contentStream, firstH, false);
				colorRectangle(getProductColor(product.color), contentStream, secondH, false);
				colorRectangle(getProductColor(product.color), contentStream, thirdH, false);
                colorRectangle(getProductColor(product.color), contentStream, firstH, true);
                colorRectangle(getProductColor(product.color), contentStream, secondH, true);
                colorRectangle(getProductColor(product.color), contentStream, thirdH, true);


                //text barvy
                contentStream.beginText();
                contentStream.setFont(font, 12);
                //texty
                contentStream.setNonStrokingColor(Color.BLACK);
                writeTextMatrix(((pageHeight / 6) + 15), product, manufacturer.code, font, boldFont, contentStream);
                writeTextMatrix(((pageHeight / 2) + 15), product, manufacturer.code, font, boldFont, contentStream);
                writeTextMatrix(((5 * (pageHeight / 6)) + 15), product, manufacturer.code, font, boldFont, contentStream);
                contentStream.endText();

                //linka
                contentStream.moveTo(0, (6 * pageWidth) / 10);
                contentStream.lineTo(wholePageHeight, (6 * pageWidth) / 10);
                contentStream.stroke();

                contentStream.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }

            try {
                File file = new File("pdf/" + manufacturer.name + "/" + product.invNum + ".pdf");
                file.getParentFile().mkdirs();
                document.save(file);
            } catch (FileNotFoundException ex) {
                System.out.println("Nenalezena složka pro výstup!!!");
            } catch (IOException ex) {
                System.out.println("IO Exception - nelze uložit.");

            }

            try {
                document.close();
            } catch (IOException ex) {
                System.out.println("Nelze uzavřít soubor.");
            }
        }
    }

    private void writeColorText(float y, String productColor, PDPageContentStream contentStream, PDType0Font font) throws IOException {

        if (productColor.isEmpty()) {
            productColor = "Black";
        }
        if (Color.BLACK == getProductColor(productColor)) {
            contentStream.setNonStrokingColor(Color.WHITE);
        } else {
            contentStream.setNonStrokingColor(Color.BLACK);
        }
        
        
        // 12 = fontsize
        float titleWidth = font.getStringWidth(productColor) / 1000 * 12; 
        int productColorLength = productColor.length();
        int maxLength = 10;

        if(productColorLength<=maxLength){
            contentStream.newLineAtOffset(30 -(titleWidth/2), 113);
            contentStream.showText(productColor);
            contentStream.newLineAtOffset(341, 0);
            contentStream.showText(productColor);

        } else {
            String [] splitColor = productColor.split("\\s+");
            float smallFont = 10;
            float titleWidth1 = font.getStringWidth(splitColor[0]) / 1000 * smallFont;
            float titleWidth2 = font.getStringWidth(splitColor[1]) / 1000 * smallFont;

            contentStream.setFont(font, smallFont);
            contentStream.newLineAtOffset(30 -(titleWidth1/2), 120);
            contentStream.showText(splitColor[0]);
            contentStream.newLineAtOffset((titleWidth1/2)-(titleWidth2/2), -10);
            contentStream.showText(splitColor[1]);
            contentStream.newLineAtOffset((titleWidth2/2)-(titleWidth1/2)+340, 10);
            contentStream.showText(splitColor[0]);
            contentStream.newLineAtOffset((titleWidth1/2)-(titleWidth2/2), -10);
            contentStream.showText(splitColor[1]);
        }

    }

    private void writeTextMatrix(float y, Product product, String manufacturerCode, PDType0Font font, PDType0Font boldFont, PDPageContentStream contentStream) {
        Matrix matrix = new Matrix(1, 0, 0, 1, y, 25);
                matrix.rotate(Math.toRadians(90));
        try {
			contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.setTextMatrix(matrix);
            //název produktu
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(0, 0);
                substringProductName(product.name, 54, contentStream);
                contentStream.newLineAtOffset(485, 0);
                substringProductName(product.name, 35, contentStream);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-485, -30);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(485, 0);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                //Výrobce
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(-485, -15);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                contentStream.newLineAtOffset(485, 0);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");

                contentStream.setFont(font, 14);

                //Kapacita vpravo
                contentStream.newLineAtOffset(205, 15);
                contentStream.showText(product.capacity);

                contentStream.setFont(font, 12);

                //Kód produktu vpravo
                contentStream.newLineAtOffset(0, -15);
                substringProductCode(product.productCode, 12, contentStream);

                //Kód výrobce vpravo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(manufacturerCode);

                contentStream.setFont(font, 14);

                //Kapacita vlevo
                capacityPosition(product.capacity, product.productCode, 11, contentStream);
                contentStream.showText(product.capacity);

                contentStream.setFont(font, 12);

                //Kód produktu vlevo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(product.productCode);
                //Kód výrobce vlevo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(manufacturerCode);

            contentStream.setNonStrokingColor(Color.BLACK);
            
            if (capacityMove){
                contentStream.newLineAtOffset(50, 0);
            }

            writeColorText(50, product.color, contentStream, font);

        } catch (IOException ex) {
            System.out.println("Nelze nakreslit textovou matici.");
            System.out.println("Chyba: " + ex);
        }

    }

    private void substringProductCode(String productCode, int maxLength, PDPageContentStream contentStream){
        int length = productCode.length();
        try {
			if (maxLength>=length){
				contentStream.showText(productCode);
			} else{
				String shortLine = productCode.substring(0, maxLength);
				contentStream.showText(shortLine + "...");
			}
        } catch (IOException ex) {
			System.out.println("Nelze napsat kód produktu.");
			System.out.println("Chyba: " + ex);
		}
    }

    private void substringProductName(String productName, int maxLength, PDPageContentStream contentStream){
        int length = productName.length();
        try {
			if (maxLength>=length){
				contentStream.showText(productName);
			} else{
				String firstLine = productName.substring(0, maxLength);
				String secondLine = productName.substring(maxLength);

				contentStream.showText(firstLine);
				contentStream.newLineAtOffset(0, -15);

				if (secondLine.length()<maxLength) {
					contentStream.showText(secondLine);
				} else {
					String lastLine = secondLine.substring(0, maxLength-3);
					contentStream.showText(lastLine + "...");
				}
				contentStream.newLineAtOffset(0, 15);
			}
        } catch (IOException ex) {
			System.out.println("Nelze napsat název produktu.");
			System.out.println("Chyba: " + ex);
		}

    }

    private void capacityPosition (String capacity, String productCode, int maxLength, PDPageContentStream contentStream){
        int capacityLength = capacity.length();
        int productCodeLength = productCode.length();

        try {
			if (maxLength>=capacityLength && maxLength>=productCodeLength) {
				contentStream.newLineAtOffset(-300, 30);
			} else {
				contentStream.newLineAtOffset(-350, 30);
                capacityMove = true;
                                
			}
		} catch (IOException ex) {
			System.out.println("Nelze dát pozici kapacitě.");
			System.out.println("Chyba: " + ex);
		}
	}

    /**
     * Metoda nakreslí obdélník s barvou toneru
     */
    private void colorRectangle(Color color, PDPageContentStream contentStream, float pos, boolean right) throws IOException {
        int xp = 410;
        int xk = 480;
        //final int yh = 40;
		//final int yd = 70;
        if (right) {
            xp += 340;
            xk += 340;
        }
        final float xpr1 = xp + (xk-xp)/3;
        final float xpr2= xpr1 + + (xk-xp)/3;

        if (color != Color.WHITE) {
            contentStream.setNonStrokingColor(color);
            contentStream.moveTo(pos + 40, xp+5);
            contentStream.lineTo(pos + 40, xp+5);
            contentStream.lineTo(pos + 40, xk - 10);
            contentStream.curveTo(pos + 40, xk, pos + 40, xk, pos + 50, xk);
            contentStream.lineTo(pos + 60, xk);
            contentStream.curveTo(pos + 70, xk, pos + 70, xk, pos + 70, xk-10);
            contentStream.lineTo(pos + 70, xp + 10);
            contentStream.curveTo(pos + 70, xp, pos + 70, xp, pos + 60, xp);
            contentStream.lineTo(pos + 50, xp);
            contentStream.curveTo(pos + 40, xp, pos+40, xp, pos+40, xp+ 10);
            contentStream.fill();

        } else {
            // levy kus
            contentStream.setNonStrokingColor(Color.CYAN);
            contentStream.moveTo(pos + 70, xpr1);
            contentStream.lineTo(pos + 70, xpr1);
            contentStream.lineTo(pos + 70, xp + 10);
            contentStream.curveTo(pos + 70, xp, pos + 70, xp, pos + 60, xp);
            contentStream.lineTo(pos + 50, xp);
            contentStream.curveTo(pos + 40, xp, pos + 40, xp, pos + 40, xp + 10);
            contentStream.lineTo(pos + 40, xpr1);
            contentStream.fill();

            //prostredek
            contentStream.setNonStrokingColor(Color.MAGENTA);
            contentStream.addRect(pos + 40, xpr1, 30, (xk - xp) / 3);
            contentStream.fill();

            //pravy kus
            contentStream.setNonStrokingColor(Color.YELLOW);
            contentStream.moveTo(pos + 40, xpr2);
            contentStream.lineTo(pos + 40, xpr2);
            contentStream.lineTo(pos + 40, xk - 10);
            contentStream.curveTo(pos + 40, xk, pos + 40, xk, pos + 50, xk);
            contentStream.lineTo(pos + 60, xk);
            contentStream.curveTo(pos + 70, xk, pos + 70, xk, pos + 70, xk - 10);
            contentStream.lineTo(pos + 70, xpr2 );

            contentStream.fill();
        }
		contentStream.setNonStrokingColor(Color.BLACK);

    }

    @Deprecated
    private static void paintRectangle(float pos, Color color, PDPageContentStream contentStream) {
        try {
            if (color != Color.WHITE) {
                contentStream.setNonStrokingColor(color);
                contentStream.addRect(0, 0, wholePageHeight, 30+margin);
                contentStream.fill();
                contentStream.addRect(0, wholePageWidth-30-margin, wholePageHeight, 30+margin);
                contentStream.fill();
            } else {
                float part = pageHeight / 9;
                contentStream.setNonStrokingColor(Color.CYAN);
                contentStream.addRect(pos, 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos, wholePageWidth-30-margin, pageHeight / 3, 30+margin);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.MAGENTA);
                contentStream.addRect(pos + part, 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos+part, wholePageWidth-30-margin, pageHeight/3, 30+margin);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.YELLOW);
                contentStream.addRect(pos + (2 * part), 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos+(2*part), wholePageWidth-30-margin, pageHeight/3, 30+margin);
                contentStream.fill();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Deprecated
    private static void paintColor(float pos, Color color, PDPageContentStream contentStream) {
        try {
            if (color != Color.WHITE) {
                contentStream.setNonStrokingColor(color);
                contentStream.addRect(lamdaImageHeight, lamdaImageWidth/2, 20, 50);
                contentStream.fill();
                contentStream.addRect((2*(pageWidth/3))+lamdaImageHeight, lamdaImageWidth/2, 20, 50);
                contentStream.fill();
            } else {
                float part = pageHeight / 9;
                contentStream.setNonStrokingColor(Color.CYAN);
                contentStream.addRect(pos, 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos, wholePageWidth-30-margin, pageHeight / 3, 30+margin);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.MAGENTA);
                contentStream.addRect(pos + part, 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos+part, wholePageWidth-30-margin, pageHeight/3, 30+margin);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.YELLOW);
                contentStream.addRect(pos + (2 * part), 0, pageHeight / 3, 30+margin);
                contentStream.addRect(pos+(2*part), wholePageWidth-30-margin, pageHeight/3, 30+margin);
                contentStream.fill();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Color getProductColor(String color){
        Color currentColor;
        switch (color){
            case "Black" : currentColor = Color.BLACK;
                break;
            case "Yellow" : currentColor =  Color.YELLOW;
                break;
            case "Cyan" : currentColor =  Color.CYAN;
                break;
            case "Magenta" : currentColor =  Color.MAGENTA;
                break;
            case "Black/Rot" : currentColor =  Color.BLACK;
                break;
            case "CMYK" : currentColor =  Color.WHITE;
                break;
            case "Color" : currentColor =  Color.WHITE;
                break;
            case "Grey" : currentColor =  Color.GRAY;
                break;
            case "Light Cyan" : currentColor =  Color.CYAN.brighter();
                break;
            case "Light Magenta" : currentColor =  Color.MAGENTA.brighter();
                break;
            case "Matte black" : currentColor =  Color.BLACK;
                break;
            case "Photo" : currentColor =  Color.WHITE;
                break;
            case "Photo Black" : currentColor =  Color.BLACK;
                break;
            case "Photo Cyan" : currentColor =  Color.CYAN;
                break;
            case "Photo Magenta" : currentColor =  Color.MAGENTA;
                break;
            case "Red/Black" : currentColor =  Color.RED;
                break;
            case "Violett" : currentColor =  Color.magenta.darker();
                break;
            default : currentColor = Color.BLACK;
                break;
                    }
        return currentColor;
    }

    private static Color switchColor(String currentColor){
        if (currentColor.equals("Black")) {
             return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

}