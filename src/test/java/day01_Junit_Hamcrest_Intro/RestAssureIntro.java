package day01_Junit_Hamcrest_Intro;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class RestAssureIntro {


    @DisplayName("Spartan /api/hello Endpoint Test")
    @Test
    public void TestHello(){
    // This is the public ip I shared for spartan2
    //http://100.25.29.10:8000/api/hello

        // make sure this is what's imported for data type Response
        // import io.restassured.response.Response;
        Response response = get("http://3.95.214.153:8000/api/hello");

        // get status code out of this Response object
        System.out.println("response.statusCode() = " + response.statusCode());//200

        // assert the status code is 200
        assertThat(response.statusCode(),is(200));

        // how to pretty print entire "response body"
        // prettyPrint() -- print and return the body as String
        String responseBodyAsString = response.prettyPrint();//print the body as a String

        System.out.println("==========================");

        // assertThat the body is Hello from Sparta
        assertThat(responseBodyAsString,is("Hello from Sparta"));

        // get the header called ContentType from the response
        //three ways of getting content-Type header in restAssure
        System.out.println("response.getHeader(\"Content-Type\") = " + response.getHeader("Content-Type"));//text/plain;charset=UTF-8 as String
        System.out.println("response.getContentType() = " + response.getContentType());//text/plain;charset=UTF-8 as String
        System.out.println("response.contentType() = " + response.contentType());//returns text/plain;charset=UTF-8 as String
        System.out.println("response.header(\"Content-Type\") = " + response.header("Content-Type"));//text/plain;charset=UTF-8


        // assert That Content-Type is text/plain;charset=UTF-8
        assertThat(response.contentType() ,  is("text/plain;charset=UTF-8") );
        // assert That Content-Type startWith text
        assertThat(response.contentType(),startsWith("text"));


        // Easy way to work with Content-type without typing much
        // We can use "ContentType Enum" from RestAssured to easily get "main" part content-type
        // ContentType.TEXT -->> returns  text/plain as Enum(Object) not as String
        // startWith accept a String object
        // so use toString method to convert ContentType.TEXT to String so we can use it startWith
        //response.contentType() ===returns you String
        assertThat(response.contentType(),startsWith(ContentType.TEXT.toString()));// /api/hello end point returns us ONLY plain text

        assertThat(response.contentType() ,  is( not(ContentType.JSON)   ) );//since end point does not return us JSON we can assert that this endpoint does not return JSON



    }

}
