package day03_methodChanining_JsonPathIntro;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class Practice {

    //https://api.github.com/users/{username}

//    @DisplayName("gitHub API path param")
//    @Test
//    public void gitHubVerification(){
//
//        given()
//                .accept(ContentType.JSON)
//                .pathParam("username","Fatime116").
//                when()
//                .get("https://api.github.com/users/{username}").
//                then()
//                .log().body()
//                .statusCode(is(200))
//                .contentType(ContentType.JSON)
//                .body("login",is("Fatime116"))
//                .body("id",is(50605806))
//                .body("url",is("https://api.github.com/users/Fatime116"));
//
//
//
//    }


    @BeforeAll
    public static void setUp(){

        baseURI ="http://3.95.214.153:8000";
        basePath="/api";
    }

    @DisplayName("Get all Spartan extracting data from Json Array response")
    @Test
    public void allSpartansPayload(){

        JsonPath jp = given()
                .accept(ContentType.JSON)
                .when()
                .get("/spartans")
                .prettyPeek()
                .jsonPath();

        int firstID = jp.getInt("id[0]");
        System.out.println("firstID = " + firstID);

        String firstName = jp.getString("name[0]");
        System.out.println("firstName = " + firstName);

        List<String> allNames = jp.getList("name");
        System.out.println("allNames : " + allNames);

        List<String> allGenders = jp.getList("gender");
        System.out.println("allGenders : " + allGenders);

        List<Long> allPhones = jp.getList("phone");
        System.out.println("allPhones : " + allPhones);

    }

    @DisplayName("Testing /spartans/search endpoint and extracting data")
    @Test
    public void searchSpartan(){

        JsonPath jp = given()
                .accept(ContentType.JSON)
                .queryParam("nameContains","f")
                .queryParam("gender","Female").
                        when()
                .get("/spartans/search")
                .prettyPeek()
                .jsonPath();

        int firstObjectID = jp.getInt("content.id[0]");
        System.out.println("firstObjectID = " + firstObjectID);

        String secondObjectName = jp.getString("content.name[1]");
        System.out.println("secondObjectName = " + secondObjectName);

        long thirdObjectPhone = jp.getLong("content.phone[2]");
        System.out.println("thirdObjectPhone = " + thirdObjectPhone);

        List<Integer> allIDs = jp.getList("content.id");
        System.out.println("allIDs = " + allIDs);

        List<String> allNames =jp.getList("content.name");
        System.out.println("allNames = " + allNames);

        List<String> allGenders = jp.getList("content.gender");
        System.out.println("allGenders = " + allGenders);

    }

}
