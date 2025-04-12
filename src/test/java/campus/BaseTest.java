package campus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.*;

public class BaseTest {

    protected RequestSpecification requestSpecification;

    @BeforeClass
    public void login() {
        baseURI = "https://test.mersys.io";

        Login userCredential = new Login();
        userCredential.setUsername("Campus25");
        userCredential.setPassword("Campus.2524");
        userCredential.setRememberMe("true");

        Cookies cookies = given()
                .contentType(ContentType.JSON)
                .body(userCredential)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();

        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }
}
