package campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Cam05_FieldsTests extends BaseTest {

    String fieldName;
    String fieldID;
    String fieldCode;
    String newfieldName;
    String newfieldCode;
    Faker faker = new Faker();
    Map<String, String> fields = new HashMap<>();

    @Test
    public void createFields() {
        fieldName = "field-" + faker.number().digits(3);
        fieldCode = faker.number().digits(5);

        fields.put("name", fieldName);
        fields.put("code", fieldCode);
        fields.put("type", "STRING");
        fields.put("schoolId", "6390f3207a3bcb6a7ac977f9");

        fieldID =
                given()
                        .spec(requestSpecification)
                        .body(fields)
                        .log().body()
                        .when()
                        .post("/school-service/api/entity-field")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createFields")
    public void createFieldsNegative() {
        given()
                .spec(requestSpecification)
                .body(fields)
                .log().body()
                .when()
                .post("/school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "createFields")
    public void updateFields() {
        newfieldName = "field-" + faker.number().digits(2);
        newfieldCode = faker.number().digits(3);

        fields.put("name", newfieldName);
        fields.put("code", newfieldCode);
        fields.put("id", fieldID);

        given()
                .spec(requestSpecification)
                .body(fields)
                .log().body()
                .when()
                .put("/school-service/api/entity-field")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newfieldName));
    }

    @Test(dependsOnMethods = "updateFields")
    public void deleteFields() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/entity-field/" + fieldID)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteFields")
    public void deleteFieldsNegative() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/entity-field/" + fieldID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"));
    }
}
