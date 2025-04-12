package campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Cam13_NationalitiesTests extends BaseTest {

    Faker faker = new Faker();
    String nationalityID;
    String nationalityName;
    Map<String, String> nationality;

    @Test
    public void createNationality() {
        nationality = new HashMap<>();

        nationalityName = "serdar" + faker.number().digits(3);
        nationality.put("name", nationalityName);

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

        given()
                .spec(requestSpecification)
                .body(nationality)
                .log().body()
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createNationalityNegative")
    public void updateNationality() {
        nationalityName = "Tazekan-" + faker.number().digits(5);
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
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/nationality/" + nationalityID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Nationality not  found"));
    }
}
