import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import com.konias.pojo.Api;
import com.konias.pojo.GetCourseDetails;
import com.konias.pojo.WebAutomation;

import io.restassured.RestAssured;

/**
 * This class demonstrates how to use Rest Assured to authenticate with OAuth2
 * and retrieve course details.
 * 
 * @author Kristoffer Onias
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
        String accessToken = createOauthToken();

        // Retrieve course details using the OAuth2 token
        GetCourseDetails courseDetails = getCourseDetails(accessToken);

        // Print the course details
        printCourseDetails(courseDetails);

        String getSoapUICoursePrice = getPriceByCourseTitle(courseDetails, "SoapUI Webservices testing");

        System.out.println("SoapUI Webservices testing price is:\n" + getSoapUICoursePrice);

        String[] expectedWebApiCourseTitles = {"Selenium Webdriver Java", "Cypress", "Protractor"};

        compareCourseTitles(getAllWebAutomationCourses(courseDetails), expectedWebApiCourseTitles);
    }

    /**
     * Creates an OAuth2 token using the client credentials grant type.
     * 
     * @return OAuth2 access token
     */
    private static String createOauthToken() {
        return given()
                .formParams("client_id",
                        "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParams("grant_type", "client_credentials")
                .formParams("scope", "trust")
                .when()
                .post("/oauthapi/oauth2/resourceOwner/token")
                .then()
                .extract()
                .jsonPath()
                .get("access_token");
    }

    /**
     * Retrieves course details using the access token.
     * 
     * @param accessToken access token
     * @return course details
     */
    private static GetCourseDetails getCourseDetails(String accessToken) {
        return given()
                .queryParams("access_token", accessToken)
                .when()
                .get("/oauthapi/getCourseDetails")
                .as(GetCourseDetails.class);
    }

    /**
     * Prints the course details.
     * 
     * @param courseDetails course details
     */
    private static void printCourseDetails(GetCourseDetails courseDetails) {
        System.out.println(courseDetails);
    }

    /**
     * Retrieves the price of a course by its title.
     * 
     * @param courseDetails course details
     * @param courseTitle course title
     * @return price of the course
     */
    private static String getPriceByCourseTitle(GetCourseDetails courseDetails, String courseTitle) {
        List<Api> apiCoursesList = courseDetails.getCourses().getApi();

        for (Api apiCourse : apiCoursesList) {
            if (apiCourse.getCourseTitle().equalsIgnoreCase(courseTitle)) {
                return apiCourse.getPrice();
            }
        }

        throw new IllegalArgumentException("Course not found: " + courseTitle);
    }

    /**
     * Retrieves the titles of all web automation courses.
     * 
     * @param courseDetails course details
     * @return list of course titles
     */
    private static List<String> getAllWebAutomationCourses(GetCourseDetails courseDetails) {
        List<WebAutomation> webAutomationCoursesList = courseDetails.getCourses().getWebAutomation();
        List<String> courseTitlesList = new ArrayList<>();

        for (WebAutomation webAutomationCourse : webAutomationCoursesList) {
            courseTitlesList.add(webAutomationCourse.getCourseTitle());
        }

        return courseTitlesList;
    }

    /**
     * Compares the actual list of course titles with the expected list of course titles.
     * 
     * @param actualCourseTitles actual list of course titles
     * @param expectedCourseTitles expected list of course titles
     */
    private static void compareCourseTitles(List<String> actualCourseTitles, String[] expectedCourseTitles) {
        List<String> expectedList = Arrays.asList(expectedCourseTitles);

        try {
            Assert.assertTrue(actualCourseTitles.equals(expectedList));
        } catch (AssertionError e) {
            System.out.println("Actual List of Course Titles: " + actualCourseTitles);
            System.out.println("Expected List of Course Titles: " + expectedList);
            throw new AssertionError("Actual List of Course Titles does not match the expected List of Course Titles");
        }
    }
}

