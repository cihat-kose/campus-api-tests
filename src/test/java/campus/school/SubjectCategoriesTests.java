package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SubjectCategoriesTests extends BaseTest {

    Faker faker = new Faker();
    String subjectID;
    String subjectName;
    String subjectCode;
    Map<String, String> subject;

    @Test
    public void createSubject() {
        subject = new HashMap<>();
        subjectName = faker.country().name() + faker.number().digits(5);
        subjectCode = faker.country().countryCode2() + faker.number().digits(5);

        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        subjectID =
                given()
                        .spec(requestSpecification)
                        .body(subject)
                        .log().body()
                        .when()
                        .post("/school-service/api/subject-categories")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("subjectID = " + subjectID);
    }

    @Test(dependsOnMethods = "createSubject")
    public void createSubjectNegative() {
        given()
                .spec(requestSpecification)
                .body(subject)
                .log().body()
                .when()
                .post("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(500) // TODO: If backend returns 400 instead of 500, change this to 400
                .body("detail", containsString("already")); // TODO: If backend uses "message" instead of "detail", update this
    }

    @Test(dependsOnMethods = "createSubjectNegative")
    public void updateSubject() {
        subject.put("id", subjectID);
        subjectName = "TechnoStudy" + faker.number().digits(5);
        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        given()
                .spec(requestSpecification)
                .body(subject)
                .when()
                .put("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(subjectName));
    }

    @Test(dependsOnMethods = "updateSubject")
    public void deleteSubject() {
        given()
                .spec(requestSpecification)
                .pathParam("subjectID", subjectID)
                .log().uri()
                .when()
                .delete("/school-service/api/subject-categories/{subjectID}")
                .then()
                .log().body()
                .statusCode(200); // TODO: If backend returns 204 (No Content), change this to 204
    }

    @Test(dependsOnMethods = "deleteSubject")
    public void deleteSubjectNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("subjectID", subjectID)
                .log().uri()
                .when()
                .delete("/school-service/api/subject-categories/{subjectID}")
                .then()
                .log().body()
                .statusCode(500) // TODO: Adjust if backend returns a different code like 400
                .body("detail", equalTo("SubjectCategory not  found")); // TODO: Check if error message key is "message" or "detail"
    }
}
