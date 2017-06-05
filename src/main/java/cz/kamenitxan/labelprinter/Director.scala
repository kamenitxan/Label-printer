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
		products.filter(p => p.isValid).par.foreach(p => generatorF.genNG.newInstance().generate(p))
	}
}
