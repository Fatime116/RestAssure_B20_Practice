package day02_header_PathParam_body;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;


public class SingleSpartanTest {

     @BeforeAll
     public static void setUpMethod(){
        baseURI =  "http://100.26.101.158:8000";
        basePath = "/api";
     }

     @AfterAll
     public static void tearDownMethod(){
         reset();
     }

     //for single Spartan, we need to provide pathParam either in get request or given() section
     @DisplayName("Testing  /spartans/{id}  endpoint")
     @Test
    public void test1Spartan(){

         given()
                 .accept(ContentType.JSON).
                 //this part extra, because by default single spartan end point return you JSON
         when()
                 .get("/spartans/2").
         then()
//                 .assertThat()
                 .statusCode(is(200))
                 .and()
                 .contentType(ContentType.JSON);


         //second way of getting request, adding pathparam in given section
         // the value 2 is path variable|params to uniquely identify the resource
         given()
                 .accept(ContentType.JSON)//extra step, because this endpoint ONLY accepts JSON, no need to specify again
                 .pathParam("id",2).//adding pathparam in given section
         when()
                 .get("/spartans/{id}").//same exact endPoints
         then()
          //     .assertThat()
                 .statusCode(is(200))
                 .contentType(ContentType.JSON);


         //third way of getting request providing different path param
         given()
                 .accept(ContentType.JSON).//extra step, because this endpoint ONLY accepts JSON, no need to specify again
         when()
                 .get("/spartans/{id}",2).
         then()
                 .assertThat()
                 .statusCode(is(200))
                 .contentType(ContentType.JSON);


     }

     /*
     {
    "id": 2,
    "name": "Elvira",
    "gender": "Female",
    "phone": 1456789230
}
      */
      @DisplayName("Testing GET  /spartans/{id}  endpoint payload")
      @Test
     public void test1SpartanPayload(){
          given()
                  .accept(ContentType.JSON).//extra step, because this endpoint ONLY accepts JSON, no need to specify again
          when()
                  .get("/spartans/{id}",2).
          then()
                  .assertThat()
                  .statusCode(is(200))
                  .contentType(ContentType.JSON)
                  .body("id",is(2))//it takes a Matcher as value
                  .body("name",equalTo("Elvira"))
                  .body("gender",is("Female"))
                  .body("phone",equalTo(1456789230));

      }

}
