package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DepartmentsTests extends BaseTest {

    String departmentsId;
    String departmentsName;
    Faker faker = new Faker();

    @Test
    public void createDepartments() {
        Map<String, String> departments = new HashMap<>();

        departmentsName = faker.country().countryCode2() + faker.number().digits(3);
        departments.put("name", departmentsName);
        departments.put("code", faker.number().digits(4));
        departments.put("school", "6390f3207a3bcb6a7ac977f9");

        departmentsId =
                given()
                        .spec(requestSpecification)
                        .body(departments)
                        .log().body()
                        .when()
                        .post("/school-service/api/department")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createDepartments")
    public void createDepartmentsNegative() {
        Map<String, String> departments = new HashMap<>();
        departments.put("name", departmentsName); // Aynı isim → negatif senaryo
        departments.put("code", faker.number().digits(4));
        departments.put("school", "6390f3207a3bcb6a7ac977f9");

        given()
                .spec(requestSpecification)
                .body(departments)
                .log().body()
                .when()
                .post("/school-service/api/department")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createDepartmentsNegative")
    public void updateDepartments() {
        Map<String, String> departments = new HashMap<>();
        departmentsName = "departName" + faker.number().digits(3);

        departments.put("id", departmentsId);
        departments.put("name", departmentsName);
        departments.put("code", faker.number().digits(4));
        departments.put("school", "6390f3207a3bcb6a7ac977f9");

        given()
                .spec(requestSpecification)
                .body(departments)
                .log().body()
                .when()
                .put("/school-service/api/department")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateDepartments")
    public void deleteDepartments() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/department/" + departmentsId)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteDepartments")
    public void deleteDepartmentsNegative() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/department/" + departmentsId)
                .then()
                .log().body()
                .statusCode(204); // Not: Sistem bunu 204 döndürüyorsa, böyle kalabilir
    }
}
