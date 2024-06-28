import static io.restassured.RestAssured.given;

import java.io.File;

import com.konias.files.JsonHelper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;


/**
 * This class demonstrates how to create a Jira issue with an attachment using the Jira REST API and RestAssured.
 * It creates a bug with an attachment and verifies the response status code.
 * 
 * @author Konias
 */
public class CreateBugWithAttachmentTest {

    /**
     * This method demonstrates how to create a bug with an attachment.
     * It sends a POST request to the Jira REST API to create a new bug with the provided issue details.
     * It then extracts the Jira issue key from the response and uses it to add an attachment to the bug.
     * It verifies the response status code for both requests.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        createBugWithAttachment();
    }

    /**
     * This method creates a bug with an attachment.
     * It sets the Jira REST API base URI and authorization key.
     * It sends a POST request to create a new bug with the provided issue details.
     * It extracts the Jira issue key from the response and uses it to add an attachment to the bug.
     * It verifies the response status code for both requests.
     */
    private static void createBugWithAttachment() {
        // Set Jira REST API base URI and authorization key
        final String baseUri = "https://konias.atlassian.net";
        final String jiraAuthKey = System.getenv("jira_api_token");
        
        // Set file paths for issue details and attachment
        final String createIssueJsonPath = "F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\createIssue.json";
        String attachmentPath = "F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\attachment.jpg";

        RestAssured.baseURI = baseUri;

        // Create a new bug and extract the Jira issue key from the response
        String createResponse = createIssue(jiraAuthKey, createIssueJsonPath);
        String jiraIssueKey = getJiraIssueKey(createResponse);

        // Add an attachment to the bug and verify the response status code
        addAttachment(jiraAuthKey, jiraIssueKey, attachmentPath);
    }

    /**
     * This method sends a POST request to the Jira REST API to create a new bug with the provided issue details.
     * It verifies the response status code.
     * 
     * @param jiraAuthKey Jira authorization key
     * @param createIssueJsonPath path to the file containing issue details in JSON format
     * @return response from the Jira REST API
     */
    private static String createIssue(String jiraAuthKey, String createIssueJsonPath) {
        // Read issue details from file
        String payloadString = JsonHelper.getJsonFromFile(createIssueJsonPath);

        // Send POST request to create a new bug
        return given()
            .header("Authorization", jiraAuthKey)
            .header("Content-Type", "application/json")
            .body(payloadString)
            .log().all()
        .when()
            .post("/rest/api/3/issue")
        .then()
            .assertThat()
            .statusCode(201)
            .extract().response().asString();
    }

    /**
     * This method extracts the Jira issue key from the response.
     * 
     * @param createResponse response from the Jira REST API
     * @return Jira issue key
     */
    private static String getJiraIssueKey(String createResponse) {
        // Parse the response JSON
        JsonPath createResponseObject = JsonHelper.stringToJson(createResponse);
        
        // Extract the Jira issue key from the response
        return createResponseObject.get("key");
    }

    /**
     * This method sends a POST request to add an attachment to a bug.
     * It verifies the response status code.
     * 
     * @param jiraAuthKey Jira authorization key
     * @param jiraIssueKey Jira issue key
     * @param attachmentPath path to the file to be attached
     */
    private static void addAttachment(String jiraAuthKey, String jiraIssueKey, String attachmentPath) {
        // Send POST request to add an attachment to a bug
        given()
            .pathParam("jiraIssueKey", jiraIssueKey)
            .header("Authorization", jiraAuthKey)
            .header("X-Atlassian-Token", "no-check")
            .multiPart("file", new File(attachmentPath))
            .log().all()
        .when()
            .post("/rest/api/3/issue/{jiraIssueKey}/attachments")
        .then()
            .assertThat()
            .statusCode(200)
            .log().all();
    }


}

