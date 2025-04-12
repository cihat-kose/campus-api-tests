package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DiscountsTests extends BaseTest {

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

        System.out.println("discountID = " + discountID);
    }

    @Test(dependsOnMethods = "createDiscounts")
    public void createDiscountsNegative() {
        discount.put("description", discountDescription);
        discount.put("code", discountCode);

        // TODO: Change expected status code if backend starts returning 400 instead of 500
        given()
                .spec(requestSpecification)
                .body(discount)
                .log().body()
                .when()
                .post("/school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(500)  // Backend returns 500 for duplicates
                .body("detail", containsString("already"));
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
        // TODO: Validate if backend returns consistent error message and consider changing status code check
        given()
                .spec(requestSpecification)
                .pathParam("discountID", discountID)
                .log().uri()
                .when()
                .delete("/school-service/api/discounts/{discountID}")
                .then()
                .log().body()
                .statusCode(500) // Backend currently returns 500 if already deleted
                .body("detail", containsString("Discount not found"));
    }
}
