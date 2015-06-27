package cz.kamenitxan.labelprinter;

import cz.kamenitxan.labelprinter.models.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

	public static List<Product> importFile(String filename) {
		List<Product> products = new ArrayList<>();


		FileInputStream file;
		XSSFWorkbook workbook;

		try {
			file = new FileInputStream(new File(filename));
			workbook = new XSSFWorkbook (file);
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
				Product product = new Product() {{
					invNum = getCellValue(row.getCell(0), row.getCell(0).getCellType(), evaluator);
					name = getCellValue(row.getCell(1), row.getCell(1).getCellType(), evaluator);
					capacity = getCellValue(row.getCell(7), row.getCell(7).getCellType(), evaluator);
					color = getCellValue(row.getCell(2), row.getCell(2).getCellType(), evaluator);
				}};
				products.add(product);
			} catch (NullPointerException ex) {
				System.out.println("NULL na radce " + row.getRowNum() );
			}
		}

		return products;
	}

	private static String getCellValue(Cell cell, int cellType, FormulaEvaluator evaluator) {
		if (cell == null) {
			return "";
		}
		if (cellType == 0) {
			return String.valueOf(cell.getNumericCellValue()).replace(".0", "");
		} else if (cellType == 1){
			return cell.getStringCellValue();
		} else if (cellType == 2) {
			int evaluatedCell = evaluator.evaluateFormulaCell(cell);
			return getCellValue(cell, evaluatedCell, evaluator);
		}
		return "";
	}
}
