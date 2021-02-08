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
import org.junit.jupiter.api.BeforeAll;
import pojo.Spartan;
import testbase.Spartan_TestBase;
import utility.ConfigurationReader;
import utility.SpartanUtil;

public class SpartanWithReusableSpecForAdminRoleTest_2 {

    static RequestSpecification  reqSpec ;
    static ResponseSpecification resSpec ;
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
        Spartan randomSpartanPayload = SpartanUtil.getRandomSpartanPOJO_Payload();

        RequestSpecification postReqSpec = given().spec(reqSpec)//reuse the previous step
                .contentType(ContentType.JSON)//telling the server what kind of data we are sending
                .body(randomSpartanPayload);

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
}
