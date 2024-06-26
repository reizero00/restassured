import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.konias.files.JsonHelper;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String payloadString = JsonHelper.getJsonFromFile("F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\addBook.json");

        JSONObject payloadObject = new JSONObject(payloadString);

        payloadObject
            .put("isbn", isbn)
            .put("aisle", aisle);

        System.out.println(payloadObject.toString());
        // Add book

        String response = 
        given()
            .header("Content-Type", "application/json")
            .body(payloadObject.toString())
        .when()
            .post("/Library/Addbook.php")
        .then()
            .assertThat()
            .statusCode(200)
            .extract().response().asString();

        JsonPath AddBookJsonObject = JsonHelper.stringToJson(response);

        String bookId = AddBookJsonObject.get("ID");

        System.out.println("This is the book id: \n" + bookId);

    }

    @Test(dataProvider = "BooksData")
    public void deleteBook(String isbn, String aisle) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
       
        String payloadString = "{}";
        
        JSONObject payloadObject = new JSONObject(payloadString);
        
        payloadObject.put("ID", isbn + aisle);
        
        String responseString =
        given()
            .header("Content-Type", "application/json")
            .body(payloadObject.toString())
        .when()
            .delete("/Library/DeleteBook.php")
        .then()
            .assertThat()
            .statusCode(200)
            .body("msg", equalTo("book is successfully deleted"))
            .extract().response().asString();

        JsonPath responseObject = JsonHelper.stringToJson(responseString);

        String deleteSuccessMessage = responseObject.get("msg");

        Assert.assertEquals(deleteSuccessMessage, "book is successfully deleted");
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][] {
            {"rai", "123"},
            {"gai", "456"},
            {"xero", "696"}
        };
    }   
}


