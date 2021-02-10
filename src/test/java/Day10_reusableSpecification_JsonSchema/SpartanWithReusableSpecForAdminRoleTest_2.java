package Day10_reusableSpecification_JsonSchema;

import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;

import pojo.Spartan;
import utility.ConfigurationReader;
import utility.SpartanUtil;

import java.util.concurrent.TimeUnit;

public class SpartanWithReusableSpecForAdminRoleTest_2 {

    static RequestSpecification  reqSpec ;
    static ResponseSpecification resSpec ;
    static RequestSpecification postReqSpec ;
    static Spartan randomSpartanPayload ;
    @BeforeAll
    public static void setUp(){
        baseURI= ConfigurationReader.getProperty("spartan.base_url");
        basePath="/api";
        //common request
        reqSpec = given().log().all()
                .auth().basic("admin","admin");
        //common expectation
        resSpec = expect().logDetail(LogDetail.ALL)
                .statusCode(is(200))
                .contentType(ContentType.JSON);

        randomSpartanPayload = SpartanUtil.getRandomSpartanPOJO_Payload();
        postReqSpec = given().spec(reqSpec)//reuse the previous step
                .contentType(ContentType.JSON)//telling the server what kind of data we are sending
                .body(randomSpartanPayload);



    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("GET /spartans/{id} EndPoint Test")
    @Test
    public void testSingleSpartan(){

        given()
                .spec(reqSpec)
        .pathParam("id","7")
        .when()
        .get("/spartans/{id}")
        .then()
        .spec(resSpec)
        ;

        reqSpec
                .pathParam("id","7")
        .when()
                .get("/spartans/{id}")
        .then()
                .spec(resSpec)
        ;
    }
  //response: you actually got as a response according to what you send as a the request, it is a physical stuff, the actual thing.
  // including all the data, all the headers
  //responseSpecification:it is not physical, it is the something(expectation) that you had in your mind
   @DisplayName("GET /spartans EndPoint Test")
    @Test
    public void testAllSpartan(){

      // RequestSpecification rSpec = given().log().all().auth().basic("admin","admin");
       //we put rSpec in the class level, so our every test can use this

       given()
              // .log().all()
              // .auth().basic("admin","admin");
             .spec(reqSpec)//each and every request we use log.all and basic auth,so we just take this step out
      .when()
               .get("/spartans")
      .then()
       .spec(resSpec)
       //we just moved it to the ResponseSpecification
//               .log().all()
//               .statusCode(is(200))
//               .contentType(ContentType.JSON)
       ;
   }

    @DisplayName("POST /spartans EndPoint Test")
    @Test
    public void testPostSingleSpartan() {




        ResponseSpecification postResponseSpec = expect()
                .logDetail(LogDetail.ALL)
                .statusCode(is(201))
                .contentType(ContentType.JSON)
                .body("success",is("A Spartan is Born!"))
                .body("data.id",notNullValue())
                .body("data.name",is(randomSpartanPayload.getName()))
                .body("data.gender",is(randomSpartanPayload.getGender()))
                .body("data.phone",is(randomSpartanPayload.getPhone()))
                ;

        given()
                .spec(postReqSpec)
        .when()
                .post("/spartans")
        .then()
                .spec(postResponseSpec);

//RBAC: Role Based Access Control:
//     testing the people with the correct access can do everything that they are supposed to do
//    people without the correct access should not be able to do anything what they are not supposed to do
    }


    //I also test my response time
    @DisplayName("GET /spartans  check response time <2 second" )
    @Test
    public void testAllSpartanResponseTime() {
        given()
                .spec(reqSpec)
                .when()
                .get("/spartans")
                .then()
                .spec(resSpec)
                .time(lessThan(2000L))//it is milli-second, 1000 is 1 second
                .time(lessThan(2L), TimeUnit.SECONDS);
    }


    @DisplayName("/spartans Bad Request negative scenario")
    @Test
    public void  testBadRequest400ResponseBody(){

        Spartan badPayload = new Spartan("A","A",100L);
      given()
                .spec(postReqSpec)
                .body(badPayload)//even if postReqSpec has body, it will accept the last payload
                .when()
                .post("/spartans")
                .then()
                .statusCode(is(400))
                .body("errors",hasSize(3))
              //.body("errors[0].defaultMessage",is("name should be at least 2 character and max 15 character"))
            //  .body("errors[1].defaultMessage",is("Gender should be either Male or Female"))
             // .body("errors[2].defaultMessage",is("Phone number should be at least 10 digit and UNIQUE!!"))
              //order is not predictable,it is random
             .body("errors.defaultMessage",containsInAnyOrder("Gender should be either Male or Female","Phone number should be at least 10 digit and UNIQUE!!","name should be at least 2 character and max 15 character"))//does not care about order
               .body("message",containsString("Error count: 3"));

        //verify the error field has value of json array with 3 items
        //verify default messages for gender field

        //"Gender should be either Male or Female"
        // "name should be at least 2 character and max 15 character"
        //"Phone number should be at least 10 digit and UNIQUE!!"

        //verify message field contains "Error count: 3"

    }
}
