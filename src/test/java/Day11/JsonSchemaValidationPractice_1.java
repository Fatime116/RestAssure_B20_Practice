package Day11;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import pojo.Spartan;
import utility.ConfigurationReader;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testbase.SpartanAdmin_TestBase;
import utility.SpartanUtil;

import java.io.File;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class JsonSchemaValidationPractice_1 extends SpartanAdmin_TestBase {
    /*
       we test status code, body, header, response time, the structure of our json against given json schema file which is provided by developer
       if we have very large response payload, and we care about only few fields, in order to verify rest of them, without really checking the actual value
       we just check the structure of the json(json response) against given json schema
     //json schema: the requirement for how the json object look like, the structure of the json
       1.added json-schema-validator dependencies
       2.added the schema file
       3.wrote the line in body , provide the path of json schema
       i have the schema file i have to validate that i got from my developer, and i used that to automate the validation of the schema
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

    @DisplayName("Testing GET /spartans endpoint structure ")
    @Test
    public void testAllSpartanResponseSchema(){
        given()
                .spec(adminReqSpec)
                .when()
                .get("/spartans")
                .then()
                .spec(adminResSpec)
                .body(matchesJsonSchemaInClasspath("allSpartansSchema.json"));

    }



    @DisplayName("Testing POST /spartans endpoint structure ")
    @Test
    public void testPostSpartanResponseSchema() {

        // We can also use matchesJsonSchema method if we want to provide full path for this file

        File schemaFile = new File("src/test/resources/postSuccessResponseSchema.json");
        Spartan spartanPOJO_payload = SpartanUtil.getRandomSpartanPOJO_Payload();

        given()
                .spec(adminReqSpec)
                .contentType(ContentType.JSON)
                .body(spartanPOJO_payload)
        .when()
                .post("/spartans")
        .then()
                .log().all()
                .statusCode(is(201))
                 .body("data.name",is(spartanPOJO_payload.getName()))
       // .body(matchesJsonSchemaInClasspath("postSuccessResponseSchema.json"))
        // what if my schema file is somewhere else other than resource folder ?
        // then you need to provide full path and use different method
                .body(matchesJsonSchema( schemaFile ))
        ;

    }
//how does the data base rule for table enforced? constraints

    @DisplayName("Testing GET /spartans/search endpoint structure ")
    @Test
    public void testSearchSpartanResponseSchema() {
        given()
                .spec(adminReqSpec)
                .queryParam("gender","Male")
                .queryParam("nameContains","aa")
                .when()
                .get("/spartans/search")
                .then()
                .spec(adminResSpec)
                .body(matchesJsonSchemaInClasspath("searchSpartanSchema.json"));
    }
}
