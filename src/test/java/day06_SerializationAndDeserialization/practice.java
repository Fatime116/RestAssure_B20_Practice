package day06_SerializationAndDeserialization;

import org.junit.jupiter.api.BeforeAll;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;

import pojo.Spartan;
import pojo.SpartanRead;
import utility.ConfigurationReader;
import utility.SpartanUtil;

import java.util.List;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.* ;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class practice {

    @BeforeAll
    public static void setUp() {
        //RestAssured.filters().add(new AllureRestAssured() ) ;
        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api";
    }

    @AfterAll
    public static void tearDown() {
        reset();
    }


    @DisplayName("Get /api/spartans --Get All Data and Save Response JsonArray As Java Object ")
    @Order(1)
    @Test
    public void getAllSpartansAndSaveResponseAsPOJO() {


        JsonPath jp = given().
                auth().basic("admin", "admin").
                when().
                get("/spartans").prettyPeek()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();


        List<SpartanRead> allSpartansPOJO = jp.getList("", SpartanRead.class);
        //im gonna get entire json array converted to the SpartanRead POJO

        //System.out.println(allSpartansPOJO );
        allSpartansPOJO.forEach(System.out::println);


    }


    @Order(2)
    @DisplayName("POST /api/spartans  -post and Save Response JsonArray As Java Object")
    @Test
    public void addJavaObjectAndSaveResponseAsPOJO(){

        Spartan spartanPojoPayload = SpartanUtil.getRandomSpartanPOJO_Payload();

        JsonPath jp = given()
                .auth().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(spartanPojoPayload)//serialization--java object to JSON object
                .when().
                        post("/spartans")
                .then()
                .log().all()
                .statusCode(is(201))
                .extract()
                .jsonPath();


        /*
        {
    "success": "A Spartan is Born!",
    "data": {
        "id": 178,
        "name": "Soila",
        "gender": "Female",
        "phone": 6598941478
    }
}
         */

        //what we need to care about is json field inside data
        SpartanRead PostSpartan =  jp.getObject("data",SpartanRead.class);
        System.out.println("PostSpartan = " + PostSpartan);

    }

}
