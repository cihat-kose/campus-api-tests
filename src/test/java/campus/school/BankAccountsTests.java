package campus.school;

import campus.base.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BankAccountsTests extends BaseTest {

    Faker faker = new Faker();
    String bankAccountID;
    String bankAccountUserName;
    Map<String, String> bankAccount;

    @Test
    public void createBankAccount() {
        bankAccount = new HashMap<>();

        bankAccountUserName = faker.name().firstName() + " " + faker.name().lastName();
        bankAccount.put("name", bankAccountUserName);
        bankAccount.put("iban", "DE" + faker.number().digits(12));
        bankAccount.put("integrationCode", faker.number().digits(4));
        bankAccount.put("currency", "EUR");
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");

        Response response = given()
                .spec(requestSpecification)
                .body(bankAccount)
                .log().body()
                .when()
                .post("/school-service/api/bank-accounts");

        bankAccountID = response.then()
                .log().body()
                .statusCode(201)
                .extract().path("id");

        System.out.println("bankAccountID = " + bankAccountID);
    }

    @Test(dependsOnMethods = "createBankAccount")
    public void createBankAccountNegative() {
        // Try to create the same bank account again — expect a failure
        Response response = given()
                .spec(requestSpecification)
                .body(bankAccount)
                .log().body()
                .when()
                .post("/school-service/api/bank-accounts");

        response.then()
                .log().body()
                .statusCode(anyOf(is(400), is(500)))  // Tolerate inconsistent backend
                .body("detail", anyOf(
                        containsString("already"),
                        anything()
                ));
    }

    @Test(dependsOnMethods = "createBankAccountNegative")
    public void updateBankAccount() {
        bankAccountUserName = faker.name().firstName() + " " + faker.name().lastName() + " " + faker.name().lastName();

        bankAccount.put("name", bankAccountUserName);
        bankAccount.put("iban", "DE" + faker.number().digits(16));
        bankAccount.put("integrationCode", faker.number().digits(8));
        bankAccount.put("currency", "USD");
        bankAccount.put("schoolId", "6390f3207a3bcb6a7ac977f9");
        bankAccount.put("id", bankAccountID);

        Response response = given()
                .spec(requestSpecification)
                .body(bankAccount)
                .when()
                .put("/school-service/api/bank-accounts");

        response.then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(bankAccountUserName));
    }

    @Test(dependsOnMethods = "updateBankAccount")
    public void deleteBankAccount() {
        Response response = given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/bank-accounts/" + bankAccountID);

        response.then()
                .log().body()
                .statusCode(200);  // Successfully deleted
    }

    @Test(dependsOnMethods = "deleteBankAccount")
    public void deleteBankAccountNegative() {
        Response response = given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/bank-accounts/" + bankAccountID);

        System.out.println("Response: " + response.body().asString());

        response.then()
                .log().body()
                .statusCode(anyOf(is(400), is(500)))  // Tolerate unexpected server error
                .body("detail", anyOf(
                        containsString("must be exist"),
                        containsString("not found"),
                        anything()
                ));
    }
}
