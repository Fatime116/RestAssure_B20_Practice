package day04_Logging_PostPutPatch;
import org.junit.jupiter.api.*;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.* ;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryAppTest_4 {
  private static String myToken;

   @BeforeAll
    public static void setUp(){
       baseURI="http://library1.cybertekschool.com";
       basePath="/rest/v1";
   }

   @AfterAll
    public static void tearDown(){
       reset();
   }

    @DisplayName("Testing POST /login Endpoint")
    @Order(1)
    @Test
    public void testLogin() {
        /*
        Librarian1  email	librarian69@library
        Librarian1  password		KNPXrm3S
         */

        //•	API Keys
        //A token provided by the api vendor, to identify who you are and track your usage and authorize the requests
        //Usually , it's required to provide the token either in query parameter or header for each request
        //in library app, we used it header : header("x-library-token",myToken)
        myToken = given()
                .log().all()
                .contentType(ContentType.URLENC)//it is Not JSON
                .formParam("email", "librarian69@library")//to fill up body using formParam
                .formParam("password", "KNPXrm3S").
                        when()
                .post("/login").
                        then()
                .log().all()
                .assertThat()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("token", is(not(emptyString())))
                .extract()
                .jsonPath()
                .getString("token");//we just can extract one information
        //we only get limited to get one info

        // How to extract some data out of response object
        // after doing validation in then section extract
        // without breaking the chain -->> use extract() method that return

        System.out.println("token = " + myToken);
    }

    @DisplayName("Testing GET /dashboard_stats Endpoint")//this test depends on first test in order to get Token
    @Order(2)
    @Test
    public void testDashboard_stats(){

//      this is how we provide header .header("headerName","headerValue")
        given()
                .log().all()
                .header("x-library-token",myToken).//provide api key in the custom header
                when()
                .get("/dashboard_stats").
                then()
                .log().all()
                .assertThat()
                .statusCode( is(200) )
                .contentType(ContentType.JSON)
//                .body("book_count",is("1537"))
//                 .body("users",is("7733"))
        ;

    }

    // create a utility class LibraryUtility
    // create a static method called getToken(environment, username, password)


     @DisplayName("Testing GET /get_book_categories Endpoint ")
     @Order(3)
    @Test
    public void test_get_book_categories(){

        given()
                .header("x-library-token",myToken)
                .contentType(ContentType.JSON).
                when()
                .get("get_book_categories").
                then()
                .log().all()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("name[1]",is("Anthology"));
     }


     @DisplayName("Testing get_book_by_id/{id} Endpoint ")
    @Order(4)
    @Test
    public void test_get_book_by_id(){

       given()
               .header("x-library-token",myToken)
               .contentType(ContentType.JSON)
               .pathParam("id",2).
               when()
               .get("get_book_by_id/{id}")
               .then()
               .log().all()
               .contentType(ContentType.JSON)
               .statusCode(is(200))
               .body("id",is("2"))
               .body("year",is("2004"))
       .body("author",is("Philippa Giacubbo"))
       ;

     }

     @DisplayName("Testing /add_book EndPoint")
    @Order(5)
    @Test
    public void test_add_book(){

       given()
               .header("x-library-token",myToken)
               .contentType(ContentType.URLENC)
               .formParam("name","my daughter")
               .formParam("isbn","sdsdd")
               .formParam("year",2016)
               .formParam("author","fatime")
               .formParam("book_category_id",116)
               .formParam("description","my daughter is pretty").
               when()
               .post("/add_book")
               .then()
               .log().body()
               .statusCode(is(200))
               .contentType(ContentType.JSON)
       .body("message",is("The book has been created."))
       ;
     }


}
