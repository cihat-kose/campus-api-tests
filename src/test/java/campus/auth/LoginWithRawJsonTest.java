package campus.auth;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Login using raw JSON string.
 * This class uses Java 15+ Text Blocks (""") for cleaner syntax.
 * A classic Java 8+ version is also provided in comments.
 */
public class LoginWithRawJsonTest {

    @Test
    public void loginWithTextBlockJson() {
        // ✅ Java 15+ Text Block (clean multiline string)
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

                .when()
                .post("https://test.mersys.io/auth/login")

                .then()
                .statusCode(200);
    }

    /*
     * 🔁 Java 8-14 Classic Multiline String Version:
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
