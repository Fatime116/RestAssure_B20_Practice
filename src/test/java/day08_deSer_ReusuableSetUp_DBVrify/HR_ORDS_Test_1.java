package day08_deSer_ReusuableSetUp_DBVrify;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pojo.Country;
import testbase.HR_ORDS_TestBase;


import java.util.List;

import static io.restassured.RestAssured.*;

public class HR_ORDS_Test_1 extends HR_ORDS_TestBase {

    //http://54.90.101.103:1000/ords/hr/regions/3
    @DisplayName("Test GET /countries/{country_id} to POJO")
    @Test
    public void testSingleCountryResponseToPOJO() {

//        Response response = given()
//                .pathParam("country_id","AR").
//                when()
//                .get("/countries/{country_id}");


       Response response = get("/countries/{country_id}","AR").prettyPeek();//another way of providing path parameter
       Country argentina = response.as( Country.class ); //if dont provide any json path to get specific field, As is better
        System.out.println("argentina = " + argentina);
//argentina = Country{country_id='AR', country_name='Argentina', region_id=2}

        Country argentina2 = response.jsonPath().getObject("", Country.class);
        System.out.println("argentina with Json path = " + argentina2);
//  argentina with Json path = Country{country_id='AR', country_name='Argentina', region_id=2}

        //from Country class object, we just getter method to get single region_id
        int region_id = argentina2.getRegion_id();//using getter method, to get single field from object of region class
        System.out.println("region_id = " + region_id);

        String country_name = argentina2.getCountry_name();
        System.out.println("country_name = " + country_name);

        String country_id = argentina2.getCountry_id();
        System.out.println("country_id = " + country_id);

    }

    @DisplayName("Test GET /countries to List of POJO")
    @Test
    public void testAllCountriesResponseToListOfPOJO(){


        Response response = get("/countries").prettyPeek();
        //convert what we got in response as json to POJO object
        List<Country> allCountries = response.jsonPath().getList("items",Country.class);
        allCountries.forEach(System.out::println);
       Country id_0_AR =  allCountries.get(0);//first country 'AR'
        System.out.println("id_2 = " + id_0_AR);


    }
    }
