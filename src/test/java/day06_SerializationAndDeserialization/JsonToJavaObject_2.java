package day06_SerializationAndDeserialization;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import pojo.SpartanRead;
import utility.ConfigurationReader;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.* ;

public class JsonToJavaObject_2 {

    @BeforeAll
    public static void setUp(){
        //RestAssured.filters().add(new AllureRestAssured() ) ;
        baseURI = ConfigurationReader.getProperty("spartan.base_url");
        basePath = "/api" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("Get 1 Data and Save Response Json As Java Map Object")
    @Test
    public void getOneSpartanAndSaveResponseJsonAsMap() {
        Response response = given()
                .auth().basic("admin", "admin")
                .log().all()
                .pathParam("id", 4).
                        when()
                .get("/spartans/{id}").prettyPeek(); //all i need a response object, when i get to when section , it gives me response object

        // if i cannot get then section where i can provide log().all() section, i directly put prettyPeek

        JsonPath jp = response.jsonPath();// i wanna get json path object


        Map<String, Object> responseMap = jp.getMap("");//get all one json object as a whole
        //in stead of extracting id, gender one by one, just get it as Map object to get whole thing
        System.out.println("responseMap = " + responseMap);//responseMap = {id=242, name=Stacia, gender=Female, phone=9529827359}

        /*
        {
    "id": 4,
    "name": "John ABC",
    "gender": "Female",
    "phone": 3786741487
}
         */

   //since we are getting one single object, we used getObject()
        SpartanRead sp = jp.getObject("",SpartanRead.class);//get the single Json object and convert it to the custom Java object ----sp class object
        System.out.println("sp = " + sp);//sp = SpartanRead{id=242, name='Stacia', gender='Female', phone=9529827359}


        /**
         * {
         *     "id": 5,
         *     "name": "Sayeem",
         *     "gender": "Male",
         *     "phone": 1231231230
         * }
         * jsonPath to get whole json object is just empty string ""
         *
         * assume this is a car object
         * {
         *     "make":"Honda"
         *     "color" : "white"
         *     "engine" : {
         *                   "type" : "v8"
         *                   "horsepower" : 350
         *                }
         * }
         * jsonPath for horse power -->> engine.horsepower
         * jsonPath for engine itself -->> engine
         * jsonPath for entire car JsonObject -->> ""
         *
         *
         */
    }

    @DisplayName("Get All Data and Save Response JsonArray As Java Object")
    @Test
    public void getAllSpartanAndSaveResponseJsonAsListOfSpartanReadPOJO(){

        //easy way to get the json:  send a request and get the response, and through that response
        //manipulate the response using json path object, so we can get it as
        //as a map object
        //the object that we can specify like POJO object

        Response response = given().
                auth().basic("admin","admin").
                when().
              get("/spartans").prettyPeek();


        JsonPath jp = response.jsonPath();

        List<SpartanRead> allSpartansPOJO = jp.getList("", SpartanRead.class);
        //im gonna get entire json array converted to the SpartanRead POJO

       // System.out.println("allSpartansPOJO = " + allSpartansPOJO);
        allSpartansPOJO.forEach(System.out::println);


    }


    //Homework
    // Send request to /api/spartans/search endpoint
    // save your jsonArray from search result into
    // List of SpartanRead POJO
    @DisplayName("GET request to /api/spartans/search endpoint")
    @Test
    public void getSearchSpartanAndSaveResponseJsonAsJavaObject() {
        Response response = given().
                auth().basic("admin","admin").
                queryParam("nameContains","de")
                .queryParam("gender","Male").
                when().
                get("/spartans/search")
                .prettyPeek()
                ;

        JsonPath jp = response.jsonPath();
/*
{
    "content": [
        {
            "id": 23,
            "name": "Deda",
            "gender": "Male",
            "phone": 1456545256
        },
        {
            "id": 104,
            "name": "Flldde",
            "gender": "Male",
            "phone": 6105035233
        }
    ],
 */

        //since it is list of POJO objects, we use List<SpartanRead>
        List<SpartanRead> AllSpartanPOJOAfterSearch = jp.getList("content", SpartanRead.class);
        AllSpartanPOJOAfterSearch.forEach(System.out::println);

    }

    }
