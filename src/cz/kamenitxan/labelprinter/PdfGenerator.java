package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.models.Manufacturer;
import cz.kamenitxan.labelprinter.models.Product;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;


public class PdfGenerator {
    public static final float pageWidth = 843;
    public static final float pageHeight = 596;
    public static final PDRectangle PAGE_SIZE_A4 = new PDRectangle( pageHeight, pageWidth );
    
    public static final float lamdaImageWidth = 90;
    public static final float lamdaImageHeight  = 196;
    public static final float labelImageWidth = 15;
    public static final float labelImageHeight  = 67;
    
    

    public static void generatePdf(Product product, ArrayList<Manufacturer> manufacturers){
        for (Manufacturer manufacturer : manufacturers) {
            PDDocument document = new PDDocument();

            PDPage page = new PDPage(PAGE_SIZE_A4);
            document.addPage(page);

            page.setRotation(90);

            PDPageContentStream contentStream;

            try {
                contentStream = new PDPageContentStream(document, page);
                PDType0Font font = PDType0Font.load(document, new File(Main.class.getResource("OpenSans-Regular.ttf").toURI()));
                PDType0Font boldFont = PDType0Font.load(document, new File(Main.class.getResource("OpenSans-Bold.ttf").toURI()));

                //obrazky
                PDImageXObject lamdaImage = PDImageXObject.createFromFile("img/lamda.jpg", document);
                PDImageXObject labelImage = PDImageXObject.createFromFile("img/label.jpg", document);
                if (!System.getProperty("os.name").equals("Mac OS X")) {
                    font = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\src\\cz\\kamenitxan\\labelprinter\\OpenSans-Regular.ttf"));
                    boldFont = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\src\\cz\\kamenitxan\\labelprinter\\OpenSans-Bold.ttf"));
                    lamdaImage = PDImageXObject.createFromFile("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\lamda.jpg", document);
                    labelImage = PDImageXObject.createFromFile("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\label.jpg", document);
                }

                float firstH = 0;
                float secondH = pageHeight/3;
                float thirdH = 2*secondH;

                //barevne obdelniky
                paintRectangle(firstH, contentStream, getProductColor(product.color));
                paintRectangle(secondH, contentStream, getProductColor(product.color));
                paintRectangle(thirdH, contentStream, getProductColor(product.color));


                contentStream.drawImage(lamdaImage, 0, ((pageWidth / 3) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, 0, ((4 * (pageWidth / 5)) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight / 3) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight / 3) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);

                contentStream.drawImage(lamdaImage, pageHeight / 3, ((pageWidth / 3) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, pageHeight / 3, ((4 * (pageWidth / 5)) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((2 * (pageHeight / 3)) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((2 * (pageHeight / 3)) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);

                contentStream.drawImage(lamdaImage, (2 * (pageHeight / 3)), ((pageWidth / 3) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(lamdaImage, (2 * (pageHeight / 3)), ((4 * (pageWidth / 5)) - (lamdaImageHeight / 2)), lamdaImageWidth, lamdaImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight) - labelImageWidth - 5), ((pageWidth / 3) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);
                contentStream.drawImage(labelImage, ((pageHeight) - labelImageWidth - 5), ((4 * (pageWidth / 5)) - (labelImageHeight / 2)), labelImageWidth, labelImageHeight);


                //text barvy
                contentStream.setNonStrokingColor(switchColor(product.color));
                contentStream.beginText();
                contentStream.setFont(font, 12);

                contentStream.newLineAtOffset((pageHeight / 6) - 30, 10);
                contentStream.showText(product.color);
                contentStream.newLineAtOffset(0, pageWidth - 30);
                contentStream.showText(product.color);
                contentStream.newLineAtOffset((pageHeight / 3), -(pageWidth - 30));
                contentStream.showText(product.color);
                contentStream.newLineAtOffset(0, pageWidth - 30);
                contentStream.showText(product.color);
                contentStream.newLineAtOffset((pageHeight / 3), -(pageWidth - 30));
                contentStream.showText(product.color);
                contentStream.newLineAtOffset(0, pageWidth - 30);
                contentStream.showText(product.color);


                //texty
                contentStream.setNonStrokingColor(Color.BLACK);
                // matice(výška, 0, 0, šířka, y, x)
                Matrix matrix = new Matrix(1, 0, 0, 1, ((pageHeight / 6) + 15), 50);
                matrix.rotate(Math.toRadians(90));
                contentStream.setTextMatrix(matrix);
                //název produktu
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(product.name);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText(product.name);
                //Katalogové číslo
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-460, -30);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                //Kapacita
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.capacity);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.capacity);
                //Výrobce
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(-665, -15);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                //Kód produktu
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.productCode);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.productCode);
                //Kód výrobce
                contentStream.newLineAtOffset(-350, -15);
                contentStream.showText(manufacturer.code);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(manufacturer.code);
                // matice(výška, 0, 0, šířka, y, x)
                Matrix matrix2 = new Matrix(1, 0, 0, 1, ((pageHeight / 2) + 15), 50);
                matrix2.rotate(Math.toRadians(90));
                contentStream.setTextMatrix(matrix2);
                //název produktu
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(product.name);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText(product.name);
                //Katalogové číslo
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-460, -30);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                //Kapacita
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.capacity);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.capacity);
                //Výrobce
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(-665, -15);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                //Kód produktu
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.productCode);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.productCode);
                //Kód výrobce
                contentStream.newLineAtOffset(-350, -15);
                contentStream.showText(manufacturer.code);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(manufacturer.code);
                // matice(výška, 0, 0, šířka, y, x)
                Matrix matrix3 = new Matrix(1, 0, 0, 1, ((5 * (pageHeight / 6)) + 15), 50);
                matrix3.rotate(Math.toRadians(90));
                contentStream.setTextMatrix(matrix3);
                //název produktu
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText(product.name);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText(product.name);
                //Katalogové číslo
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-460, -30);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Katalogové číslo: ");
                contentStream.setFont(boldFont, 14);
                contentStream.showText(product.invNum);
                //Kapacita
                contentStream.setFont(font, 14);
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.capacity);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.capacity);
                //Výrobce
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(-665, -15);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                //Kód produktu
                contentStream.newLineAtOffset(-145, 0);
                contentStream.showText(product.productCode);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(product.productCode);
                //Kód výrobce
                contentStream.newLineAtOffset(-350, -15);
                contentStream.showText(manufacturer.code);
                contentStream.newLineAtOffset(350, 0);
                contentStream.showText(manufacturer.code);


                contentStream.endText();

                //linky
                contentStream.moveTo(0, (6 * pageWidth) / 10);
                contentStream.lineTo(pageHeight, (6 * pageWidth) / 10);
                contentStream.stroke();

                contentStream.moveTo(pageHeight / 3, 0);
                contentStream.lineTo(pageHeight / 3, pageWidth);
                contentStream.stroke();

                contentStream.moveTo((pageHeight / 3) * 2, 0);
                contentStream.lineTo((pageHeight / 3) * 2, pageWidth);
                contentStream.stroke();


                contentStream.close();
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (URISyntaxException e) {
                e.printStackTrace();
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

    private static void paintRectangle(float pos, PDPageContentStream contentStream, Color color) {

        try {
            if (color != Color.WHITE) {
                contentStream.setNonStrokingColor(color);
                contentStream.addRect(0, 0, pageHeight, 30);
                contentStream.fill();
                contentStream.addRect(0, pageWidth-30, pageHeight, 30);
                contentStream.fill();
            } else {
                float part = pageHeight / 9;
                contentStream.setNonStrokingColor(Color.CYAN);
                contentStream.addRect(pos, 0, pageHeight / 3, 30);
                contentStream.addRect(pos, pageWidth-30, pageHeight / 3, 30);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.MAGENTA);
                contentStream.addRect(pos + part, 0, pageHeight / 3, 30);
                contentStream.addRect(pos+part, pageWidth-30, pageHeight/3, 30);
                contentStream.fill();
                contentStream.setNonStrokingColor(Color.YELLOW);
                contentStream.addRect(pos + (2 * part), 0, pageHeight / 3, 30);
                contentStream.addRect(pos+(2*part), pageWidth-30, pageHeight/3, 30);
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
            case "Photo" : currentColor =  Color.BLACK;
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
        if (currentColor.equals("Black")){
             return Color.WHITE;
        }
        else{
            return Color.BLACK;
        }
    }

    private PdfGenerator()
    {
    }
}