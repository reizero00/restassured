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
     * @param sheetName The name of the sheet to read from.
     * @param headerName The name of the horizontal header to look for.
     * @param verticalHeaderName The name of the vertical header to look for.
     * @return A list of strings containing the data from the specified row.
     * @throws Exception If an error occurs while reading the Excel file.
     */
    public ArrayList<String> readExcelData(String filePath, String sheetName, String headerName, String verticalHeaderName) throws Exception {
        // Create a new Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook(filePath);
        // Get the specified sheet from the workbook
        XSSFSheet sheet = workbook.getSheet(sheetName);
        // Get the index of the specified header column
        int column = getColumnIndex(sheet, headerName);
        // Get the data from the specified row based on the column and vertical header names
        return getRowData(sheet, column, verticalHeaderName);
    }


    /**
     * Retrieves the index of the column with the specified horizontal header name.
     *
     * @param sheet The sheet to search for the column.
     * @param horizontalHeaderName The name of the column to search for.
     * @return The index of the column with the specified horizontal header name, or -1 if not found.
     */
    private int getColumnIndex(XSSFSheet sheet, String horizontalHeaderName) {
        int column = -1;  // Initialize column index to -1 (not found)
        Row firstRow = sheet.getRow(0);  // Get the first row of the sheet
        Iterator<Cell> cells = firstRow.cellIterator();  // Get an iterator for the cells in the row
        int k = 0;  // Initialize counter for column index

        // Iterate over each cell in the first row
        while (cells.hasNext()) {
            Cell cell = cells.next();  // Get the next cell

            // Check if the cell value matches the horizontal header name
            if (cell.getStringCellValue().equalsIgnoreCase(horizontalHeaderName)) {
                column = k;  // Set the column index to the current value of k
                break;  // Exit the loop
            }

            k++;  // Increment the counter for column index
        }

        return column;  // Return the column index
    }

    /**
     * Retrieves data from a row in an Excel sheet based on specified column and vertical header name.
     *
     * @param sheet The sheet to retrieve data from.
     * @param column The column number to search for the vertical header name.
     * @param verticalHeaderName The name to match in the specified column.
     * @return An ArrayList of strings containing the data from the specified row.
     */
    private ArrayList<String> getRowData(XSSFSheet sheet, int column, String verticalHeaderName) {
        // Initialize an ArrayList to store the test case data
        ArrayList<String> testCaseData = new ArrayList<>();

        // Iterate over each row in the sheet
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row r = sheet.getRow(i);

            // Check if the cell value in the specified column matches the vertical header name
            if (r.getCell(column).getStringCellValue().equalsIgnoreCase(verticalHeaderName)) {

                // Iterate over each cell in the row
                Iterator<Cell> cells = r.cellIterator();
                while (cells.hasNext()) {
                    Cell c = cells.next();

                    // Check the cell type and add the value to the ArrayList
                    if (c.getCellType() == CellType.STRING) {
                        testCaseData.add(c.getStringCellValue());
                    } else if (c.getCellType() == CellType.NUMERIC) {
                        testCaseData.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                    }
                }
                // Remove the first element, which is the vertical header name
                testCaseData.remove(0);

                // Break the loop once the data is retrieved
                break;
            }
        }

        // Return the test case data
        return testCaseData;
    }
}