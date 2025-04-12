package campus.auth;

import campus.base.UserCredentials;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Positive login scenarios using valid credentials and POJO
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

                .when()
                .post("https://test.mersys.io/auth/login")

                .then()
                .statusCode(200);
    }
}
