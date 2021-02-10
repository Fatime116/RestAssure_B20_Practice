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

import org.junit.jupiter.api.Test;

public class Test_XML_Response extends SpartanAdmin_TestBase {

    //get xml response for GET /spartans
    //https://codebeautify.org/xmlviewer

    @Test
    public void testXML()  {

       given()
               .spec(adminReqSpec)
               .accept(ContentType.XML)//what kind of data we want to get
               .when()
               .get("/spartans")//this is only endPoint which sends us XML
               .then()
               .log().all()
               .statusCode(is(200))
               .contentType(ContentType.XML)
               ;

    }

}
