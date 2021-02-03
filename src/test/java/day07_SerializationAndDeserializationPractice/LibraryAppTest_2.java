package day07_SerializationAndDeserializationPractice;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pojo.BookCategory;
import testbase.library_TestBase;


import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

// we want to be able to name the fields any name we want
// rather than being limited to use same name as json object key
// but we need to tell Jackson data-bind
// which json key map to which pojo class field
// we use annotation @JsonProperties for this

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LibraryAppTest_2 extends library_TestBase {

    private static String myToken ;
    @DisplayName("1.Testing POST /login Endpoint")
    @Test
    public void testLogin() {
        /*
        Librarian1  email	librarian69@library
        Librarian1  password		KNPXrm3S
         */

         myToken = given()
                .log().all()
                .contentType(ContentType.URLENC)
                .formParam("email", "librarian69@library")
                .formParam("password", "KNPXrm3S").
                        when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Disabled
    @DisplayName("2.Testing GET /dashboard_stats Endpoint")
    @Test
    public void testzDashboard_stats() {
//      this is how we provide header .header("headerName","headerValue")
        given()
                .log().all()
                .header("x-library-token",myToken).
                when()
                .get("/dashboard_stats").
                then()
                .log().all()
                .assertThat()
                .statusCode( is(200) )
                .contentType(ContentType.JSON)
        ;

    }


    @DisplayName("3.Save the result of Get Dashboard Stat as Map Object")
    @Test
    public void testGetDashBoardStatAsMap(){
//        {
//            "book_count": "1162",
//             "borrowed_books": "650",
//             "users": "7411"
//        }

        Response response = given()
                .log().all()
                .header("x-library-token", myToken).
                        when()
                .get("/dashboard_stats").prettyPeek();

        JsonPath jp = response.jsonPath();
        Map<String, Object> responseJsonAsMap = jp.getMap("");//"" represents the whole String
        System.out.println("responseJsonAsMap = " + responseJsonAsMap);
        Object book_count = responseJsonAsMap.get("book_count");
        System.out.println("book_count = " + book_count);//1543
        //responseJsonAsMap = {book_count=1543, borrowed_books=805, users=7754}

    }

    @DisplayName("4. Save /get_book_categories response as POJO")
    @Test
    public void testGetBookCategoriesAsPOJO(){

        JsonPath jp = given()
                .log().all()
                .header("x-library-token", myToken).
                        when()
                .get("/get_book_categories").prettyPeek()//right after i send a request, this is where i get response object, i will print out same response object to me
                .jsonPath();
      //getting all book categories response as List<BookCategory> Object
        List<BookCategory> allBookCategories = jp.getList("", BookCategory.class);
        allBookCategories.forEach(System.out::println);

        BookCategory firstBookCategory = allBookCategories.get(1);
        System.out.println("firstBookCategory = " + firstBookCategory);

        String firstCategory_id = firstBookCategory.getCategory_id();
        System.out.println("firstCategory_id = " + firstCategory_id);
        System.out.println("=====================================");

        List<Integer> id = jp.getList("id");
        System.out.println("id = " + id);

        List<String> names = jp.getList("name");
        System.out.println("names = " + names);

        System.out.println("=====================================");
        // without using above List
        // use jsonPath to get single object which is number 5 item in jsonArray
        // and save it as single BookCategory Object
        BookCategory num5Object = jp.getObject("[4]", BookCategory.class);
        System.out.println("num5Object = " + num5Object);

        String Name4th = jp.getString("name[3]");
        System.out.println("Name4th = " + Name4th);

    }


}
