import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import java.io.File;
import java.util.Arrays;

import com.konias.files.JsonHelper;
import com.konias.pojo.EcommerceApis.CreateOrderRequest;
import com.konias.pojo.EcommerceApis.CreateProductResponse;
import com.konias.pojo.EcommerceApis.LoginRequest;
import com.konias.pojo.EcommerceApis.LoginResponse;
import com.konias.pojo.EcommerceApis.Orders;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;


public class ECommerceExerciseTest {

    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        LoginRequest loginRequest = createLoginRequestBody("kristofferonias@gmail.com", "Testtesttest123");

        LoginResponse loginResponse = getLoginDetails(loginRequest);

        String userId = loginResponse.getUserId();

        String basePath = System.getProperty("user.dir");
        String attachmentPath = basePath + "/restassuredpractice/src/main/resources/attachment.jpg";

        CreateProductResponse createProductResponse = createProduct(loginResponse.getToken(),"Cool Product Name", userId, "fashion", "shirts", "100", "Addidas Original", "women", attachmentPath);

        String productId = createProductResponse.getProductId();

        CreateOrderRequest createOrderRequestBody = createOrderRequestBody("United States",productId);
        String createOrderResponse = createOrder(loginResponse.getToken(), createOrderRequestBody);

        String orderId = getOrdersIdFromCreateOrder(createOrderResponse);

        String orderDetailsResponse = getOrderDetails(loginResponse.getToken(), orderId);

        System.out.println(orderDetailsResponse);

        deleteProduct(loginResponse.getToken(), productId);

        deleteOrder(loginResponse.getToken(), orderId);

    }

    private static RequestSpecification requestHeader(String authenticationToken) {
 
        return new RequestSpecBuilder()
            .addHeader("Authorization", authenticationToken)
            .build();

    }

    private static LoginResponse getLoginDetails(LoginRequest loginRequest) {

        return given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
            .when()
                .post("/api/ecom/auth/login")
            .then()
                .statusCode(200)
                .extract().as(LoginResponse.class);
            
    }

    private static LoginRequest createLoginRequestBody(String userEmail, String userPassword) {
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail(userEmail);
        loginRequest.setUserPassword(userPassword);

        return loginRequest;
    }

    private static CreateProductResponse createProduct(String authenticationToken, String productName, String userId, String productCategory, String productSubCategory, String productPrice, String productDescription, String productFor, String productImage) {

        return given()
                .spec(requestHeader(authenticationToken))
                .formParam("productName", productName)
                .formParam("productAddedBy", userId)
                .formParam("productCategory", productCategory)
                .formParam("productSubCategory", productSubCategory)
                .formParam("productPrice", productPrice)
                .formParam("productDescription", productDescription)
                .formParam("productFor", productFor)
                .multiPart("productImage", new File(productImage))
            .when()
                .post("/api/ecom/product/add-product")
            .then()
                .statusCode(201)
                .extract().as(CreateProductResponse.class);
    }

    private static CreateOrderRequest createOrderRequestBody(String country, String productOrderedId){
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        Orders orders = new Orders();

        orders.setCountry(country);
        orders.setProductOrderedId(productOrderedId);

        createOrderRequest.setOrders(Arrays.asList(orders));

        return createOrderRequest;     
    }

    private static String createOrder(String authenticationToken, CreateOrderRequest createOrderRequest) {

        return given()
                .spec(requestHeader(authenticationToken))
                .header("Content-Type", "application/json")
                .body(createOrderRequest)
            .when()
                .post("/api/ecom/order/create-order")
            .then()
                .statusCode(201)
                .extract().asString();
    }

    private static String getOrdersIdFromCreateOrder(String requestPayload) {
        JsonPath requestJson = JsonHelper.stringToJson(requestPayload);

        return requestJson.get("orders[0]");
    }

    private static String getOrderDetails(String authenticationToken, String orderId) {

        return given()
            .spec(requestHeader(authenticationToken))
            .header("Content-Type", "application/json")
            .queryParams("id", orderId)
        .when()
            .get("/api/ecom/order/get-orders-details")
        .then()
            .statusCode(200)
            .extract().asString();
    }

    private static void deleteProduct(String authenticationToken, String productId) {
        given()
            .spec(requestHeader(authenticationToken))
            .pathParam("productId", productId)
        .when()
            .delete("/api/ecom/product/delete-product/{productId}")
        .then()
            .statusCode(200)
            .body("message", containsString("Product Deleted Successfully"));

    }
    
    private static void deleteOrder(String authenticationToken,String orderId) {
        given()
            .spec(requestHeader(authenticationToken))
            .pathParam("orderId", orderId)
        .when()
            .delete("/api/ecom/order/delete-order/{orderId}")
        .then()
            .statusCode(200)
            .body("message", containsString("Orders Deleted Successfully"));
    }
}

