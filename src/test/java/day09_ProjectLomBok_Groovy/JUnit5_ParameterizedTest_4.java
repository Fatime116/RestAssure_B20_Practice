package day09_ProjectLomBok_Groovy;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JUnit5_ParameterizedTest_4 {

   // Parameterized Test makes it possible to run a test multiple times with different arguments
    //they are declared inside of @ParameterizedTest annotation
    // you must provide external parameter and declare as least one @ValueSource that will provide argument for each invocation
    //then use those arguments in the test method
    //strings={"","","","",""}
    //short,byte,int,float ,String and add s at the end of each data type

    //all the Test method does not have parameters
    @ParameterizedTest
    @ValueSource(ints = {5,6,7,8,1,2,3})
    public void test1(int myNum ){

        //assert 5,6,7,8,9 all less than 10
        //no loop for us, just let JUnit do the thing for us
        System.out.println("myNum = " + myNum);
        assertThat(myNum,is(lessThan(10)));//will not stop other line
      //  assertTrue(myNum<10);//even if one line is not passed, it will not abort my other tests

    }


    @ParameterizedTest
    @ValueSource(ints = {2,3,4,5,6})
    public void testSingleSpartan(int myID ) {
        Response response = given()
                .auth().basic("admin","admin")
                .contentType(ContentType.JSON)
                .pathParam("id", myID). //return request specification
                when()
                .get("http://3.95.171.166:8000/spartans/{id}")//the moment you use http method, gives you response
                .prettyPeek();
    }

    //using CSV file as source for parameterized test
    //it goes straightly to the csv file, and read the data
    //very first line is header
    @ParameterizedTest
    @CsvFileSource(resources ="/zipcode.csv",numLinesToSkip = 1)//we want to skip first line
    // add slash / in front of zipcode path
    public void  test2(String zip){

      //  System.out.println("zip = " + zip);

          //api.zippopotam.us/us/{zipcode}
        given()
                .log().uri()
                .baseUri("https://api.zippopotam.us")
                .pathParam("zipcode" , zip).
                when()
                .get("/us/{zipcode}").
                then()
                .statusCode(200)

        ;
    }

    //run the test with multiple columns in csv files source

    @ParameterizedTest
    @CsvFileSource(resources = "/country_zipcode.csv" , numLinesToSkip = 1)
    public void testCountryZipCodeCombo(String csvCountry, int csvZip){

        //api.zippopotam.us/{{country}}/{{zipcode}}
        given()
                .log().uri()
                .baseUri("https://api.zippopotam.us")
                .pathParam("country" , csvCountry)
                .pathParam("zipcode" , csvZip).
                when()
                .get("/{country}/{zipcode}").
                then()
                .statusCode(200) ;

    }
}
