package day06_SerializationAndDeserialization;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;

import pojo.Spartan;
import pojo.SpartanRead;
import utility.ConfigurationReader;
import utility.SpartanUtil;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.* ;

public class PostWithCustomObject_1 {
    @BeforeAll
    public static void setUp(){

        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("Add 1 Data with POJO as body")
    @Test
    public void testAddDataWithPojo() {

        //Spartan sp1 = new Spartan("B20 user","Male",1234567890L) ;
        Spartan sp1 = SpartanUtil.getRandomSpartanPOJO_Payload();

        given()
                .auth().basic("admin","admin")
                .log().all()
                .contentType(ContentType.JSON)
                .body(sp1).//serialization------java object to json object
                //provide java object in the body and let jackson data bind to take care of it
        when()
                .post("/spartans").
                then()
                .log().all()
                .assertThat()
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("success",is("A Spartan is Born!"))
                .body("data.name",is(sp1.getName()))
                .body("data.gender",is(sp1.getGender()))
                .body("data.phone",is(sp1.getPhone()));

    }
    @DisplayName("Add 1 Data with POJO as body")
    @Test
    public void testAddDataWithPojoAndSaveResponseJsonAsPOJO() {

        Spartan randomSpartanPOJO_payload = SpartanUtil.getRandomSpartanPOJO_Payload();

        Response response= given()
                .auth().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(randomSpartanPOJO_payload).//AddDataWithPojo--serialization
                        when()
                .post("/spartans").prettyPeek();

        /*
        {
    "success": "A Spartan is Born!",
    "data": {
        "id": 177,
        "name": "Dwight",
        "gender": "Male",
        "phone": 8505402580
    }
}
         */
        JsonPath jp = response.jsonPath();
        SpartanRead getOnePOJOObject=jp.getObject("data",SpartanRead.class);//deserialization
        // Save Response Json As POJO
        System.out.println("getOnePOJOObject = " + getOnePOJOObject);

    }

    }
