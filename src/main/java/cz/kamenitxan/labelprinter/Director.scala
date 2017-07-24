package cz.kamenitxan.labelprinter

import java.io.File

import cz.kamenitxan.labelprinter.generators.Generators

import scala.collection.JavaConverters._

/**
  * Created by tomaspavel on 6.3.17.
  */
object Director {

	def generate(file: File, generator: Generators): Unit = {
		val products = ExcelReader.importFile(file, generator).asScala
		val generatorF = generator
		if (Main.debug) {
			products.filter(p => p.isValid).slice(0, 50).par.foreach(p => generatorF.genNG.newInstance().generate(p))
		} else {
			products.filter(p => p.isValid).par.foreach(p => generatorF.genNG.newInstance().generate(p))
		}
	}
}
