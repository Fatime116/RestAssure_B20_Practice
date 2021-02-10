package day09_ProjectLomBok_Groovy;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.Country;
import testbase.HR_ORDS_TestBase;
import utility.DB_Utility;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ORDS_API_DB_PracticeTest_1 extends HR_ORDS_TestBase{

    @DisplayName("GET /countries/{country_id} Compare Result with DB")
    @Test
    public void testResponseMatchDatabaseData(){

        //save into as Map, Country POJO class
      String myCountryID = "AR";
        Country arPOJO =    given().log().ifValidationFails()
                .pathParam("country_id" , myCountryID).
                        when()
                .get("/countries/{country_id}").prettyPeek()
                .as(Country.class) ;//dont need call it through json path
        //we dont need anything json path , so it is better to use as method to get pojo
    //arPOJO : single object of country pojo class

        // here the shorter way of above code, without calling specific path parameter
       //  Country arPOJO1 =  get("/countries/{country_id}" , myCountryID ) .as(Country.class) ;

        System.out.println("arPOJO = " + arPOJO);//actual result
      //arPOJO = Country{country_id='AR', country_name='Argentina', region_id=2}
        String query = "SELECT * FROM COUNTRIES WHERE COUNTRY_ID = '" + myCountryID + "'  " ;
        //select * from COUNTRIES where COUNTRY_ID = 'AR';
        System.out.println("query = " + query);
        DB_Utility.runQuery( query ) ;
        Map<String,String> dbResultMap = DB_Utility.getRowMap(1) ;//because AR is in the first row
        System.out.println("dbResultMap = " + dbResultMap);

        assertThat(arPOJO.getCountry_id(),is(dbResultMap.get("COUNTRY_ID")));
        //using getter to get country_id from actual pojo result

        assertThat(arPOJO.getCountry_name(),is(dbResultMap.get("COUNTRY_NAME")));

//                    actual                                           expected
        assertThat(arPOJO.getRegion_id(),equalTo(Integer.parseInt(dbResultMap.get("REGION_ID"))));
             //giving id as int                   convert it int            giving id as String
        //                                         Integer.parseInt("123") ===> 123
        //                                           String                      number


    }

    @DisplayName("GET /countries Capture All CountryID and Compare Result with DB")
    @Test
    public void testResponseAllCountryIDsMatchDatabaseData() {

        JsonPath jp = get("/countries").prettyPeek().
                then()
                .extract()
                .jsonPath();

        List<Country> allCountries = jp.getList("items", Country.class);
        Country country = allCountries.get(0);
        String first_country_id_1 = country.getCountry_id();
        System.out.println("country_id = " + first_country_id_1);

        //or we can do this way:
        String firstCountry_ID = jp.getString("items.country_id[0]");
        System.out.println("firstCountry_ID = " + firstCountry_ID);

        //all countries id  from json path
         List<String> allCountryID = jp.getList("items.country_id");//all countries ID
          allCountryID.forEach(System.out::println);

       DB_Utility.runQuery("SELECT * FROM COUNTRIES");
      List<String> expectedListFromDB = DB_Utility.getColumnDataAsList("COUNTRY_ID");//all countries id from DB utility
     expectedListFromDB.forEach(System.out::println);

        // assert both list has same information
       assertThat(allCountryID, equalTo(expectedListFromDB)  );
        //          List                  List

    }

}
