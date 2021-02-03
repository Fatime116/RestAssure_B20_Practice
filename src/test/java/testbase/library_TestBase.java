package testbase;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import sun.security.krb5.Config;
import utility.ConfigurationReader;

import static io.restassured.RestAssured.*;

public abstract class library_TestBase {

    @BeforeAll
    public static void setUp(){
        baseURI = "http://library1.cybertekschool.com";
        basePath = "/rest/v1" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

}
