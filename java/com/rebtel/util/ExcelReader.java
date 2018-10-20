package com.rebtel.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.rebtel.testbed.core.TestBedConfig;

public class ExcelReader {

	private static Log log = LogUtil.getLog(ExcelReader.class);

	/**
	 * CommonMethod(readExcelData) which reads the data from the excel using
	 * specified data key
	 * 
	 * @param filePath
	 * @param sheetName
	 * @param tableName
	 * @return
	 */
	public static String[][] readExcelData(String filePath, String sheetName, String tableName) {
		String[][] testData = null;
		System.setProperty("sCurrentTestCase", sheetName);
		System.setProperty("sDataSheet", tableName);
		log.info("Reading file, sheet, table::: " + filePath + ", " + sheetName + ", " + tableName);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
			HSSFSheet sheet = workbook.getSheet(sheetName);
			HSSFCell[] boundaryCells = findCell(sheet, tableName);
			HSSFCell startCell = boundaryCells[0];
			HSSFCell endCell = boundaryCells[1];
			int startRow = startCell.getRowIndex() + 1;
			int endRow = endCell.getRowIndex();
			int startCol = startCell.getColumnIndex() + 1;
			int endCol = endCell.getColumnIndex() - 1;
			testData = new String[endRow - startRow + 1][endCol - startCol + 1];
			for (int i = startRow; i < endRow + 1; i++) {
				for (int j = startCol; j < endCol + 1; j++) {
					if (sheet.getRow(i).getCell(j).getCellType() == HSSFCell.CELL_TYPE_STRING) {
						testData[i - startRow][j - startCol] = sheet.getRow(i).getCell(j).getStringCellValue();
					} else if (sheet.getRow(i).getCell(j).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						Double temp = sheet.getRow(i).getCell(j).getNumericCellValue();
						testData[i - startRow][j - startCol] = String.valueOf(temp.intValue());
					}
				}
			}
		} catch (FileNotFoundException fnfe) {
			log.error(
					"Could not find the Excel file in path:::" + filePath + ".  Error message:::" + fnfe.getMessage());
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			log.error("Could not read the Excel sheet in path:::" + filePath + ". Error message:::" + ioe.getMessage());
			ioe.printStackTrace();
		} catch (Exception e) {
			log.error(" Failed to read data from excel sheet due to :::" + e.getMessage());
			e.printStackTrace();
		}
		return testData;
	}

	/**
	 * CommonMethod(findCell) which reads the data from the excel using text
	 * 
	 * @param sheet
	 * @param text
	 * @return
	 */
	public static HSSFCell[] findCell(HSSFSheet sheet, String text) {

		String pos = "start";
		HSSFCell[] cells = new HSSFCell[2];
		try {
			for (Row row : sheet) {
				for (Cell cell : row) {
					if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING && text.equals(cell.getStringCellValue())) {
						if (pos.equalsIgnoreCase("start")) {
							cells[0] = (HSSFCell) cell;
							pos = "end";
						} else {
							cells[1] = (HSSFCell) cell;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(" Failed to find cell in excel due to :::" + e.getMessage());
			e.printStackTrace();
		}
		return cells;
	}

	/**
	 * CommonMethod(readExcelDataByRow) which reads the data from the excel
	 * sheet by Row
	 * 
	 * @param filePath
	 * @param sheetName
	 * @param iRow
	 * @return
	 */
	public Map<String, Object> readExcelDataByRow(String filePath, String sheetName, int iRow) {
		Map<String, Object> data = new HashMap();

		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(filePath));
			HSSFSheet sheet = workbook.getSheet(sheetName);
			int iCol = 1;
			while (sheet.getRow(iRow).getCell(iCol) != null) {
				if (sheet.getRow(iRow).getCell(iCol).getCellType() == HSSFCell.CELL_TYPE_STRING) {
					data.put(sheet.getRow(0).getCell(iCol).toString(),
							sheet.getRow(iRow).getCell(iCol).getStringCellValue().trim());
				}
				if (sheet.getRow(iRow).getCell(iCol).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					data.put(sheet.getRow(0).getCell(iCol).toString(),
							sheet.getRow(iRow).getCell(iCol).getNumericCellValue());
				}
				iCol = iCol + 1;
			}

		} catch (FileNotFoundException e) {
			log.error("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (Exception e) {
			log.error(" Failed to read excel data by row due to :::" + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * CommonMethod(getDataInHashMap) which reads the data from the excel and
	 * 
	 * @param sheetName
	 * @param dataKey
	 * @return
	 */
	public static LinkedHashMap<String, String> getDataInHashMap(String sheetName, String dataKey) {
		LinkedHashMap<String, String> testData = new LinkedHashMap<String, String>();
		try {
			String dataSheetFilePath = TestBedConfig.getDataSheetPath();
			String[][] testDataArray = ExcelReader.readExcelData(dataSheetFilePath, sheetName, dataKey);

			for (int i = 0; i < testDataArray.length; i++) {
					testData.put(testDataArray[i][0], testDataArray[i][1]);
			}
		} catch (Exception e) {
			log.error(
					" Failed to read excel data by data key  and store in linked hash map due to :::" + e.getMessage());
			e.printStackTrace();
		}
		return testData;
	}

	/**
	 * CommonMethod(getData) which reads the data from excel
	 * 
	 * @param sheetName
	 * @param tableName
	 * @return
	 */
	public static String[][] getData(String sheetName, String tableName) {
		String[][] testData = null;
		try {
			testData = ExcelReader.readExcelData(TestBedConfig.getDataSheetPath(), sheetName, tableName);
			return testData;
		} catch (Exception e) {
			log.error("Failed to read data from excel using datakey. Error message::" + e.getMessage());
		}
		if (testData == null)
			log.error("Failed to read data from excel using datakey as testData is NULL");
		return testData;
	}
}
