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

public class SpartanAdmin_TestBase {
    public static RequestSpecification adminReqSpec;
    public static  ResponseSpecification adminResSpec;

    @BeforeAll
    public static void setUp(){
        baseURI= ConfigurationReader.getProperty("spartan.base_url");
        basePath="/api";
        //common request
        adminReqSpec = given().log().all()
                .auth().basic(ConfigurationReader.getProperty("spartan.admin.username"),
                ConfigurationReader.getProperty("spartan.admin.password"));
        //common expectation
       adminResSpec = expect().logDetail(LogDetail.ALL).statusCode(is(200)).contentType(ContentType.JSON);

    }
    @AfterAll
    public static void tearDown(){
        reset();
    }
}
