import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;

import com.konias.pojo.GooglePlaceApis.AddPlace;
import com.konias.pojo.GooglePlaceApis.Location;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * This class contains the main method that demonstrates the usage of the Google Place Add API.
 * It also contains helper methods to construct the request body and set request specifications.
 */
public class GoogleMapsTest {

    /**
     * The main method that demonstrates the usage of the Google Place Add API.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Set the base URI for the Rest Assured requests
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        AddPlace addPlaceRequestBody = createAddPlaceRequest("29, side layout, cohen 09", "-38.383494", "33.427362", "Frontline house", "(+91) 983 893 3937", "http://google.com", "French-IN", "69", new String[]{"shoe park", "shop"});

        System.out.println(addPlace(addPlaceRequestBody));
    }

    /**
     * Adds a new place to the Google Map API.
     * @param addPlaceRequestBody the body of the request
     * @return the response as a string
     */
    public static String addPlace(AddPlace addPlaceRequestBody) {
        return given()
                .spec(getRequestSpecification())
                .body(addPlaceRequestBody)
                .log().all()
                .when()
                .post("/maps/api/place/add/json")
            .then()
                .spec(getResponseSpecification())
                .extract().response().asString();
    }

    /**
     * Constructs an AddPlace object with the given parameters.
     * @return the constructed AddPlace object
     */
    private static AddPlace createAddPlaceRequest(String address, String lat, String lng, String name, String phoneNumber, String website, String language, String accuracy, String[] types) {
        AddPlace addPlaceRequestBody = new AddPlace();
        addPlaceRequestBody.setAccuracy(Integer.parseInt(accuracy));
        addPlaceRequestBody.setAddress(address);
        addPlaceRequestBody.setLanguage(language);
        addPlaceRequestBody.setLocation(new Location(Double.parseDouble(lat), Double.parseDouble(lng)));
        addPlaceRequestBody.setName(name);
        addPlaceRequestBody.setPhone_number(phoneNumber);
        addPlaceRequestBody.setWebsite(website);
        addPlaceRequestBody.setTypes(new ArrayList<String>(Arrays.asList(types)));
        return addPlaceRequestBody;
    }

    /**
     * Returns a RequestSpecification object that contains the base URI and query parameters.
     * @return the RequestSpecification object
     */
    private static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType("application/json")
                .build();
    }

    /**
     * Returns a ResponseSpecification object that contains the expected status code and content type.
     * @return the ResponseSpecification object
     */
    private static ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
    }
}
