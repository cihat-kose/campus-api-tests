package campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class Cam12_DiscountsTests extends BaseTest {

    Faker faker = new Faker();
    String discountID;
    String discountDescription;
    String discountCode;
    Map<String, String> discount;

    @Test
    public void createDiscounts() {
        discount = new HashMap<>();

        discountDescription = faker.nation().nationality() + faker.number().digits(5);
        discountCode = faker.code().asin() + faker.number().digits(5);

        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        discountID =
                given()
                        .spec(requestSpecification)
                        .body(discount)
                        .log().body()
                        .when()
                        .post("/school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createDiscounts")
    public void createDiscountsNegative() {
        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        given()
                .spec(requestSpecification)
                .body(discount)
                .log().body()
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createDiscountsNegative")
    public void updateDiscounts() {
        discountDescription = "TechnoStudy-" + faker.number().digits(5);

        discount.put("id", discountID);
        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        given()
                .spec(requestSpecification)
                .body(discount)
                .when()
                .put("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200)
                .body("description", equalTo(discountDescription));
    }

    @Test(dependsOnMethods = "updateDiscounts")
    public void deleteDiscounts() {
        given()
                .spec(requestSpecification)
                .pathParam("discountID", discountID)
                .log().uri()
                .when()
                .delete("/school-service/api/discounts/{discountID}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDiscounts")
    public void deleteDiscountsNegative() {
        given()
                .spec(requestSpecification)
                .pathParam("discountID", discountID)
                .log().uri()
                .when()
                .delete("/school-service/api/discounts/{discountID}")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Discount not found"));
    }
}
