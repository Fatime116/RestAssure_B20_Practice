package Day11_XML_Path;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Test_XML_ElementAttribute {

    public static String firstGuyDriverId;
    @DisplayName("Test omdbapi xml response and movie info")
    @Test
    public void testMovieAttribute(){

        //search for your favorite movie
        //assert the movie info according to your expected result
        ////http://www.omdbapi.com/?t=Wonder Woman 1984&apiKey=5b5d0fe8&r=xml
      XmlPath xp =   given()
                .baseUri("http://www.omdbapi.com/")
                .queryParam("t","Wonder Woman 1984")
                .queryParam("apiKey","5b5d0fe8")
                .queryParam("r","xml")
                .when()
                .get().prettyPeek()
              .then()
              .statusCode(is(200))
              .body("root.movie.@title",is("Wonder Woman 1984"))
              //because in xml, you get everything in String,
              //if you clearly called the getInt() from xmlPath, then you will get int, because you asked for int, and it will return int
              .body("root.movie.@year.toInteger()",is(2020))//treat number as Number using toInteger() from groovy
              .extract()
                .xmlPath();
                //not element value
                //we are trying to get element attribute value

        /*
        only 2 elements, root and movie,   the rest is all attribute
        <root response="True">
          <movie title="Wonder Woman 1984" year="2020" rated="PG-13" released="25 Dec 2020" runtime="151 min" genre="Action, Adventure, Fantasy" director="Patty Jenkins" writer="Patty Jenkins (story by), Geoff Johns (story by), Patty Jenkins (screenplay by), Geoff Johns (screenplay by), Dave Callaham (screenplay by), William Moulton Marston (based on characters from DC Wonder Woman created by)" actors="Gal Gadot, Chris Pine, Kristen Wiig, Pedro Pascal" plot="Diana must contend with a work colleague and businessman, whose desire for extreme wealth sends the world down a path of destruction, after an ancient artifact that grants wishes goes missing." language="English" country="USA, UK, Spain" awards="5 nominations." poster="https://m.media-amazon.com/images/M/MV5BNWY2NWE0NWEtZGUwMC00NWMwLTkyNzUtNmIxMmIyYzA0MjNiXkEyXkFqcGdeQXVyMTA2OTQ3MTUy._V1_SX300.jpg" metascore="60" imdbRating="5.5" imdbVotes="129,163" imdbID="tt7126948" type="movie"/>
        </root>
         */

        System.out.println(xp.getString("root.movie")); // you get nothing because you are asking for the value inside the opening and closing tag

        // we want to get title attribute of movie element
        // we use .@attribute name to access the attributes
        String name = xp.getString("root.movie.@title");
        System.out.println("name = " + name);//name attribute

        String rate = xp.getString("root.movie.@rated");
        System.out.println("rate = " + rate);

        int year = xp.getInt("root.movie.@year");//it is going to return int
        System.out.println("year = " + year);

    }

    //http://ergast.com/api/f1/drivers

    @DisplayName("Test Ergast Developer API /drivers endpoint")
    @Test
    public void testDrivers(){

        firstGuyDriverId  = given()
                .baseUri("http://ergast.com/api/f1/drivers")
                .contentType(ContentType.XML)
                .when()
                .get()
                .then()
                 .log().all()
                .statusCode(is(200))
        .body("MRData.DriverTable.Driver[0].@driverId ",is("abate"))
               .body("MRData.DriverTable.Driver[0].GivenName",is("Carlo"))
               .extract()
        .xmlPath()
              // .getString("MRData.DriverTable.Driver[0].FamilyName")//Abate
                .getString("MRData.DriverTable.Driver[0].@driverId")
        ;

       // System.out.println("familyNameFirstGuy = " + familyNameFirstGuy);


      //  String firstGuyDriverID =  xp.getString("MRData.DriverTable.Driver[0].@driverId");//driverId is attribute, so we use @
      //  System.out.println("firstGuyDriverID = " + firstGuyDriverID);

        //both same
        //MRData.DriverTable.Driver.GivenName[0]
        //MRData.DriverTable.Driver[0].GivenName

     //   String firstGuyGivenName=  xp.getString("MRData.DriverTable.Driver[0].GivenName");//GivenName is element
    //   System.out.println("firstGuyGivenName = " + firstGuyGivenName);

    }

    // Send a request to GET /drivers/:driverId endpoint using above driver id
    @DisplayName("Test Ergast Developer API /drivers/:driverId endpoint")
    @Test
    public void testSingleDrivers() {

        given()
                .baseUri("http://ergast.com")
                .basePath("/api/f1")
                .pathParam("driverId",firstGuyDriverId)//since it accepts String
                .accept(ContentType.XML)
                .when()
                .get("/drivers/{driverId}")
                .then()
                .statusCode(is(200))
                .body("MRData.DriverTable.Driver.GivenName",is("Carlo"));

    }


}
