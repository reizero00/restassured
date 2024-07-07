import java.util.ArrayList;

import org.testng.annotations.Test;

import com.konias.files.POIHelpers;

public class XLSTest {
    
    /**
     * This method reads data from an Excel file using Apache POI and prints the data to the console.
     * The Excel file is located in the "src/main/resources" directory and is named "xlstest.xlsx".
     * The method reads data from the sheet named "TestCases" and prints the data for the row named "Purchase".
     *
     * @throws Exception if an error occurs while reading the Excel file
     */
    @Test
    public void main() throws Exception {
        // Create an instance of the POIHelpers class
        POIHelpers poiHelpers = new POIHelpers();

        // Get the base path of the project using System.getProperty("user.dir")
        String basePath = System.getProperty("user.dir");

        // Construct the path of the Excel file
        String attachmentPath = basePath + "/src/main/resources/xlstest.xlsx";

        // Read the data from the Excel file using the readExcelData method of the POIHelpers class
        ArrayList<String> testCaseData = poiHelpers.readExcelData(attachmentPath, "TestCases", "Purchase");

        // Print the data to the console
        System.out.println(testCaseData);
    }


}
