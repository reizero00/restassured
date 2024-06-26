package com.konias;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.konias.files.JsonHelper;
import org.json.JSONObject;
import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Basics {

    public static void main(String[] args) {
        // Validate if add place API is working as expected

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String addPlaceString = JsonHelper.getJsonFromFile("F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\addPlace.json");

        
        String response = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(addPlaceString)
            .when()
                .post("/maps/api/place/add/json")
            .then()
                .assertThat()
                .statusCode(200)
                .body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();
        
        System.out.println(response);

        JsonPath js = new JsonPath(response);

        String placeId = js.getString("place_id");

        System.out.println(placeId);

        // Update place

        String updatePlaceString = JsonHelper.getJsonFromFile("F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\updatePlace.json");
        JSONObject updatePlaceJson = new JSONObject(updatePlaceString);

        updatePlaceJson.put("place_id", placeId);
        
        // Convert the JSON object back to a JSON string
        updatePlaceString = updatePlaceJson.toString();

        given()
                .log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(updatePlaceString)
            .when()
                .put("/maps/api/place/update/json")
            .then()
                .assertThat()
                .log().all()
                .statusCode(200)
                .body("msg", equalTo("Address successfully updated"));
                
                
                // Get Place details
                
        String getPlaceResponse = given()
                .log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .assertThat()
                .log().all()
                .statusCode(200)
                .body("address", equalTo("70 winter walk, USA"))
                .extract().response().asString();;
        
        JsonPath getPlaceJsonObject = JsonHelper.stringToJson(getPlaceResponse);
        
        String actualAddress = getPlaceJsonObject.getString("address");

        Assert.assertEquals(actualAddress, "70 winter walk, USA");

    }
}

