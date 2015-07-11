package cz.kamenitxan.labelprinter.models;

import java.util.ArrayList;

public enum Manufacturers {
    
    GRAS_KOMPATIBIL("Gras kompatibil", "GK"),
    GRAS_RENOVACE("Gras renovace", "GR"),
    KANTE("Kante", "K"),
    ORINK("Orink", "O"),
    PEACH("Peach", "P"),
    TEXPO("Texpo", "T"),
    TOP_DISTRIBUTION("Top Distribution", "TD"),
    ZM("ZM", "Z");

    
    public static final ArrayList<Manufacturers> MANUFACTURERS = new ArrayList<>(); 
    
	private String name;
	private String code;
        
            
        

	private Manufacturers(String name, String code) {
		this.name = name;
		this.code = code;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
        
        
}
