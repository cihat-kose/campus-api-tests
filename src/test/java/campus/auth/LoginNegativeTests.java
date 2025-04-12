package campus.auth;

import campus.utils.UserCredentials;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * This test validates that logging in with an incorrect password
 * returns the appropriate error response (HTTP 401 Unauthorized).
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
                .log().body() // Optional: logs the request body
                .when()
                .post("https://test.mersys.io/auth/login")
                .then()
                .log().body() // Optional: logs the response body
                .statusCode(401)
                .body("detail", equalTo("Invalid username or password"));
    }
}
