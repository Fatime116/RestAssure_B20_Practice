package day05_HamcrestCollectionSupport;
import io.restassured.http.ContentType;
import utility.ConfigurationReader;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.* ;
import org.junit.jupiter.api.DisplayName;
import utility.SpartanUtil;


import java.util.Map;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("Spartan App End to End CRUD Happy Path")
public class Spartan_E2E_HappyPath_5 {

   private static Map<String,Object> payloadMap ;
   private static int newID;

    // crud operation -- CREATE-- READ -- UPDATE -- DELETE
    @BeforeAll
    public static void setUp(){
        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api" ;
        payloadMap  = SpartanUtil.getRandomSpartanRequestPayload();

    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("1. Testing POST /api/spartans Endpoint")
    @Test
    public void testAddData() {

         newID = given()
                .auth().basic("admin", "admin")
                .log().all()
                .accept(ContentType.JSON)//what kind of data content type we want to accept(expect) as a response
                .contentType(ContentType.JSON)//what content type we are sending in the request
                .body(payloadMap).//to make it class level so that our every test can access
                when()
                .post("/spartans").
                        then()
                .log().all()
                .assertThat()
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("data.name", is(payloadMap.get("name")))//validation body part
                .body("data.gender", is(payloadMap.get("gender")))
                .body("data.phone", equalTo(payloadMap.get("phone")))
                .extract()
                .jsonPath()
         .getInt("data.id")
         ;
        System.out.println("newID = " + newID);
    }
    @DisplayName("2. Testing GET /api/spartans/{id} Endpoint")
    @Test
    public void testGet1SpartanData() {

        given()
                .auth().basic("admin","admin")
                .log().all()
                .pathParam("id",newID).//i need to have valid id for getting single spartan
                when()
                .get("/spartans/{id}")
                .then()
                .log().all()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(is(200))
                .body("id",is(newID))
                .body("name",is(payloadMap.get("name")))
                .body("gender",is(payloadMap.get("gender")))
                .body("phone",is(payloadMap.get("phone")))
        ;
    }
    @DisplayName("3. Testing PUT /api/spartans/{id} Endpoint")
    @Test
    public void testUpdate1SpartanData() {
        // We want to have different payload so we can update
        // Option is rerun the utility method to override
        // existing map object with newly generated faker map object
        payloadMap = SpartanUtil.getRandomSpartanRequestPayload();
//        System.out.println("payloadMap = " + payloadMap);
        given()
                .auth().basic("admin","admin")
                .pathParam("id" , newID)
                .accept(ContentType.JSON)
                .body(payloadMap) // updated payload
                .log().all().
        when()
                .put("/spartans/{id}").
        then()
                .log().all()
                .assertThat()
                .statusCode( is(204) )
                .body( emptyString() )//no body for put request
        ;
        // in order to make sure the update actually happened
        // i want to make another get request to this ID
        given()
                .auth().basic("admin","admin")
                .pathParam("id" , newID)
                .log().all().
                when()
                .get("/spartans/{id}").
                then()
                .log().all()
                .assertThat()
                .statusCode( is (200) )
                .contentType(ContentType.JSON)
                .body("id" , is(newID) )
                .body("name" , is( payloadMap.get("name")  ) )
                .body("gender" , is( payloadMap.get("gender") ) )
                .body("phone" , is( payloadMap.get("phone") ) )
        //             actual           expected
        ;

    }

    @DisplayName("4. Testing DELETE /api/spartans/{id} Endpoint")
    @Test
    public void testDelete1SpartanData() {

        given()
                .auth().basic("admin","admin")
                .pathParam("id" , newID)
                .log().all().
        when()
                .delete("/spartans/{id}")
        .then()
                .assertThat()
                .statusCode(is(204))
                .body(emptyString())
                ;

        // in order to make sure the delete actually happened
        // i want to make another get request to this ID expect 404
        given()
                .auth().basic("admin","admin")
                .pathParam("id",newID)
                .log().all()
                .when()
                .get("/spartans/{id}")
                .then()
                .assertThat()
                .statusCode(is(404))
                .log().all()
                ;
    }

    }
