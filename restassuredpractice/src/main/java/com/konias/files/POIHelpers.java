package com.konias.files;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIHelpers {

    /**
     * Reads data from an Excel file.
     * 
     * @param filePath The path of the Excel file.
     * @param headerName The name of the horizontal header to look for.
     * @param verticalHeaderName The name of the vertical header to look for.
     * @return A list of strings containing the data from the specified row.
     * @throws Exception If an error occurs while reading the Excel file.
     */
    public ArrayList<String> readExcelData(String filePath, String headerName, String verticalHeaderName) throws Exception {
        // Create a new XSSFWorkbook object from the file path
        XSSFWorkbook workbook = new XSSFWorkbook(filePath);
        // Get the column index of the specified horizontal header name
        int column = getColumnIndex(workbook, headerName);
        // Get the data from the specified row using the column index and vertical header name
        return getRowData(workbook, column, verticalHeaderName);
    }

    /**
     * Retrieves the index of the specified horizontal header name in the Excel file.
     *
     * @param workbook the Excel workbook
     * @param horizontalHeaderName the name of the horizontal header to look for
     * @return the index of the horizontal header, or -1 if not found
     */
    public int getColumnIndex(XSSFWorkbook workbook, String horizontalHeaderName) {
        int column = -1; // Initialize column index to -1

        // Iterate over each sheet in the workbook
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            Iterator<Row> rows = sheet.iterator();

            // Get the first row of the sheet
            Row firstRow = rows.next();
            Iterator<Cell> cells = firstRow.cellIterator();
            int k = 0; // Initialize cell index

            // Iterate over each cell in the first row
            while (cells.hasNext()) {
                Cell cell = cells.next();

                // Check if the cell value matches the horizontal header name
                if (cell.getStringCellValue().equalsIgnoreCase(horizontalHeaderName)) {
                    column = k; // Update the column index
                    break; // Exit the loop
                }

                k++; // Increment the cell index
            }
        }

        return column; // Return the column index
    }

    /**
     * Retrieves data from a specific row in the Excel file.
     *
     * @param workbook the Excel workbook
     * @param column the index of the column containing the vertical header name
     * @param verticalHeaderName the name of the vertical header to look for
     * @return a list of strings containing the data from the specified row
     */
    public ArrayList<String> getRowData(XSSFWorkbook workbook, int column, String verticalHeaderName) {
        // Initialize an array list to store the test case data
        ArrayList<String> testCaseData = new ArrayList<>();

        // Iterate over each sheet in the workbook
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            Iterator<Row> rows = sheet.iterator();

            // Iterate over each row in the sheet
            while (rows.hasNext()) {
                Row r = rows.next();

                // Check if the cell value in the specified column matches the vertical header name
                if (r.getCell(column).getStringCellValue().equalsIgnoreCase(verticalHeaderName)) {
                    Iterator<Cell> cells = r.cellIterator();

                    // Iterate over each cell in the row and add its value to the test case data
                    while (cells.hasNext()) {
                        Cell c = cells.next();
                        // Check if the cell type is STRING or NUMERIC because POI is strictly typed
                        if(c.getCellType() == CellType.STRING){
                            // Add the cell value to the test case data
                            testCaseData.add(c.getStringCellValue());
                        }
                        else if(c.getCellType() == CellType.NUMERIC){
                            // Add the cell value to the test case data
                            testCaseData.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                        }
                    }
                }
            }
        }

        // Return the test case data
        return testCaseData;
    }
}
