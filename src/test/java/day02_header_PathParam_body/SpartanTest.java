package day02_header_PathParam_body;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;

public class SpartanTest {

   //basePoint will be changing, but endPoints always the same according to the document

    @BeforeAll
    public static void setUp(){
        // baseURI and basePath is static fields of RestAssured Class
        // Since we static imported RestAssured, we can access all static field directly just like it's in our own class here
        // you can use static way as below
        //RestAssured.baseURI = "http://100.26.101.158:8000";
        // or you can directly use as below
        baseURI = "http://3.95.171.166:8000";
        //RestAssured.basePath = "/api" ;
        basePath = "/api" ;
        // baseURI + basePath + whatever you provided in http method like get post
        // for example :
        // get("/spartans") -->>  get(baseURI + basePath + "/spartans")
    }

    @AfterAll
    public static void tearDown(){
        //since we are working on different api, when we are running our entire tests, there will be appearing some conflicts
        //so after each class, we are gonna reset the two bases empty(original value) to whatever it was before in order to avoid the conflicts

        // resetting the value of baseURI , basePath to original value which are empty
        //  RestAssured.reset();
        reset();
        //after resetting
        // the original values are : baseURI =  "http://localhost";
        //                           basePath = "";
    }
    @DisplayName(" Testing /api/spartans enpoint ")
    @Test
    public void testGetAllSpartan(){

        //send a get request to above endpoint
        //save the response
        Response response = given().auth().basic("admin","admin").when().get("/spartans");
        //print out the result
        response.prettyPrint();
        //try to assert the status code
        assertThat(response.statusCode(),is(200));

        //try to assert content type header
        //ContentType.JSON ===>returns Enum(Object type), so in order to compare two exact the same thing, we need to convert it into String
        //response.contentType()==>returns String
        //by default this endPoint( /api/spartans ), accept response content type header is JSON
        //               String          ,       Object.toString()
        assertThat(response.contentType(),is(ContentType.JSON.toString()));


    }

    @DisplayName(" Testing /api/spartans enpoint XML response ")
    @Test
    public void testGetAllSpartanXML(){
        /**
         * given
         *      --- RequestSpecification
         *      used to provide additional information about the request
         *      base url  base path
         *      header , query params , path variable , payload
         *      authentication authorization
         *      logging , cookie
         * when
         *      --- This is where you actually send the request with http method
         *      -- like GET POST PUT DELETE .. with the URL
         *      -- We get Response Object after sending the request, our response object is Json Object
         * then
         *      -- ValidatableResponse
         *      -- This is where we can do validation
         *      -- validate status code , header , payload , cookie
         *      -- responseTime , structure of the payload , logging response
         */

        // Response response = get("http://100.26.101.158:8000/api/spartans");
        //by default, accept header is JSON, so we need to specify XML format specifically

         given()
                .header("accept", "application/xml").//i want to get XML format, so need to specify accept header
         when()
                .get("/spartans").
         then()//response object
                .assertThat()  //do nothing, but just makes it more readable and obvious that this is where we start assertion,not from hamcrest mathers
                .statusCode(200)
                .and()//it just makes you read a little bit fluently
                 .header("Content-Type","application/xml");
              //  .contentType(ContentType.XML);


        // This will do same exact thing as above in slightly different way
        // since accept header and content type header is so common ,
        // RestAssured has good support or those header by providing "method directly "
        // rather than using header method we used above

        given()
                .accept(ContentType.XML).//i am expecting XML from server by specifying accept header
                //accept("application/xml")
        when()
                .get("/spartans").
        then()
                .assertThat()
                .statusCode(is(200))
                .and()
                .contentType(ContentType.XML);//contentType("application/xml")
    }

}
