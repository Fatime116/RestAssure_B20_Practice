package day04_Logging_PostPutPatch;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class OpenMovieDB_1 {

    //http://www.omdbapi.com/?t=The+Boss+baby&apikey=aac6aabf
    @BeforeAll
    @Test
    public static void setUp(){
        baseURI="http://www.omdbapi.com";
    }

    @AfterAll
    public static void teardown(){
        reset();
    }

    @DisplayName("Test Search Movie or OpenMovie Test")
    @Test
    public void testMovie(){

        given()
                .queryParam("apikey","aac6aabf")
                .queryParam("t","The Boss Baby").

        when()
                .get().prettyPeek().//no need to add slash / GET request
        then()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("Title",is("The Boss Baby"))
              //  .body("Ratings.Source[0]",is("Internet Movie Database"))
                .body("Ratings[0].Source",is("Internet Movie Database"))
        ;
    }

    @DisplayName("Getting the log of request and response")
    @Test
    public void testSendingRequestAndGetTheLog(){
        given()
                .queryParam("apikey","aac6aabf")
                .queryParam("t","John Wick")
               //  logging the response should be in given section
             //   .log().all().//i want to know what request is being sent and what kind of info i got
           //  .log().uri().//only give me the url im sending
             .log().params().
        when()
                .get().
        then()
               // .log().all()
               .log().status()//HTTP/1.1 200 OK
               .log().body()
                .log().ifValidationFails()  //if validation fails, just print me
                .statusCode(is(200))
                .body("Plot",containsString("ex-hit-man"))
                .body("Ratings[1].Source",is("Rotten Tomatoes"))//second rating source is Rotten Tomatoes

            ;
    }
}
