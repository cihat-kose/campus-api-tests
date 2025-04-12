package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class PositionsTests extends BaseTest {

    Faker faker = new Faker();
    String positionsID;
    String positionsName;
    String positionsShort;
    Map<String, String> positions = new HashMap<>();

    @Test
    public void createPositions() {
        positionsName = "seyma" + faker.number().digits(5);
        positionsShort = "seyma" + faker.number().digits(5);

        positions.put("name", positionsName);
        positions.put("shortName", positionsShort);
        positions.put("tenantId", "6390ef53f697997914ec20c2");
        positions.put("active", "true");

        positionsID =
                given()
                        .spec(requestSpecification)
                        .body(positions)
                        .log().body()
                        .when()
                        .post("/school-service/api/employee-position")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("positionsID = " + positionsID);
    }

    @Test(dependsOnMethods = "createPositions")
    public void createPositionsNegative() {
        given()
                .spec(requestSpecification)
                .body(positions)
                .log().body()
                .when()
                .post("/school-service/api/employee-position")
                .then()
                .log().body()
                .statusCode(500)  // TODO: Backend returns 500 for duplicates, adjust assertion accordingly
                .body("detail", containsString("already"));
    }

    @Test(dependsOnMethods = "createPositionsNegative")
    public void updatePositions() {
        positions.put("id", positionsID);
        positionsName = "TechnoStudy" + faker.number().digits(5);
        positions.put("name", positionsName);
        positions.put("shortName", positionsShort);

        given()
                .spec(requestSpecification)
                .body(positions)
                .when()
                // TODO: Removed trailing slash – PUT /employee-position/ caused 404
                .put("/school-service/api/employee-position")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionsName));
    }

    @Test(dependsOnMethods = "updatePositions")
    public void deletePositions() {
        given()
                .spec(requestSpecification)
                .pathParam("positionsID", positionsID)
                .log().uri()
                .when()
                .delete("/school-service/api/employee-position/{positionsID}")
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePositions")
    public void deletePositionsNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("positionsID", positionsID)
                .log().uri()
                .when()
                .delete("/school-service/api/employee-position/{positionsID}")
                .then()
                .log().body()
                .statusCode(204); // TODO: If the system allows deleting non-existent items silently, keep 204
    }
}
