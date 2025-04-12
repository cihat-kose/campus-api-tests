package campus.auth;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Positive login test using raw JSON in Java text blocks.
 * Uses Java 15+ Text Blocks for cleaner multiline JSON definition.
 */
public class LoginWithRawJsonTest {

    @Test
    public void loginWithTextBlockJson() {
        // Raw JSON login credentials
        String json = """
            {
              "username": "Campus25",
              "password": "Campus.2524",
              "rememberMe": true
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .log().body() // Optional: logs request for debugging
                .when()
                .post("https://test.mersys.io/auth/login")
                .then()
                .log().body() // Optional: logs response
                .statusCode(200);
    }

    /*
     * 🔁 Alternative for Java 8-14 (without Text Blocks):
     *
     * String json = "{\\n" +
     *               "  \\\"username\\\": \\\"Campus25\\\",\\n" +
     *               "  \\\"password\\\": \\\"Campus.2524\\\",\\n" +
     *               "  \\\"rememberMe\\\": true\\n" +
     *               "}";
     *
     * given()
     *     .contentType(ContentType.JSON)
     *     .body(json)
     * .when()
     *     .post("https://test.mersys.io/auth/login")
     * .then()
     *     .statusCode(200);
     */
}
