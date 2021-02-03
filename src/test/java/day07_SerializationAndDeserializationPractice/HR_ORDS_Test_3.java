package day07_SerializationAndDeserializationPractice;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pojo.Region;


import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class HR_ORDS_Test_3 {

    //http://54.90.101.103:1000/ords/hr/regions/3


    @BeforeAll
    public static void setUp(){
        baseURI = "http://3.95.171.166:1000";
        basePath = "/ords/hr" ;
    }

    @AfterAll
    public static void tearDown(){
        reset();
    }

    @DisplayName("Testing /regions/{region_id}")
    @Test
    public void testThirdRegionIsAsia(){

        given()
                .log().all()
                .pathParam("region_id", 3).//it does not matter what you call "region_id", you can name it with other name
        when()
                .get("/regions/{region_id}").
       then()
                .log().all()
                .statusCode(is(200))
                .contentType(ContentType.JSON)
                .body("region_name",is("Asia"))
                ;


    }


    @DisplayName("Save GET /regions/{region_id} response as POJO")
    @Test
    public void testSingleRegionToPOJO() {

        Response response = given()
                .log().all()
                .pathParam("region_id", 3).//it does not matter what you call "region_id", you can name it with other name
                when()
                .get("/regions/{region_id}")
//                        .then()
//                .log().all()
//                .statusCode(is(200))
//                .contentType(ContentType.JSON)
//                .body("region_name",is("Asia"))
//                .extract()
//                .jsonPath()
                ;

        JsonPath jp = response.jsonPath();
        Region region3 = jp.getObject("", Region.class);//
        System.out.println("region3 = " + region3);//we only stored what we want to get, we only want to get 2 fields
      //region3 = Region{region_id=3, region_name='Asia'}

        Region region4 =response.as(Region.class);//here we dont need json path
        //if there is no nested object, if we directly can go there, we can use this
        System.out.println("region4 = " + region4);


    }

    @DisplayName("Save GET /regions response as List of POJO")
    @Test
    public void testAllRegionsToListOfPOJO() {


        Response response = get("/regions").prettyPeek();

        JsonPath jp = response.jsonPath();

        List<Region> allRegions = jp.getList("items", Region.class);//all regions
        allRegions.forEach(System.out::println);

    }

    }
