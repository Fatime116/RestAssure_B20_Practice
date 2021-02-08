package testbase;

import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utility.ConfigurationReader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class Spartan_TestBase {

    @BeforeAll
    public static void setUp(){
        baseURI= ConfigurationReader.getProperty("spartan.base_url");
        basePath="/api";
        //common request
        RequestSpecification reqSpec = given().log().all().auth().basic("admin","admin");
        //common expectation
        ResponseSpecification resSpec = expect().logDetail(LogDetail.ALL).statusCode(is(200)).contentType(ContentType.JSON);

    }
    @AfterAll
    public static void tearDown(){
        reset();
    }
}
