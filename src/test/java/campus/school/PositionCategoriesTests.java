package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PositionCategoriesTests extends BaseTest {

    Faker faker = new Faker();
    String positionCategoriesID;
    String positionCategoriesName;
    Map<String, String> positionCategories;

    @Test
    public void createPositionCategories() {
        positionCategories = new HashMap<>();
        positionCategoriesName = "Scrum Master - " + faker.number().digits(5);
        positionCategories.put("name", positionCategoriesName);

        positionCategoriesID =
                given()
                        .spec(requestSpecification)
                        .body(positionCategories)
                        .log().body()
                        .when()
                        .post("/school-service/api/position-category")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("positionCategoriesID = " + positionCategoriesID);
    }

    @Test(dependsOnMethods = "createPositionCategories")
    public void createPositionCategoriesNegative() {
        given()
                .spec(requestSpecification)
                .body(positionCategories)
                .log().body()
                .when()
                .post("/school-service/api/position-category")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createPositionCategories")
    public void updatePositionCategories() {
        positionCategoriesName = "ProductOwner - " + faker.number().digits(5);
        positionCategories.put("id", positionCategoriesID);
        positionCategories.put("name", positionCategoriesName);

        given()
                .spec(requestSpecification)
                .body(positionCategories)
                .when()
                .put("/school-service/api/position-category")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionCategoriesName));
    }

    @Test(dependsOnMethods = "updatePositionCategories")
    public void deletePositionCategories() {
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/position-category/" + positionCategoriesID)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePositionCategories")
    public void deletePositionCategoriesNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("positionCategoriesID", positionCategoriesID)
                .log().uri()
                .when()
                .delete("/school-service/api/position-category/{positionCategoriesID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("PositionCategory not  found"));
    }
}
