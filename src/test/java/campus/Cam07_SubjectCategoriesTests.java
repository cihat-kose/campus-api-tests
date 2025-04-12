package campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Cam07_SubjectCategoriesTests extends BaseTest {

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
        subject.put("name", subjectName);
        subject.put("code", subjectCode);

        given()
                .spec(requestSpecification)
                .body(subject)
                .log().body()
                .when()
                .post("/school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
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
                .statusCode(200);
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
                .statusCode(400)
                .body("message", equalTo("SubjectCategory not  found"));
    }
}
