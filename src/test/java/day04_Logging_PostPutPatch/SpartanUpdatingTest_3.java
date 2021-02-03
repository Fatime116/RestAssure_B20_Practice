package day04_Logging_PostPutPatch;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;

public class SpartanUpdatingTest_3 {

    @BeforeAll
    public static void setUpMethod(){
        baseURI =  "http://107.23.57.180:8000";
        basePath = "/api";
    }

    @AfterAll
    public static void tearDownMethod(){
        reset();
    }


    @DisplayName("Testing PUT /api/spartans/{id} with String body")
    @Test
    public void testUpdatingSingleSpartanWithStringBody() {


        String newSpartanStr = "{\n" +
                "        \"name\": \"B20 \",\n" +    //exclude id
                "        \"gender\": \"Male\",\n" +
                "        \"phone\": 1608075534\n" +
                "    }";

        given()
//                .log().all()
                .auth().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .pathParam("id", 3)
                .body(newSpartanStr).
                when()
                .put("spartans/{id}").
                then()
                .log().all()
                .assertThat()
                .statusCode(is(204))//for put request
                .body(is(emptyString()))
                // This is how we can check a header exists by checking the value is not null
                // using notNullValue() matcher
                .header("Date", is(notNullValue()))

        ;

    }
        @DisplayName("Testing PATCH /api/spartans/{id} with String body")
        @Test
        public void testPartialUpdatingSingleSpartanWithStringBody() {

            // update the name to B20 Patched
            // {"name" : "B20 Patched"}


            String patchBody = "{\"name\" : \"B20 Patched\"}";
            given()
                    .auth().basic("admin", "admin")
                    .log().all()//we should put on the top
                    .pathParam("id", 691)
                    .contentType(ContentType.JSON)
                    .body(patchBody).
             when()
                    .patch("/spartans/{id}").
             then()
                    .log().all()
                    .assertThat()
                    .statusCode(is(204))
                    // body for 204 response is always empty
                    // we can validate it using emptyString() matcher
                    .body(emptyString())
            ;
        }

    @DisplayName("Testing Delete /api/spartans/{id}")
    @Test
    public void testDeletingSingleSpartan(){
        given()
                .log().all()
                .auth().basic("admin","admin")
                .pathParam("id",732).
        when()
                .delete("/spartans/{id}").
        then()
                .log().all()
                .assertThat()
                .statusCode(is(204))
                .body(emptyString())

        ;

    }
}
