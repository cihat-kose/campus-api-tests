package campus.auth;

import campus.base.UserCredentials;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Negative login test with invalid password
 */
public class LoginNegativeTests {

    @Test
    public void loginWithInvalidPassword() {
        UserCredentials creds = new UserCredentials();
        creds.setUsername("Campus25");
        creds.setPassword("WrongPassword123");
        creds.setRememberMe("true");

        given()
                .contentType(ContentType.JSON)
                .body(creds)

                .when()
                .post("https://test.mersys.io/auth/login")

                .then()
                .statusCode(401)
                .body("detail", equalTo("Invalid username or password")); // based on real response
    }
}
