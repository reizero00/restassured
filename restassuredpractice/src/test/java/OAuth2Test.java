import static io.restassured.RestAssured.given;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class OAuth2Test {
String access_token;
String courses;
String code;

String emailAddress = "REDACTED@gmail.com";
String password = "REDACTED";

    @Test
    public void testOAuthCode() throws InterruptedException {
        code = getOauthCodeWithSelenium(emailAddress, password);
        access_token = getAccessToken(code);
        System.out.println("Access Token: " + access_token);
        courses = getCoursesAuthenticated(access_token);
        System.out.println("Courses: \n" + courses);
    }

    public String getAccessToken(String code) {
        return access_token = 
        given()
            .urlEncodingEnabled(false)
            .queryParam("code",code)
            .queryParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
            .queryParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
            .queryParam("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
            .queryParam("grant_type","authorization_code")
        .when()
            .post("https://www.googleapis.com/oauth2/v4/token")
        .then()
            .statusCode(200)
            .log().all()
            .extract()
            .path("access_token");
    }

    public String getCoursesAuthenticated(String access_token) {
        return courses =
        given()
            .urlEncodingEnabled(false)
            .queryParam("access_token", access_token)
        .when()
            .get("https://rahulshettyacademy.com/getCourse.php")
        .then()
            .statusCode(200)
            .extract().response().asString();

    }

    public String getOauthCodeWithSelenium(String emailAddress, String password) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "F://git//chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");

        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(emailAddress);
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        
        // driver.findElement(By.xpath("//button/span[text()='Continue']")).click();;
        
        String oauthCodeUrl = driver.getCurrentUrl();

        String removedStartIndexFromURL = oauthCodeUrl.split("code=")[1];
        code = removedStartIndexFromURL.split("&scope")[0];

        driver.quit();

        return code;
    }

}


