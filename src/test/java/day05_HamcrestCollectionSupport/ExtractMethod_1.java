package day05_HamcrestCollectionSupport;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;
import java.util.List;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class ExtractMethod_1 {

    /*
     extract() method of RestAssured
     enable you to extract data after validation
     in then section of the method chaining
     */
    @BeforeAll
    public static void setUp(){
        baseURI = "http://107.23.57.180:8000";
        basePath = "/api" ;
    }

    //http://100.26.101.158:8000/api/spartans/search?nameContains=de&gender=Male

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("Testing GET /api/spartans/search with Basic auth")
    @Test
    public void testSearchAndExtractData(){
        // search for nameContains : a , gender Female
        // verify status code is 200
        // extract jsonPath object after validation
        // use that jsonPath object to get the list of all results
        // and get the numberOfElements field value
        // compare those 2

        JsonPath jp = given()
                .log().all()
                .auth().basic("admin", "admin")
                .queryParam("nameContains", "a")
                .queryParam("gender", "Female").
                        when()
                .get("/spartans/search").
                        then()
                .assertThat()
                .log().all()
                .statusCode(is(200))
                .extract()
                .jsonPath();//since we are going to get more than one information, so we need to assign to jp object
               //we only get limited to get one info

        // get the list of all names in String
        List<String> allNames = jp.getList("content.name");//since we have more elements we use getList method
        System.out.println("allNames = " + allNames);// if we provide wrong json path, we will get null

        // we are getting numberOfElements field from json result
        // since it's a top level key , json path will be just numberOfElements
        int numberOfElements = jp.getInt("numberOfElements");
        System.out.println("numberOfElements = " + numberOfElements);

        // verifying the numOfElements match the size of list we got
        //assertThat(numberOfElements,equalTo(allNames.size())); //old way

        // using hamcrest matcher collection support for asserting the list size
        assertThat(allNames,hasSize(numberOfElements));

    }
}
