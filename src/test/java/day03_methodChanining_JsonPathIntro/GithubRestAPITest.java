package day03_methodChanining_JsonPathIntro;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;



public class GithubRestAPITest {


    @DisplayName("Test GitHub GET /users/{username}")
    @Test
    public void testGitHub(){

        given()
                .pathParam("username","Fatime116").
        when()
                .get("https://api.github.com/users/{username}").
       then()
                .log().all()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("login",is("Fatime116"));
    }
}
