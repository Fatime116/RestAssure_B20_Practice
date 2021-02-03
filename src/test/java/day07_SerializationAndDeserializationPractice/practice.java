package day07_SerializationAndDeserializationPractice;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;
import pojo.Spartan;
import utility.ConfigurationReader;
import utility.SpartanUtil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
import java.util.*;

import static io.restassured.RestAssured.* ;
public class practice {


    @BeforeAll
    public static void setUp(){
        //RestAssured.filters().add(new AllureRestAssured() ) ;
        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("Put 1 data with Java Object")
    @Test
    public void testAdd1DatalUpdate(){

        Map<String, Object> randomSpartanRequestPayload = SpartanUtil.getRandomSpartanRequestPayload();

        int id = 3;
        given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .pathParam("id",id)
                .body(randomSpartanRequestPayload)
                .when()
                .put("/spartans/{id}")
                .then()
                .statusCode(is(204))
                .body(emptyString());


        JsonPath jp = given()
                .auth().basic("admin","admin")
                .pathParam("id",id)
                .when()
                .get("/spartans/{id}")
                .then()
                .statusCode(is(200))
                .extract()
                .jsonPath();

        Spartan singleSpartan = jp.getObject("",Spartan.class);
        System.out.println("singleSpartan = " + singleSpartan);

    }




}
