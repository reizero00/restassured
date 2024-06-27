import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.konias.pojo.GooglePlaceApis.AddPlace;
import com.konias.pojo.GooglePlaceApis.Location;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GoogleMapsTest {

    public static void main(String[] args) {
        // Set the base URI for the Rest Assured requests
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        AddPlace addPlaceRequestBody = constructAddPlaceRequest(69, "29, side layout, cohen 09",
         "French-IN", -38.383494, 33.427362, "Frontline house", "(+91) 983 893 3937",
          "http://google.com", "shoe park", "shop");

        System.out.println(addPlaceRequestBody);

        System.out.println(addPlace(addPlaceRequestBody));
    }

    public static String addPlace(AddPlace addPlaceRequestBody) {
        return given()
            .spec(rahulShettyAcademyHeader())
            .body(addPlaceRequestBody)
            .log().all()
        .when()
            .post("/maps/api/place/add/json")
        .then()
            .spec(standardResponse())
            .extract().response().asString();
    }
        
    public static AddPlace constructAddPlaceRequest(int accuracy, String address, String language,double lat, double lng,
    String name, String phoneNumber, String website, String... types) {
        AddPlace addPlace = new AddPlace();
    
        addPlace.setAccuracy(accuracy);
        addPlace.setAddress(address);
        addPlace.setLanguage(language);
        addPlace.setLocation(createLocationList(lat, lng));
        addPlace.setName(name);
        addPlace.setPhone_number(phoneNumber);
        addPlace.setTypes(createTypesList(types));
        addPlace.setWebsite(website);

        return addPlace;
    }

    private static List<String> createTypesList(String... typeStrings) {
        return new ArrayList<>(Arrays.asList(typeStrings));
    }

    private static Location createLocationList(double lat, double lng) {
        Location location = new Location();

        location.setLat(lat);
        location.setLng(lng);

        return location;
    }

    private static RequestSpecification rahulShettyAcademyHeader() {
        return new RequestSpecBuilder()
            .setBaseUri("https://rahulshettyacademy.com")
            .addQueryParam("key", "qaclick123")
            .setContentType("application/json")
            .build();
        
    }

    private static ResponseSpecification standardResponse() {
        return new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType("application/json")
            .build();

    }
}
