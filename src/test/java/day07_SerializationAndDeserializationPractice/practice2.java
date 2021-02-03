package day07_SerializationAndDeserializationPractice;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;
import pojo.Spartan;
import utility.ConfigurationReader;
import utility.SpartanUtil;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class practice2 {
private static int newID;

  @BeforeAll
  public static void setUp(){

      baseURI="http://3.95.171.166:8000";
      basePath="/api";
  }

    @Order(1)
    @Disabled
    @DisplayName("GET /spartans")
    @Test
    public void getALlSpartans(){

        given()
                .auth().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .when().get("/spartans")
                 .then()
        .log().all()
        .statusCode(is(200))
        .contentType(ContentType.JSON)
        .body("id[0]",is(170))
        .body("name[0]",is("Maribeth"))
        ;
    }

    @DisplayName("GET /spartans/{id}")
    @Test
    public void getSingleSpartan() {
        given()
                .auth().basic("admin", "admin")
                .pathParam("id",170)
                .when()
                .get("/spartans/{id}")
                .then()
                .log().all()
                .statusCode(is(200))
        .body("id",is(170))
        .body("name",is("Maribeth"))
        .body("phone",is(6778792227l));
    }

    @DisplayName("GET /spartans/search")
    @Test
    public void getSearchSpartan() {
        given()
                .auth().basic("admin", "admin")
                .queryParam("gender", "Male")
                .queryParam("nameContains", "a")
                .when()
                .get("/spartans/search")
                .then()
                .log().all()
                .statusCode(is(200))
        .body("content.id[0]",is(170))
        .body("numberOfElements",is(54))
        .body("pageable.sort.sorted",is(false))
        ;
    }


    @DisplayName("POST /spartans")
    @Test
    public void postSpartanAsString() {

       String str = "{\n" +
               "        \"name\": \"Mulati\",\n" +
               "        \"gender\": \"Male\",\n" +
               "        \"phone\": 1234512345\n" +
               "    }";

      given()
              .auth().basic("admin","admin")
              .contentType(ContentType.JSON)
              .body(str)
      .when()
      .post("/spartans")
      .then()
      .log().all()
      .statusCode(is(201))
      .contentType(ContentType.JSON)
      .body("success",is("A Spartan is Born!"))
      .body("data.name",is("Mulati"));

    }

    @DisplayName("POST /spartans")
    @Test
    public void postSpartanAsMap() {


//        Map<String,Object> mapObj = new LinkedHashMap<>();
//        mapObj.put("name","fatime");
//        mapObj.put("gender","Female");
//        mapObj.put("phone",1234512345);

        Map<String, Object> mapObj = SpartanUtil.getRandomSpartanRequestPayload();

         newID = given()
                .auth().basic("admin", "admin")
                .contentType("application/json")
                .body(mapObj)//serialization
                .when()
                .post("/spartans").prettyPeek()
                .then()
                .log().all()
                .statusCode(is(201))
                .extract()
                .jsonPath().getInt("data.id");


//        JsonPath jp = response.jsonPath();
      //  int newID = jp.getInt("data.id");
     //   System.out.println("newID = " + newID);

        JsonPath jp = given()
        .auth().basic("admin","admin")
        .pathParam("id",newID)
        .when()
        .get("/spartans/{id}")
        .then()
                .log().all()
        .statusCode(is(200))
        .body("id",is(newID))
        .body("name",is(mapObj.get("name")))
        .body("gender",is(mapObj.get("gender")))
        .extract()
        .jsonPath()
        ;

        String name = jp.getString("name");
        System.out.println("name = " + name);

        long phone = jp.getLong("phone");
        System.out.println("phone = " + phone);


    }
    @DisplayName("POST /spartans")
    @Test
    public void postSpartanAsExternalFile() {


        File file = new File("singleSpartan.json");

        given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .body(file)//serialization
                .when()
                .post("/spartans")
                .then()
                .log().all()
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("success",is("A Spartan is Born!"))
                .body("data.name",is("Olivia"));

    }





}
