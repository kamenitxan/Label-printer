package cz.kamenitxan.labelprinter.models;

import java.awt.*;
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
	public String ean2 = "";
	public String manufacturer = "";

	public Function<Product, Boolean> validator;

	public Product() {
	}

	public Product(String invNum, String name, String capacity, String colorName, String productCode, String ean, String ean2) {
		this.invNum = invNum;
		this.name = name;
		this.capacity = capacity;
		this.colorName = colorName;
		this.color = getProductColor(colorName);
		this.hexColor =  String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
		this.productCode = productCode;
		this.ean = ean;
		this.ean2 = ean2;

		if (this.name != null) {
			this.name = this.name.replace("+", "+ ");
		}
	}

	public Product(String invNum, String name, String capacity, String colorName, String productCode, String ean, String ean2, Function<Product, Boolean> validator, String manufacturer) {
		this.invNum = invNum;
		this.name = name;
		this.capacity = capacity;
		this.colorName = colorName;
		this.color = getProductColor(colorName);
		this.hexColor =  String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
		this.productCode = productCode;
		this.ean = ean;
		this.ean2 = ean2;

		if (this.name != null) {
			this.name = this.name.replace("+", "+ ");
			this.name = this.name.replace("/", "/ ");
		}
		this.validator = validator;
		this.manufacturer = manufacturer;
	}

	public boolean isValid() {
		if (validator == null) return true;
		return validator.apply(this);
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
			case "Yellow":
				currentColor = Color.YELLOW;
				break;
			case "Cyan":
			case "Photo Cyan":
				currentColor = Color.CYAN;
				break;
			case "Magenta":
			case "Photo Magenta":
				currentColor = Color.MAGENTA;
				break;
			case "CMYK":
			case "Photo":
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
			case "Red/Black":
				currentColor = Color.RED;
				break;
			case "Violett":
				currentColor = Color.magenta.darker();
				break;
			case "Photo Black":
			case "Matte black":
			case "Black/Rot":
			case "Black":
			default:
				currentColor = Color.BLACK;
				break;
		}
		return currentColor;
	}

	public Color getColorRectTextColor() {
		switch (colorName) {
			case "Black":
			case "Photo Black":
			case "Matte black":
			case "Black/Rot":
				return Color.WHITE;
			case "Violett":
			case "Red/Black":
			case "Photo Magenta":
			case "Photo Cyan":
			case "Photo":
			case "Light Magenta":
			case "Light Cyan":
			case "Grey":
			case "Color":
			case "CMYK":
			case "Magenta":
			case "Cyan":
			case "Yellow":
			default:
				return Color.BLACK;
		}
	}
}
