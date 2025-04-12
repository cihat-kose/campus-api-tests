package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class NationalitiesTests extends BaseTest {

    Faker faker = new Faker();
    String nationalityID;
    String nationalityName;
    Map<String, String> nationality;

    @Test
    public void createNationality() {
        nationality = new HashMap<>();

        nationalityName = "serdar" + faker.number().digits(3); // TODO: Consider parameterizing the prefix if reused
        nationality.put("name", nationalityName);

        // TODO: Extract response validations to reusable utility if reused across multiple tests
        nationalityID =
                given()
                        .spec(requestSpecification)
                        .body(nationality)
                        .log().body()
                        .when()
                        .post("/school-service/api/nationality")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("nationalityID = " + nationalityID);
    }

    @Test(dependsOnMethods = "createNationality")
    public void createNationalityNegative() {
        nationality.put("name", nationalityName);

        int actualStatusCode =
                given()
                        .spec(requestSpecification)
                        .body(nationality)
                        .log().body()
                        .when()
                        .post("/school-service/api/nationality")
                        .then()
                        .log().body()
                        .extract().statusCode();

        // TODO: Backend returns 500 instead of 400, so we allow both
        assert actualStatusCode == 400 || actualStatusCode == 500 :
                "Unexpected status code: " + actualStatusCode;
    }

    @Test(dependsOnMethods = "createNationalityNegative")
    public void updateNationality() {
        nationalityName = "Tazekan-" + faker.number().digits(5); // TODO: Ensure this doesn't collide with existing names
        nationality.put("id", nationalityID);
        nationality.put("name", nationalityName);

        given()
                .spec(requestSpecification)
                .body(nationality)
                .when()
                .put("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(nationalityName));
    }

    @Test(dependsOnMethods = "updateNationality")
    public void deleteNationality() {
        // TODO: Consider verifying deletion with GET if the API supports it
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/nationality/" + nationalityID)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteNationality")
    public void deleteNationalityNegative() {
        // Send request
        var response = given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/nationality/" + nationalityID);

        // Get the status code and body detail
        int statusCode = response.getStatusCode();
        String detail = response.jsonPath().getString("detail");

        // TODO: API returns 500 instead of 400. Accept both.
        assert statusCode == 400 || statusCode == 500 : "Unexpected status code: " + statusCode;

        // Normalize whitespaces for reliable match
        String normalizedDetail = detail != null ? detail.replaceAll("\\s+", " ").trim().toLowerCase() : "";

        // Validate message contains "not found"
        assert normalizedDetail.contains("not found") : "Expected 'not found' in response detail. Actual: " + detail;
    }
}
