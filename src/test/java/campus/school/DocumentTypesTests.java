package campus.school;

import campus.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DocumentTypesTests extends BaseTest {

    String documentTypesId;
    String schoolId = "6390f3207a3bcb6a7ac977f9";  // Repeated value as a variable for better readability

    @Test
    public void addDocument() {
        String requestBody = """
                {
                  "id": null,
                  "name": "graduate2",
                  "description": "",
                  "attachmentStages": ["STUDENT_REGISTRATION"],
                  "schoolId": "%s",
                  "active": true,
                  "required": true,
                  "translateName": [],
                  "useCamera": false
                }
                """.formatted(schoolId); // The .formatted method allows inserting variables in the string

        // TODO: Consider externalizing the payload if test coverage expands
        documentTypesId = given()
                .spec(requestSpecification)
                .body(requestBody)
                .log().body()
                .when()
                .post("/school-service/api/attachments/create")
                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");

        System.out.println("documentTypesId = " + documentTypesId);
    }

    @Test(dependsOnMethods = "addDocument")
    public void updateDocument() {
        String requestBody = """
                {
                  "id": "%s",
                  "name": "entrance examination",
                  "description": "",
                  "attachmentStages": ["EMPLOYMENT"],
                  "active": true,
                  "required": true,
                  "useCamera": false,
                  "translateName": [],
                  "schoolId": "%s"
                }
                """.formatted(documentTypesId, schoolId);

        // TODO: Add assertion for the updated name to confirm update
        given()
                .spec(requestSpecification)
                .body(requestBody)
                .when()
                .put("/school-service/api/attachments")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateDocument")
    public void deleteDocument() {
        // TODO: Consider asserting deletion with GET if endpoint supports it
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/attachments/" + documentTypesId)
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDocument")
    public void deleteDocumentNegative() {
        // TODO: If backend changes status code to 404, this test should be updated accordingly
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/attachments/" + documentTypesId)
                .then()
                .log().body()
                .statusCode(500) // Since the API returns a 500, you should check for 500
                .body("detail", equalTo("Attachment Type not found")); // Check for the 'detail' field instead of 'message'
    }
}
