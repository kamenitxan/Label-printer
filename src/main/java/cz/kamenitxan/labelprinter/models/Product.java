package cz.kamenitxan.labelprinter.models;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 27.06.15.
 */
public class Product {
	public String invNum = "";
	public String name = "";
	public String capacity = "";
	public String colorName = "";
	public Color color;
	public String hexColor = "";
	public String productCode = "";
	public String ean = "";
	public String eanCode = "";
	public String manufacturer = "";

	Function<Product, Boolean> validator;

	public Product() {
	}

	public Product(String invNum, String name, String capacity, String colorName, String productCode, String ean, String eanCode) {
		this.invNum = invNum;
		this.name = name;
		this.capacity = capacity;
		this.colorName = colorName;
		this.color = getProductColor(colorName);
		this.hexColor =  String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
		this.productCode = productCode;
		this.ean = ean;
		this.eanCode = eanCode;

		if (this.name != null) {
			this.name = this.name.replace("+", "+ ");
		}
	}

	public Product(String invNum, String name, String capacity, String colorName, String productCode, String ean, String eanCode, Function<Product, Boolean> validator) {
		this.invNum = invNum;
		this.name = name;
		this.capacity = capacity;
		this.colorName = colorName;
		this.color = getProductColor(colorName);
		this.hexColor =  String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
		this.productCode = productCode;
		this.ean = ean;
		this.eanCode = eanCode;

		if (this.name != null) {
			this.name = this.name.replace("+", "+ ");
			this.name = this.name.replace("/", "/ ");
		}
		this.validator = validator;
	}

	public boolean isValid() {
		if (validator == null) return true;
		return validator.apply(this);
	}

	public Map<String, Object> getContext() {
		Map<String, Object> context = new HashMap<>();
		context.put("invNum", invNum);
		context.put("name", name);
		context.put("capacity", capacity);
		context.put("colorName", colorName);
		context.put("color", color);
		context.put("productCode", productCode);
		context.put("ean", ean);
		context.put("eanCode", eanCode);
		context.put("hexColor", hexColor);
		context.put("manufacturer", manufacturer);
		return context;
	}

        
	@Override
	public String toString() {
		return "Product{" +
				"invNum='" + invNum + '\'' +
				", name='" + name + '\'' +
				", capacity='" + capacity + '\'' +
				", colorName='" + colorName + '\'' +
                                ", productCode='" + productCode + '\'' +
				'}';
	}

	public static Color getProductColor(String color) {
		Color currentColor;
		switch (color) {
			case "Black":
				currentColor = Color.BLACK;
				break;
			case "Yellow":
				currentColor = Color.YELLOW;
				break;
			case "Cyan":
				currentColor = Color.CYAN;
				break;
			case "Magenta":
				currentColor = Color.MAGENTA;
				break;
			case "Black/Rot":
				currentColor = Color.BLACK;
				break;
			case "CMYK":
				currentColor = Color.WHITE;
				break;
			case "Color":
				currentColor = Color.WHITE;
				break;
			case "Grey":
				currentColor = Color.GRAY;
				break;
			case "Light Cyan":
				currentColor = Color.CYAN.brighter();
				break;
			case "Light Magenta":
				currentColor = Color.MAGENTA.brighter();
				break;
			case "Matte black":
				currentColor = Color.BLACK;
				break;
			case "Photo":
				currentColor = Color.WHITE;
				break;
			case "Photo Black":
				currentColor = Color.BLACK;
				break;
			case "Photo Cyan":
				currentColor = Color.CYAN;
				break;
			case "Photo Magenta":
				currentColor = Color.MAGENTA;
				break;
			case "Red/Black":
				currentColor = Color.RED;
				break;
			case "Violett":
				currentColor = Color.magenta.darker();
				break;
			default:
				currentColor = Color.BLACK;
				break;
		}
		return currentColor;
	}
}
