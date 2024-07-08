import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.annotations.Test;

import com.konias.files.POIHelpers;

import io.restassured.RestAssured;

public class XLSDrivenLibraryApiTest {

    @Test
    public void main() throws Exception {
        POIHelpers poiHelpers = new POIHelpers();
        String basePath = System.getProperty("user.dir");
        String attachmentPath = basePath + "/src/main/resources/xlstest.xlsx";
        ArrayList<String> testCaseData = poiHelpers.readExcelData(attachmentPath, "RestAssured", "TestCases", "LibraryAddBook");

        // System.out.println(testCaseData);
        HashMap<String, Object> addBookPayload = new HashMap<>();

        addBookPayload.put("name", testCaseData.get(0));
        addBookPayload.put("isbn", testCaseData.get(1));
        addBookPayload.put("aisle", testCaseData.get(2));
        addBookPayload.put("author", testCaseData.get(3));

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String response = 
            given()
                .header("Content-Type", "application/json")
                .body(addBookPayload)
            .when()
                .post("/Library/Addbook.php")
            .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();

        System.out.println(response);
    }

}
