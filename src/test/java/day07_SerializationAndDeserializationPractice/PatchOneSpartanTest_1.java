package day07_SerializationAndDeserializationPractice;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;
import pojo.Spartan;
import utility.ConfigurationReader;

import java.util.*;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.*;
public class PatchOneSpartanTest_1 {
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

    @DisplayName("Patching 1 data with Java Object")
    @Test
    public void testPatch1DataPartialUpdate() {
        // we just want to update the name and phone number, just fields

       Map<String,Object> patchBodyMap = new LinkedHashMap<>();
       patchBodyMap.put("name","B20 Voila");
        patchBodyMap.put("phone",7234561234L);



        given()
                .auth().basic("admin", "admin")
                .log().all()
                .pathParam("id", 3)
                .contentType(ContentType.JSON)
                .body(patchBodyMap).
                        when()
                .patch("/spartans/{id}")
                .then()
                .statusCode(is(204))
                .body(emptyString())
;

    }
//so we added @JsonIgnoreProperties(ignoreUnknown = true) into respective pojo class

    // we just want to update the name and phone number
    //Spartan sp = new Spartan("B20 Vvv","",7234561234L);//we cannot have it as empty String

    //even if we just used default constructor and use setter to just update the fields that we want
    // it will giving use some null values

    @DisplayName("Patching 1 data with POJO")
    @Test
    public void testPath1DataPartialUpdateWithPOJO(){


        Spartan sp = new Spartan() ;

        sp.setName("B20 VOILA");
        sp.setPhone(9876543210L);
        // MAP IS A BETTER OPTION WITH MINIMAL EFFORT
        // POJO class need some handling to ignore empty field values
        // when being serialized

        given()
                .auth().basic("admin", "admin")
                .log().all()
                .pathParam("id", 170)
                .contentType(ContentType.JSON)
                .body(sp).
                when()
                .patch("/spartans/{id}")
        .then()
                .log().all()
        .statusCode(is(500))

        ;

//body that we got:
//        {
//            "name": "B20 VOILA",
//                "gender": null,
//                "phone": 9876543210
//        }
    }

}
