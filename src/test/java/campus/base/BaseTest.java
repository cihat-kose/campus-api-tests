package campus.base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies; // Dikkat! Doğru paket: http
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.*;

/**
 * All test classes can inherit this class and use the login process ready-made.
 * Login information is sent via UserCredentials (POJO).
 */
public class BaseTest {

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void login() {
        baseURI = "https://test.mersys.io";

        // POJO object that carries user information
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername("Campus25");
        userCredentials.setPassword("Campus.2524");
        userCredentials.setRememberMe("true");

        Cookies cookies = given()
                .contentType(ContentType.JSON)
                .body(userCredentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();

        // A specification is created with the cookies received after logging in
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }
}
