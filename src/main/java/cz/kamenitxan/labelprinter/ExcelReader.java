package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.generators.Generators;
import cz.kamenitxan.labelprinter.models.Manufacturer;
import cz.kamenitxan.labelprinter.models.Product;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 27.06.15.
 */
public class ExcelReader {
	private static Logger logger = Logger.getLogger(ExcelReader.class);

	public static List<Product> importFile(String filename, Generators generator, boolean debug) {
		return importFile(new File(filename), generator, debug);
	}

	public static List<Product> importFile(File FileR, Generators generator, boolean debug) {
		logger.info("Importing file: " + FileR.getAbsolutePath());
		final List<Product> products = new ArrayList<>();

		System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.SystemOutLogger");
		System.setProperty("poi.log.level", POILogger.INFO + "");

		FileInputStream file;
		XSSFWorkbook workbook;

		try {
			file = new FileInputStream(FileR);
			workbook = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			logger.error("Soubor nenalezen", e);
			Utils.showException(e, "Soubor nenalezen");
			return products;
		} catch (Exception e) {
			logger.error("Neočekávaná chyba při čtení souboru", e);
			Utils.showException(e, "Neočekávaná chyba při čtení souboru");
			return products;
		}
		FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);

		final XSSFSheet sheet = workbook.getSheetAt(0);
		sheet.removeRow(sheet.getRow(0));

		final ArrayList<Manufacturer> manufacturers = importManufacturers(FileR);

		long start = System.nanoTime();
		Iterable<Row> iterable = sheet::rowIterator;

		Spliterator<Row> spliterator = new RowSpliterator(iterable, sheet.getPhysicalNumberOfRows());

		boolean parallel = debug || sheet.getPhysicalNumberOfRows() > 50;
		StreamSupport.stream(spliterator, parallel).forEach(row -> {
			try {
				manufacturers.forEach(m -> products.add(createAltXInk(row, evaluator, m.code)));
			} catch (NullPointerException ex) {
				logger.info("NULL na radce " + row.getRowNum());
			}
		});
		long stop = System.nanoTime();
		logger.info("imported total - "+ (stop - start) / 1000000000.0 + "s");

		return products;
	}

	private static final Function<Product, Boolean> validator = p -> p.invNum() != null && !p.invNum().equals("");

	private static Product createAltXInk(Row row, FormulaEvaluator evaluator, String manu) {
		final String invNum = getCellValue(row.getCell(0), evaluator); // A
		final String productCode = getCellValue(row.getCell(1), evaluator); // B
		final String name = getCellValue(row.getCell(5), evaluator); // F
		final String capacity = getCellValue(row.getCell(7), evaluator); // H
		final String colorName = getCellValue(row.getCell(6), evaluator); // G
		final String ean = getCellValue(row.getCell(9), evaluator);  // I
		final String ean2 = getCellValue(row.getCell(11), evaluator); // L
		return new Product(invNum, name, capacity, colorName, productCode, ean, ean2, validator, manu);
	}

	/*private static Product createLamdaToner(Row row, FormulaEvaluator evaluator, String manu) {
		final Function<Product, Boolean> val = p -> p.invNum != null && !p.invNum.equals("");
		return new Product() {{
			invNum = getCellValue(row.getCell(0), evaluator);
			name = getCellValue(row.getCell(1), evaluator);
			capacity = getCellValue(row.getCell(7), evaluator);
			colorName = getCellValue(row.getCell(2), evaluator);
			color = Product.getProductColor(colorName);
			productCode = getCellValue(row.getCell(4), evaluator);
			manufacturer = manu;
			validator = val;
		}};
	}*/

	private static final Pattern lnPatter = Pattern.compile("[\n\r]");

	private static String getCellValue(Cell cell, FormulaEvaluator evaluator) {
		if (cell == null) {
			return "";
		}
		final int cellType = cell.getCellType();
		String returnValue = null;
		if (cellType == 0) {
			returnValue = new BigDecimal(cell.getNumericCellValue()).toPlainString().replace(".0", "");
		} else if (cellType == 1) {
			returnValue = cell.getStringCellValue();
		} else if (cellType == 2) {
			switch (cell.getCachedFormulaResultType()) {
				case XSSFCell.CELL_TYPE_STRING:
					returnValue = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					returnValue = String.valueOf(cell.getNumericCellValue());
					break;
			}
		}
		if (returnValue != null) {
			return lnPatter.matcher(returnValue).replaceAll(" ");
		} else {
			return "";
		}
	}

	public static ArrayList<Manufacturer> importManufacturers(File fileR) {
		ArrayList<Manufacturer> manufacturers = new ArrayList<>();

		FileInputStream file;
		XSSFWorkbook workbook;

		try {
			file = new FileInputStream(fileR);
			workbook = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			logger.error("Soubor nenalezen", e);
			return manufacturers;
		} catch (IOException e) {
			logger.error("Neočekávaná chyba při čtení souboru", e);
			return manufacturers;
		}

		XSSFSheet sheet = workbook.getSheetAt(1);
		sheet.removeRow(sheet.getRow(0));


		for (Row row : sheet) {
			try {
				Manufacturer manufacturer = new Manufacturer() {{
					code = getCellValue(row.getCell(0), null);
					name = getCellValue(row.getCell(1), null);
				}};
				manufacturers.add(manufacturer);
			} catch (NullPointerException ex) {
				logger.info("NULL na radce " + row.getRowNum());
			}
		}

		return manufacturers;
	}
}
