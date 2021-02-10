package Day10_reusableSpecification_JsonSchema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import testbase.SpartanAdmin_TestBase;

public class JsonSchemaValidation extends SpartanAdmin_TestBase {

/*
   we test status code, body, header, response time, the structure of our json against given json schema
   if we have very large response payload, and we care about only few fields, in order to verify rest of them, without really checking the actual value
   we just check the structure of the json(json response) against given json schema
 //json schema: the requirement for how the json object look like, the structure of the json
   1.added json-schema-validator dependencies
   2.added the schema file
   3.wrote the line in body , provide the path of json schema
   i have the schema i have to validate that i got from my developer, and i used that to automate the validation of the schema
   , the structure of my json,
 */
    @DisplayName("Testing the structure of GET /api/spartans/{id}  response")
    @Test
    public void testSingleSpartanSchema(){

        given()
            .spec(adminReqSpec)
                .pathParam("id",4)
                .when()
                .get("/spartans/{id}")
                .then()
                .spec(adminResSpec)
                .body(matchesJsonSchemaInClasspath("singleSpartanSchema.json"))
        ;
    }
}
