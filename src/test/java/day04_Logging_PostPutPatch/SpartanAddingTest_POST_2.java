package day04_Logging_PostPutPatch;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class SpartanAddingTest_POST_2 {

    @BeforeAll
    public static void setUpMethod(){
        baseURI =  "http://3.95.171.166/:8000";
        basePath = "/api";
    }

    @AfterAll
    public static void tearDownMethod(){
        reset();
    }

    @DisplayName("Testing GET /api/spartans with Basic auth")
    @Test
    public void testAllSpartanWithBasicAuth(){

        given()
                .log().all()//shows what kind of request being sent
                .auth().basic("admin","admin").//basic auth
                //if we provide wrong auth, we got 401
        when()
                .get("/spartans").
        then()
                .log().all()
                .statusCode(is(200));

    }

    @DisplayName("Add 1 Data with Raw Json String POST /api/spartans")
    @Test
    public void testAddOneData() {
        /*
            {
        "id": 92,
        "name": "Zella",
        "gender": "Male",
        "phone": 1608075534
             }
         */

        String newSpartanStr = "{\n" +
                "        \"name\": \"Zella\",\n" +    //exclude id
                "        \"gender\": \"Male\",\n" +
                "        \"phone\": 1608075534\n" +
                "    }";

        System.out.println(newSpartanStr);

        given()
                .log().all()//shows what kind of request being sent
                .auth().basic("admin","admin")//basic auth
                .contentType(ContentType.JSON)//we must tell the server what kind of data format we are sending
             //   .accept(ContentType.JSON)
                .body(newSpartanStr).//we need to provide body, here provided it as String
        when()
                .post("/spartans").
                then()
                .log().all()
                .statusCode(is(201))//post request status code
                 .contentType(ContentType.JSON)
                 .body("success",is("A Spartan is Born!"))
                 .body("data.name",is("Zella"))
                .body("data.gender",is("Male"))
                .body("data.phone",is(1608075534))
         ;
    }


    @DisplayName("Add 1 Data with Map Object POST /api/spartans")
    @Test
    public void testAddOneDataWithMapAsBody() {

        Map<String, Object>  payloadMap = new HashMap<>();
        payloadMap.put("name","Tucky");
        payloadMap.put("gender","Male");
        payloadMap.put("phone",1212121212);

        //System.out.println("payloadMap = " + payloadMap);

        given()
                .auth().basic("admin","admin")
                .log().all()
                .contentType(ContentType.JSON)
                .body(payloadMap). //my object is map object, i need to turn Map object(java object) into Json object(serializing)
                //if i did not send as JSON object, i will get IllegalStateException
                //cannot serialize object
                //so we added jackson-data bind dependency for supporting java object conversion into Json. ( serializing/ deserializing)
        when()
                .post("/spartans").
        then()
                .log().all()
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("success",is("A Spartan is Born!"))
                .body("data.name",is("Tucky"))
                .body("data.gender",is("Male"))
                .body("data.phone",is(1212121212));

    }


    @DisplayName("Add 1 Data with External json file POST /api/spartans")
    @Test
    public void testAddOneDataWithJsonFileAsBody() {

        // Create a file called singleSpartan.json right under root directory
        // with below content
        /*
        {
            "name": "Olivia",
            "gender": "Female",
            "phone": 6549873210
        }
        add below code to point File object to this singleSpartan.json
         */

        File externalJson = new File("singleSpartan.json");
        given()
                .auth().basic("admin","admin")
                .log().all()
                .contentType(ContentType.JSON)
                .body(externalJson).
       when()
                .post("/spartans").
        then()
                .log().all()
                .contentType(ContentType.JSON)
                .statusCode(is(201))
                .body("success",is("A Spartan is Born!"))
                .body("data.name",is("Olivia"))
                .body("data.gender",is("Female"))
                .body("data.phone",is(6549873210L));

    }

}
