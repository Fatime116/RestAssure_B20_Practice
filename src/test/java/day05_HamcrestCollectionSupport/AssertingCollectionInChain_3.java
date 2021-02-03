package day05_HamcrestCollectionSupport;
import org.junit.jupiter.api.*;
import utility.ConfigurationReader;


import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssertingCollectionInChain_3 {

    @BeforeAll
    public static void setUp(){
        baseURI = ConfigurationReader.getProperty("spartan.database.url");
        basePath = "/api" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @Order(1)
    @DisplayName("Testing GET /api/spartans/search with Basic auth")
    @Test
    public void testSearchAndExtractData() {

        // search for nameContains : a , gender Female
        // verify status code is 200
        // check the size of result is Some hardcoded number
        // verify all names from the result contains a
        // verify all gender is Female only
        // do it in the chain

         given()
                .log().all()
                .auth().basic("admin", "admin")
                .queryParam("nameContains", "a")
                .queryParam("gender", "Female").
                        when()
                .get("/spartans/search").
                        then()
                .log().all()
                .assertThat()
                .statusCode(is(200))
                 .body("numberOfElements",is(92))
                 .body("content",hasSize(92))//extract entire content size
                 .body("content.name",everyItem(containsStringIgnoringCase("a")))

                ;


    }
}
