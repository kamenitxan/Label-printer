package cz.kamenitxan.labelprinter.models;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 27.06.15.
 */
public class Product {
	public String invNum;
	public String name;
	public String capacity;
	public String color;

	public Product(String invNum, String name, String capacity, String color) {
		this.invNum = invNum;
		this.name = name;
		this.capacity = capacity;
		this.color = color;
	}
}
