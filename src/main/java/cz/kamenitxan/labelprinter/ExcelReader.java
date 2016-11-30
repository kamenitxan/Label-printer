package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.generators.Generators;
import cz.kamenitxan.labelprinter.models.Manufacturer;
import cz.kamenitxan.labelprinter.models.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.POILogger;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 27.06.15.
 */
public class ExcelReader {

	public static List<Product> importFile(String filename, Generators generator) {
		List<Product> products = new ArrayList<>();

		System.setProperty("org.apache.poi.util.POILogger", "org.apache.poi.util.SystemOutLogger");
		System.setProperty("poi.log.level", POILogger.INFO + "");

		FileInputStream file;
		XSSFWorkbook workbook;

		try {
			file = new FileInputStream(new File(filename));
			workbook = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			System.out.println("Soubor nenalezen!!!");
			return products;
		} catch (IOException e) {
			e.printStackTrace();
			return products;
		}
		FormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);

		XSSFSheet sheet = workbook.getSheetAt(0);
		sheet.removeRow(sheet.getRow(0));


		for (Row row : sheet) {
			try {
				/*if (Objects.equals(getCellValue(row.getCell(0), row.getCell(0).getCellType(), evaluator), "200853")) {
					System.out.println("stuj");
					evaluator.setDebugEvaluationOutputForNextEval(true);
				}*/
				switch (generator) {
					case INK_ALTX: {
						products.add(createAltXInk(row, evaluator));
						break;
					}
					case TONER_LAMDA: {
						products.add(createLamdaToner(row, evaluator));
						break;
					}
				}

			} catch (NullPointerException ex) {
				System.out.println("NULL na radce " + row.getRowNum());
			}
		}

		return products;
	}

	private static Product createAltXInk(Row row, FormulaEvaluator evaluator) {
		String invNum = getCellValue(row.getCell(0), row.getCell(0).getCellType(), evaluator);
		String name = getCellValue(row.getCell(5), row.getCell(5).getCellType(), evaluator);
		String capacity = getCellValue(row.getCell(7), row.getCell(7).getCellType(), evaluator);
		String colorName = getCellValue(row.getCell(6), row.getCell(6).getCellType(), evaluator);
		String ean = getCellValue(row.getCell(8), row.getCell(8).getCellType(), evaluator);
		String eanCode = getCellValue(row.getCell(8), row.getCell(8).getCellType(), evaluator);
		return new Product(invNum, name, capacity, colorName, null, ean, eanCode);
	}

	private static Product createLamdaToner(Row row, FormulaEvaluator evaluator) {
		return new Product() {{
			invNum = getCellValue(row.getCell(0), row.getCell(0).getCellType(), evaluator);
			name = getCellValue(row.getCell(1), row.getCell(1).getCellType(), evaluator);
			capacity = getCellValue(row.getCell(7), row.getCell(7).getCellType(), evaluator);
			colorName = getCellValue(row.getCell(2), row.getCell(2).getCellType(), evaluator);
			productCode = getCellValue(row.getCell(4), row.getCell(4).getCellType(), evaluator);
		}};
	}

	private static String getCellValue(Cell cell, int cellType, FormulaEvaluator evaluator) {
		if (cell == null) {
			return "";
		}
		if (cellType == 0) {
			return String.valueOf(cell.getNumericCellValue()).replace(".0", "");
		} else if (cellType == 1) {
			return cell.getStringCellValue();
		} else if (cellType == 2) {
			switch (cell.getCachedFormulaResultType()) {
				case XSSFCell.CELL_TYPE_STRING:
					return cell.getStringCellValue();
				case XSSFCell.CELL_TYPE_NUMERIC:
					return String.valueOf(cell.getNumericCellValue());
			}
		}
		return "";
	}

	public static ArrayList<Manufacturer> importManufacturers(String filename) {
		ArrayList<Manufacturer> manufacturers = new ArrayList<>();

		FileInputStream file;
		XSSFWorkbook workbook;

		try {
			file = new FileInputStream(new File(filename));
			workbook = new XSSFWorkbook(file);
		} catch (FileNotFoundException e) {
			System.out.println("Soubor nenalezen!!!");
			return manufacturers;
		} catch (IOException e) {
			e.printStackTrace();
			return manufacturers;
		}

		XSSFSheet sheet = workbook.getSheetAt(1);
		sheet.removeRow(sheet.getRow(0));


		for (Row row : sheet) {
			try {
				Manufacturer manufacturer = new Manufacturer() {{
					code = getCellValue(row.getCell(0), row.getCell(0).getCellType(), null);
					name = getCellValue(row.getCell(1), row.getCell(1).getCellType(), null);
				}};
				manufacturers.add(manufacturer);
			} catch (NullPointerException ex) {
				System.out.println("NULL na radce " + row.getRowNum());
			}
		}

		return manufacturers;
	}
}
