package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class GradeLevelsTests extends BaseTest {

    Faker faker = new Faker();
    String gradeLevelID;
    String gradeLevelName;
    String gradeLevelShortName;
    Map<String, String> gradeLevel;

    @Test
    public void createGradeLevel() {
        gradeLevel = new HashMap<>();
        gradeLevelName = faker.name().firstName() + faker.number().digits(5);
        gradeLevelShortName = faker.name().lastName() + faker.number().digits(5);

        gradeLevel.put("name", gradeLevelName);
        gradeLevel.put("shortName", gradeLevelShortName);

        gradeLevelID =
                given()
                        .spec(requestSpecification)
                        .body(gradeLevel)
                        .log().body()
                        .when()
                        .post("/school-service/api/grade-levels")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("gradeLevelID = " + gradeLevelID);
    }

    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {
        gradeLevel.put("name", gradeLevelName);
        gradeLevel.put("shortName", gradeLevelShortName);

        given()
                .spec(requestSpecification)
                .body(gradeLevel)
                .log().body()
                .when()
                .post("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {
        gradeLevelName = "TechnoStudy" + faker.number().digits(5);
        gradeLevel.put("id", gradeLevelID);
        gradeLevel.put("name", gradeLevelName);
        gradeLevel.put("shortName", gradeLevelShortName);

        given()
                .spec(requestSpecification)
                .body(gradeLevel)
                .when()
                .put("/school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(gradeLevelID));
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel() {
        given()
                .spec(requestSpecification)
                .pathParam("levelID", gradeLevelID)
                .log().uri()
                .when()
                .delete("/school-service/api/grade-levels/{levelID}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("levelID", gradeLevelID)
                .log().uri()
                .when()
                .delete("/school-service/api/grade-levels/{levelID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Grade Level not found."));
    }
}
