package campus.school;

import campus.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DocumentTypesTests extends BaseTest {

    String documentTypesId;

    @Test
    public void addDocument() {
        documentTypesId =
                given()
                        .spec(requestSpecification)
                        .body("{\n" +
                                "    \"id\": null,\n" +
                                "    \"name\": \"graduate2\",\n" +
                                "    \"description\": \"\",\n" +
                                "    \"attachmentStages\": [\"STUDENT_REGISTRATION\"],\n" +
                                "    \"schoolId\": \"6390f3207a3bcb6a7ac977f9\",\n" +
                                "    \"active\": true,\n" +
                                "    \"required\": true,\n" +
                                "    \"translateName\": [],\n" +
                                "    \"useCamera\": false\n" +
                                "}")
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
        given()
                .spec(requestSpecification)
                .body("{\n" +
                        "  \"id\": \"" + documentTypesId + "\",\n" +
                        "  \"name\": \"entrance examination\",\n" +
                        "  \"description\": \"\",\n" +
                        "  \"attachmentStages\": [\"EMPLOYMENT\"],\n" +
                        "  \"active\": true,\n" +
                        "  \"required\": true,\n" +
                        "  \"useCamera\": false,\n" +
                        "  \"translateName\": [],\n" +
                        "  \"schoolId\": \"6390f3207a3bcb6a7ac977f9\"\n" +
                        "}")
                .when()
                .put("/school-service/api/attachments")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateDocument")
    public void deleteDocument() {
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
        given()
                .spec(requestSpecification)
                .log().uri()
                .when()
                .delete("/school-service/api/attachments/" + documentTypesId)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Attachment Type not found"));
    }
}
