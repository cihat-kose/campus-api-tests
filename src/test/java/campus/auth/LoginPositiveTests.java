package campus.auth;

import campus.utils.UserCredentials;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Positive login test using valid credentials.
 * Verifies that login is successful with HTTP 200 OK response.
 */
public class LoginPositiveTests {

    @Test
    public void loginWithValidCredentials() {
        UserCredentials creds = new UserCredentials();
        creds.setUsername("Campus25");
        creds.setPassword("Campus.2524");
        creds.setRememberMe("true");

        given()
                .contentType(ContentType.JSON)
                .body(creds)
                .log().body() // Optional: useful to debug request payload
                .when()
                .post("https://test.mersys.io/auth/login")
                .then()
                .log().body() // Optional: helpful to inspect response
                .statusCode(200);
    }
}
