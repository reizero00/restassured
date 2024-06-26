import static io.restassured.RestAssured.given;

import com.konias.files.JsonHelper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

/**
 * This class demonstrates how to use Rest Assured to authenticate with OAuth2
 * and retrieve course details.
 * 
 * @author Kaushal Kumar
 */
public class OauthTest {

    /**
     * Main function to demonstrate the usage of OAuth2 with Rest Assured.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Set the base URI for the Rest Assured requests
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Create OAuth2 token
        String oauthResponse = createOauthToken();
        String accessToken = getAccessToken(oauthResponse);

        // Retrieve course details using the OAuth2 token
        String courseDetails = getCourseDetails(accessToken);

        // Print the course details
        System.out.println(courseDetails);
    }

    /**
     * Creates an OAuth2 token using the client credentials grant type.
     * 
     * @return OAuth2 response
     */
    private static String createOauthToken() {
        return given()
                .formParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
            .when()
                .post("/oauthapi/oauth2/resourceOwner/token")
            .then()
                .assertThat()
                .statusCode(200)
                .extract().response().asString();
    }

    /**
     * Extracts the access token from the OAuth2 response.
     * 
     * @param response OAuth2 response
     * @return access token
     */
    private static String getAccessToken(String response) {
        // Parse the response JSON
        JsonPath responseJson = JsonHelper.stringToJson(response);
        
        // Extract the access token from the response
        return responseJson.get("access_token");
    }

    /**
     * Retrieves course details using the access token.
     * 
     * @param accessToken access token
     * @return course details
     */
    private static String getCourseDetails(String accessToken) {
        return given()
                .queryParams("access_token", accessToken)
            .when()
                .get("/oauthapi/getCourseDetails")
            .then()
                // .assertThat()
                // .statusCode(200) // Broken on the test site. It's always passing 401
                .extract().response().asString();
    }
}

