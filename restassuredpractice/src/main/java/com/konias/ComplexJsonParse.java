package com.konias;
import org.testng.Assert;

import com.konias.files.JsonHelper;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

    public static void main(String[] args) {
        // 1. Print No of courses returned by API

        String mockedResponse = JsonHelper.getJsonFromFile("F:\\git\\restassured\\restassuredpractice\\src\\main\\resources\\sectionSixExercise.json");

        JsonPath js = new JsonPath(mockedResponse);

        int count = js.getInt("courses.size()");
        
        System.out.println(count);

        // 2. Print Purchase Amount

        int expectedPurchaseAmount = js.getInt("dashboard.purchaseAmount");

        System.out.println(expectedPurchaseAmount);

        // 3. Print Title of the first course

        String firstCourseTitle = js.get("courses[0].title");

        System.out.println(firstCourseTitle);

        // 4. Print All course titles and their respective Prices

        for(int i=0; i<count; i++){
            String courseTitles = js.get("courses["+i+"].title");
            String coursePrices = js.get("courses["+i+"].price").toString();
            System.out.println(courseTitles + ": $" + coursePrices);
        }
        
        // 5. Print no of copies sold by RPA Course

        for(int i=0; i<count; i++){
            String courseTitles = js.get("courses["+i+"].title");

            if(courseTitles.equalsIgnoreCase("RPA")){
                int copies = js.get("courses["+i+"].copies");
                System.out.println("RPA has " + copies + " copies.");
                break;
            }

        }

        // 6. Verify if Sum of all Course prices matches with Purchase Amount

        int actualPurchaseAmount = 0;

        for(int i=0; i<count; i++){
            int coursePrice = js.get("courses["+i+"].price");
            int numberOfCopies = js.get("courses["+i+"].copies");
            int grossCourseAmount = coursePrice * numberOfCopies;

            actualPurchaseAmount += grossCourseAmount;
        }

        Assert.assertEquals(actualPurchaseAmount, expectedPurchaseAmount);
    }   

}
