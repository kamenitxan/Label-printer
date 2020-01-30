package cz.kamenitxan.labelprinter

import java.io.File

import cz.kamenitxan.labelprinter.generators.Generators
import cz.kamenitxan.labelprinter.models.Product

import scala.jdk.CollectionConverters._
import scala.collection.mutable

/**
  * Created by tomaspavel on 6.3.17.
  */
object Director {

	def generate(file: File, generator: Generators, borders: Boolean, onlyBorders: Boolean): Unit = {
		val products: mutable.Seq[Product] = if (generator.staticPdf) {
			mutable.Seq({
				val p = new Product
				p.invNum = generator.folder
				p.manufacturer = null
				p
			})
		} else {
			val f = ExcelReader.importFile(file, generator, Main.debug).asScala
			print("File imported")
			f
		}

		val generatorF = generator
		if (Main.debug) {
			products.filter(p => p != null && p.isValid && (p.manufacturer == "A" || p.manufacturer == null))
			  .slice(0, 20)
			  .foreach(p => generatorF.genNG.newInstance()
			  .generate(p, borders, onlyBorders))
		} else {
			//TODO: parallel
			products.filter(p => p != null && p.isValid).foreach(p => generatorF.genNG.newInstance().generate(p, borders, onlyBorders))
		}
		System.gc()
	}
}
