package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.models.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PdfGenerator {
    public static final float pageWidth = 843;
    public static final float pageHeigth = 596;
    public static final PDRectangle PAGE_SIZE_A4 = new PDRectangle( pageHeigth, pageWidth );
    
    

    public static void generatePdf(Product product){

		PDDocument document = new PDDocument();

		PDPage page = new PDPage(PAGE_SIZE_A4);
		document.addPage( page );

		page.setRotation(90);

		PDPageContentStream contentStream;
        try {
            contentStream = new PDPageContentStream(document, page);
			PDType0Font font = PDType0Font.load(document, new File("/Users/tomaspavel/Dropbox/Dokumenty/Programovani/labelprinter/src/cz/kamenitxan/labelprinter/OpenSans-Regular.ttf"));

            //barevne obdelniky
            contentStream.setNonStrokingColor(getProductColor(product.color));
            contentStream.fillRect(0, 0, pageHeigth, 30);
            contentStream.fillRect(0, pageWidth-30, pageHeigth, 30);
            
            //text barvy
            contentStream.setNonStrokingColor(switchColor(product.color));
            contentStream.beginText();
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( ((pageHeigth/3)-(pageHeigth/6))-30, 10 );
            contentStream.drawString( product.color );
            contentStream.moveTextPositionByAmount( 0, pageWidth-30 );
            contentStream.drawString( product.color );
            contentStream.moveTextPositionByAmount( (pageHeigth/3), -(pageWidth-30) );
            contentStream.drawString( product.color );
            contentStream.moveTextPositionByAmount( 0, pageWidth-30 );
            contentStream.drawString( product.color );
            contentStream.moveTextPositionByAmount( (pageHeigth/3), -(pageWidth-30) );
            contentStream.drawString( product.color );
            contentStream.moveTextPositionByAmount( 0, pageWidth-30 );
            contentStream.drawString( product.color );
            
            
            //texty
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.setTextRotation(90*Math.PI*0.25,110,100);
            contentStream.moveTextPositionByAmount( 0, 0 );
            //contentStream.setFont( boldFont, 12 );
            contentStream.drawString( product.name );
            contentStream.setFont( font, 12 );
            contentStream.moveTextPositionByAmount( 0, -15 );
            contentStream.drawString("Katalogové číslo: " + product.invNum );
            contentStream.moveTextPositionByAmount( 300, 0 );
            contentStream.drawString( product.capacity + " stran" );
            contentStream.moveTextPositionByAmount( -300, -15 );
            contentStream.drawString("Výrobce: Lamdaprint cz s.r.o.");
            
            
            contentStream.endText();
            
            //obrazky
            InputStream in = new FileInputStream(new File("img//lamda.jpg"));
           // PDJpeg img = new PDJpeg(document, in);
            // width, 0, 0, height, x, y - šířka a výška je naopak, kvůli rotaci
            AffineTransform at = new AffineTransform(50, 0, 0, 200, 70, 80);
            at.rotate(Math.toRadians(90));
            //contentStream.drawXObject(img, at);
            
            
            
            
            contentStream.close();
        } catch (IOException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    

        try {
            document.save("pdf/" + product.invNum + ".pdf");
        } catch (FileNotFoundException ex) {
            System.out.println("Nenalezena složka pro výstup!!!");
        } catch (IOException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void generateSupplier(Product product, String code){
    
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
            default : currentColor = Color.WHITE;
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
    
    
    
    
    public PdfGenerator()
    {
    }

}
