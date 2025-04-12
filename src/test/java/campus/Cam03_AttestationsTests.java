package campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Cam03_AttestationsTests extends BaseTest {

    Faker faker = new Faker();
    String attestationID;
    String attestationName;
    Map<String, String> attestation;

    @Test
    public void createAttestation() {
        attestation = new HashMap<>();
        attestationName = "Degree Certificates Attestation - " + faker.number().digits(5);
        attestation.put("name", attestationName);

        attestationID =
                given()
                        .spec(requestSpecification)
                        .body(attestation)
                        .log().body()
                        .when()
                        .post("/school-service/api/attestation")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("attestationID = " + attestationID);
    }

    @Test(dependsOnMethods = "createAttestation")
    public void createAttestationNegative() {
        given()
                .spec(requestSpecification)
                .body(attestation)
                .log().body()
                .when()
                .post("/school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createAttestation")
    public void updateAttestation() {
        attestationName = "Post Graduation Certificates Attestation - " + faker.number().digits(5);
        attestation.put("id", attestationID);
        attestation.put("name", attestationName);

        given()
                .spec(requestSpecification)
                .body(attestation)
                .when()
                .put("/school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(attestationName));
    }

    @Test(dependsOnMethods = "updateAttestation")
    public void deleteAttestation() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/attestation/" + attestationID)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("attestationID", attestationID)
                .log().uri()
                .when()
                .delete("/school-service/api/attestation/{attestationID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("attestation not found"));
    }
}
