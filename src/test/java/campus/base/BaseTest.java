package campus.base;

import campus.utils.UserCredentials;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void login() {
        // Set the base URI globally for RestAssured
        baseURI = "https://test.mersys.io"; // Correct place for baseURI

        // Prepare login data as POJO
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername("Campus25");
        userCredentials.setPassword("Campus.2524");
        userCredentials.setRememberMe("true");  // Correct String usage here

        // Get the JWT token from login response
        String token = given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("access_token"); // Extract the token from the response

        // Print token for debugging (Optional)
        // System.out.println("Login token: " + token);

        // Build request specification with Bearer token
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
