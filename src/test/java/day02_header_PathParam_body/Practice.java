package day02_header_PathParam_body;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class Practice {

    //http://100.26.101.158:8000/api//spartans/{id}
//get request


    //       /spartans/{id}
    @BeforeAll
    public static void setUpMethod(){

        baseURI="http://3.95.214.153:8000";
        basePath="/api";
    }

    @DisplayName("Single resources API path param1")
    @Test
    public void singleSpartan1(){

        given()
                .accept(ContentType.JSON)
                .pathParam("id",3).
                when()
                .get("/spartans/{id}").
                then()
                .log().all()
                .statusCode(is(200))
                .contentType(ContentType.JSON);

    }


    @DisplayName("Single resource API path param2")
    @Test

    public void singleSpartan2(){

        given()
                .accept(ContentType.JSON).
                when()
                .get("/spartans/3").
                then()
                .statusCode(is(200))
                .contentType(ContentType.JSON);


    }


    @DisplayName("Single resource API path param3")
    @Test

    public void singleSpartan3(){

        given()
                .accept(ContentType.JSON).
                when()
                .get("/spartans/{id}",3).
                then()
                //.log().body()
                .statusCode(is(200))
                .header("Content-Type","application/json")
                .body("id",is(3))
                .body("name",is("Fidole"))
                .body("gender",is("Male"))
                .body("phone",is(6105035233L));


    }


}
