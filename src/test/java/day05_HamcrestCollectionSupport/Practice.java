package day05_HamcrestCollectionSupport;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import utility.ConfigurationReader;
import utility.SpartanUtil;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Practice {

    private static Map<String,Object> payloadMap ;
    private static int newID;
    @BeforeAll
    public static void setUp(){

       // baseURI = "http://107.23.57.180:8000";
        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api";
        payloadMap= SpartanUtil.getRandomSpartanRequestPayload();

    }
   @AfterAll
   public static void tearDown(){
        reset();
   }

   @Disabled
   @Order(1)
    @DisplayName("Testing GET /api/spartans/search with Basic auth")
    @Test
    public void testSearchAndExtractData(){
// search for nameContains : a , gender Female
        // verify status code is 200
        // extract jsonPath object after validation
        // use that jsonPath object to get the list of all results
        // and get the numberOfElements field value
        // compare those 2

        JsonPath jp = given()
                .auth().basic("admin","admin")
                .queryParam("nameContains","a")
                .queryParam("gender","Female")
                .accept(ContentType.JSON).
                        when()
                .get("/spartans/search")
                .then()
                .log().all()
                .statusCode(is(200))
                .extract()
                .jsonPath();

        List<String> allNames = jp.getList("content.name");
        System.out.println("allNames = " + allNames);

        List<String > allGenders = jp.getList("content.gender");
        System.out.println("allGenders = " + allGenders);

        List<Long> phones = jp.getList("content.phone");
        System.out.println("phone = " + phones);

        int totalElements = jp.getInt("totalElements");
        System.out.println("totalElements = " + totalElements);

        int numberOfElements = jp.getInt("numberOfElements");
        System.out.println("numberOfElements = " + numberOfElements);
        assertThat(allNames,hasSize(numberOfElements));



    }

    @Disabled
    @Order(2)
    @Test
    public void testSearchAndExtractData2(){


        given()
                .auth().basic("admin","admin")
                .queryParam("gender","Female")
                .queryParam("nameContains","a")
                .accept(ContentType.JSON)
                .when()
                .get("/spartans/search")
                .then()
                .log().all()
                .statusCode(is(200))
                .body("content.name",everyItem(containsStringIgnoringCase("a")))
                .body("content",hasSize(45));

    }

    @Order(3)
    @DisplayName("POST ")
    @Test
    public void addData(){

        newID = given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(payloadMap).
                when()
                .post("/spartans")
                .then()
                .log().all()
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("success",is("A Spartan is Born!"))
                .body("data.name",is(payloadMap.get("name")))
                .body("data.gender",is(payloadMap.get("gender")))
                .extract()
                .jsonPath()//jp object
                .getInt("data.id");

        System.out.println(newID);
    }

    @Order(4)
    @DisplayName("GET")
    @Test
    public void getData(){

        given()
                .auth().basic("admin","admin")
                .pathParam("id",newID)
                .when()
                .get("/spartans/{id}")
                .then()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("id",is(newID))
                .body("name",is(payloadMap.get("name")))
                .body("gender",is(payloadMap.get("gender")))
                .body("phone",is(payloadMap.get("phone")));
    }

    @Order(5)
    @DisplayName("PUT")
    @Test
    public void updateData(){

        payloadMap = SpartanUtil.getRandomSpartanRequestPayload();
//        System.out.println("payloadMap = " + payloadMap);
        given()
                .auth().basic("admin","admin")
                .pathParam("id" , newID)
                .contentType(ContentType.JSON)
                .body(payloadMap) // updated payload
                .log().all().
                when()
                .put("/spartans/{id}").
                then()
                .log().all()
                //it has no content, so we dont need to verify contentType
                .assertThat()
                .statusCode( is(204) )
                .body( emptyString() )//no body for put request
        ;


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

    @Order(6)
    @DisplayName("DELETE")
    @Test
    public void deleteData(){

        given()
                .auth().basic("admin","admin")
                .pathParam("id",newID)
                .when()
                .delete("/spartans/{id}")
                .then()
                .statusCode(is(204))
                .body(emptyString());


        given()
                .auth().basic("admin","admin")
                .pathParam("id",newID)
                .when()
                .get("/spartans/{id}")
                .then()
                .log().all()
                .statusCode(is(404));

    }

}
