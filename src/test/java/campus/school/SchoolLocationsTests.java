package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class SchoolLocationsTests extends BaseTest {

    Faker faker = new Faker();
    String schoolLocationID;
    String schoolLocationName;
    String schoolLocationShortName;
    String schoolLocationCapacity;

    Map<String, String> schoolLocation;

    @Test
    public void createSchoolLocation() {
        schoolLocation = new HashMap<>();

        schoolLocationName = faker.name().firstName() + faker.number().digits(5);
        schoolLocationShortName = faker.name().lastName() + faker.number().digits(3);
        schoolLocationCapacity = faker.number().digits(5);

        schoolLocation.put("name", schoolLocationName);
        schoolLocation.put("shortName", schoolLocationShortName);
        schoolLocation.put("capacity", schoolLocationCapacity);
        schoolLocation.put("type", "LABORATORY");
        schoolLocation.put("school", "6390f3207a3bcb6a7ac977f9");

        schoolLocationID =
                given()
                        .spec(requestSpecification)
                        .body(schoolLocation)
                        .log().body()
                        .when()
                        .post("/school-service/api/location")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("LocationID = " + schoolLocationID);
    }

    @Test(dependsOnMethods = "createSchoolLocation")
    public void createSchoolLocationNegative() {
        given()
                .spec(requestSpecification)
                .body(schoolLocation)
                .log().body()
                .when()
                .post("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createSchoolLocationNegative")
    public void updateSchoolLocation() {
        schoolLocation.put("id", schoolLocationID);
        schoolLocationName = "TechnoStudy" + faker.number().digits(5);
        schoolLocation.put("name", schoolLocationName);
        schoolLocation.put("shortName", schoolLocationShortName);

        given()
                .spec(requestSpecification)
                .body(schoolLocation)
                .when()
                .put("/school-service/api/location")
                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(schoolLocationID));
    }

    @Test(dependsOnMethods = "updateSchoolLocation")
    public void deleteSchoolLocation() {
        given()
                .spec(requestSpecification)
                .pathParam("SchoolLocationID", schoolLocationID)
                .log().uri()
                .when()
                .delete("/school-service/api/location/{SchoolLocationID}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteSchoolLocation")
    public void deleteSchoolLocationNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("SchoolLocationID", schoolLocationID)
                .log().uri()
                .when()
                .delete("/school-service/api/location/{SchoolLocationID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("School Location not found"));
    }
}
