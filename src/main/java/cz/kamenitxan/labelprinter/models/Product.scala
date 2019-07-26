package cz.kamenitxan.labelprinter.models

import java.awt._
import java.util.function.Function

import scala.language.implicitConversions


/**
  * Created by Kamenitxan (kamenitxan@me.com) on 27.06.15.
  */
class Product {
	var invNum = ""
	var name = ""
	var capacity = ""
	var colorName = ""
	var color: ProductColor = _
	var hexColor = ""
	var productCode = ""
	var ean = ""
	var ean2 = ""
	var manufacturer = ""
	var validator: Function[Product, java.lang.Boolean] = _

	def this(invNum: String, name: String, capacity: String, colorName: String, productCode: String, ean: String, ean2: String) {
		this()
		this.invNum = invNum
		this.name = name
		this.capacity = capacity
		this.colorName = colorName
		this.color = ProductColor.fromString(colorName)
		this.hexColor = String.format("#%02x%02x%02x", Seq(color.color.getRed, color.color.getGreen, color.color.getBlue).map(Int.box): _* )
		this.productCode = productCode
		this.ean = ean
		this.ean2 = ean2
		if (this.name != null) this.name = this.name.replace("+", "+ ")
	}

	def this(invNum: String, name: String, capacity: String, colorName: String, productCode: String, ean: String, ean2: String, validator: Function[Product, java.lang.Boolean], manufacturer: String) {
		this()
		this.invNum = invNum
		this.name = name
		this.capacity = capacity
		this.colorName = colorName
		this.color = ProductColor.fromString(colorName)
		this.hexColor = String.format("#%02x%02x%02x",  Seq(color.color.getRed, color.color.getGreen, color.color.getBlue).map(Int.box): _*)
		this.productCode = productCode
		this.ean = ean
		this.ean2 = ean2
		if (this.name != null) {
			this.name = this.name.replace("+", "+ ")
			this.name = this.name.replace("/", "/ ")
		}
		this.validator = validator
		this.manufacturer = manufacturer
	}

	def isValid: Boolean = {
		if (validator == null) return true
		validator.apply(this)
	}

	override def toString: String = "Product{" + "invNum='" + invNum + '\'' + ", name='" + name + '\'' + ", capacity='" + capacity + '\'' + ", colorName='" + colorName + '\'' + ", productCode='" + productCode + '\'' + '}'

	def getColorRectTextColor: Color = color.textColor

}