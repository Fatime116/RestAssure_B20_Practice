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
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testbase.SpartanAdmin_TestBase;
import utility.SpartanUtil;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
public class JsonSchemaValidationPractice_1 extends SpartanAdmin_TestBase {

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

    @DisplayName("Testing POST /spartans endpoint structure ")
    @Test
    public void testPostSpartanResponseSchema() {

        Spartan spartanPOJO_payload = SpartanUtil.getRandomSpartanPOJO_Payload();

        given()
                .log().all()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(spartanPOJO_payload)
                .when()
                .post("spartans")
                .then()
                .log().all()
                 .body("data.name",is(spartanPOJO_payload.getName()))
        ;



    }
}