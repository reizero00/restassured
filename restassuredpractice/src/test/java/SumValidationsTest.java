
import org.testng.Assert;
import org.testng.annotations.Test;

import com.konias.files.JsonHelper;

import io.restassured.path.json.JsonPath;

public class SumValidationsTest {

    @Test
    public void sumOfCoursesTest () {
        String mockedResponse = JsonHelper.getJsonFromFile("F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\sectionSixExercise.json");
        JsonPath js = new JsonPath(mockedResponse);
        int expectedPurchaseAmount = js.getInt("dashboard.purchaseAmount");
        int actualPurchaseAmount = calculateActualPurchaseAmount(js);

        Assert.assertEquals(actualPurchaseAmount, expectedPurchaseAmount);
    }

    private int calculateActualPurchaseAmount(JsonPath js) {
        int count = js.getInt("courses.size()");
        int actualPurchaseAmount = 0;

        for(int i=0; i<count; i++){
            int coursePrice = js.get("courses["+i+"].price");
            int numberOfCopies = js.get("courses["+i+"].copies");
            int grossCourseAmount = coursePrice * numberOfCopies;

            actualPurchaseAmount += grossCourseAmount;
        }

        return actualPurchaseAmount;
    }
}

