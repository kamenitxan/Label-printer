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
                //Main.class.getResource("OpenSans-Regular.ttf").getPath();
                //PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\OpenSans-Regular.ttf"));
                //PDType0Font boldFont = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\OpenSans-Bold.ttf"));
                PDType0Font font = PDType0Font.load(document, new File("img/OpenSans-Regular.ttf"));
                PDType0Font boldFont = PDType0Font.load(document, new File("img/OpenSans-Bold.ttf"));
                //obrazky
                PDImageXObject lamdaImage = PDImageXObject.createFromFile("img/lamda.jpg", document);
                PDImageXObject labelImage = PDImageXObject.createFromFile("img/label.jpg", document);
                /*if (!System.getProperty("os.name").equals("Mac OS X")) {
                    font = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\src\\cz\\kamenitxan\\labelprinter\\OpenSans-Regular.ttf"));
                    boldFont = PDType0Font.load(document, new File("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\src\\cz\\kamenitxan\\labelprinter\\OpenSans-Bold.ttf"));
                    lamdaImage = PDImageXObject.createFromFile("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\lamda.jpg", document);
                    labelImage = PDImageXObject.createFromFile("C:\\Users\\Kateřina\\Documents\\GitHub\\Label-printer\\img\\label.jpg", document);
                }*/

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

                
                contentStream.newLineAtOffset((pageHeight / 6) - 15, 10);
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
                
                writeTextMatrix(((pageHeight / 6) + 15), contentStream, product, manufacturer.code, font, boldFont);
                writeTextMatrix(((pageHeight / 2) + 15), contentStream, product, manufacturer.code, font, boldFont);
                writeTextMatrix(((5 * (pageHeight / 6)) + 15), contentStream, product, manufacturer.code, font, boldFont);

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

    private static void writeTextMatrix(float y, PDPageContentStream contentStream, Product product, String manufacturerCode, PDType0Font font, PDType0Font boldFont) {
              
        Matrix matrix = new Matrix(1, 0, 0, 1, y, 50);
                matrix.rotate(Math.toRadians(90));
        try {
            contentStream.setTextMatrix(matrix);
            //název produktu
                contentStream.setFont(boldFont, 14);
                contentStream.newLineAtOffset(0, 0);
                substringProductName(contentStream, product.name, 54);
                contentStream.newLineAtOffset(460, 0);
                substringProductName(contentStream, product.name, 35);
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
                //Výrobce
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(-460, -15);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                contentStream.newLineAtOffset(460, 0);
                contentStream.showText("Výrobce: Lamdaprint cz s.r.o.");
                
                contentStream.setFont(font, 14);
                
                //Kapacita vpravo
                contentStream.newLineAtOffset(205, 15);
                contentStream.showText(product.capacity);
                
                contentStream.setFont(font, 12);
                
                //Kód produktu vpravo
                contentStream.newLineAtOffset(0, -15);
                substringProductCode(contentStream, product.productCode, 12);
                
                //Kód výrobce vpravo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(manufacturerCode);
                
                contentStream.setFont(font, 14);
                
                //Kapacita vlevo
                capacityPosition(contentStream, product.capacity, product.productCode, 11);
                contentStream.showText(product.capacity);
                
                contentStream.setFont(font, 12);

                //Kód produktu vlevo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(product.productCode);
                //Kód výrobce vlevo
                contentStream.newLineAtOffset(0, -15);
                contentStream.showText(manufacturerCode);
                
                
                
        } catch (IOException ex) {
            System.out.println("Nelze nakreslit textovou matici.");
            System.out.println("Chyba: " + ex);
        }
                
    }
    private static void substringProductCode(PDPageContentStream contentStream, String productCode, int maxLength){
        
        int length = productCode.length();
        try {
        if (maxLength>=length){
            
                contentStream.showText(productCode); 
        }
        else{
            String shortLine = productCode.substring(0, maxLength);
            contentStream.showText(shortLine + "...");
            }
            
        
        } catch (IOException ex) {
                System.out.println("Nelze napsat kód produktu.");
                System.out.println("Chyba: " + ex);
            }
        
    }
    
    
    private static void substringProductName(PDPageContentStream contentStream, String productName, int maxLength){
    
        
        int length = productName.length();
        try {
        if (maxLength>=length){
            
                contentStream.showText(productName); 
        }
        else{
            
            String firstLine = productName.substring(0, maxLength);
            String secondLine = productName.substring(maxLength);
            
            contentStream.showText(firstLine);
            contentStream.newLineAtOffset(0, -15);
            
            if (secondLine.length()<maxLength){
            
            contentStream.showText(secondLine);
            
            }
            else{
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
    
    private static void capacityPosition (PDPageContentStream contentStream, String capacity, String productCode, int maxLength){
    
        int capacityLength = capacity.length();
        int productCodeLength = productCode.length();
        
        try {
        if (maxLength>=capacityLength && maxLength>=productCodeLength){
   
                contentStream.newLineAtOffset(-300, 30);
        }
        else{
                contentStream.newLineAtOffset(-350, 30);
        }
            } catch (IOException ex) {
                System.out.println("Nelze dát pozici kapacitě.");
                System.out.println("Chyba: " + ex);
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