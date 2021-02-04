package day09_ProjectLomBok_JsonSchemaValidation;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.Country;
import pojo.Department;
import sun.jvm.hotspot.utilities.Assert;
import testbase.HR_ORDS_TestBase;
import utility.DB_Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class practice extends HR_ORDS_TestBase {

    @DisplayName("GET /countries/{country_id} Compare result with DB")
    @Test
    public void testResponseMatchDataBaseData(){

        String my_ID = "AR";

   Country arPOJO = given().log().uri()
           .pathParam("country_id",my_ID)
           .when()
           .get("/countries/{country_id}").prettyPeek()//I dont have then() section, so i cannot log().all()
           .as(Country.class);//save the single country object without using jsonpath()

        //without calling path params
       // Country asPOJO_withoutSpecificPath = when().get("/countries/{country_id}", my_ID).as(Country.class);
        System.out.println("arPOJO = " + arPOJO);
        String country_id = arPOJO.getCountry_id();//using getter to get country id

        String arQuery = "select * from COUNTRIES where COUNTRY_ID='" + my_ID + "'";
        System.out.println("arQuery = " + arQuery);

        DB_Utility.runQuery(arQuery);
        Map<String,String> dbResultMap = DB_Utility.getRowMap(1);//we are at the first row

        //start validating
        assertThat(country_id,is(dbResultMap.get("COUNTRY_ID")));

        assertThat(arPOJO.getCountry_name(),is(dbResultMap.get("COUNTRY_NAME")));

        assertThat(arPOJO.getRegion_id(),equalTo(Integer.parseInt(dbResultMap.get("REGION_ID"))));//
        //dbResultMap.get("REGION_ID") return us String, we just truned to into number
    }

    @DisplayName("GET /countries Capture All CountryID and Compare Result with DB")
    @Test
    public void testResponseAllCountryIDsMatchDatabaseData(){

       List<String> all_IDs =  given()
                .log().all()
                .when()
                .get("/countries")
                .jsonPath()
                .getList("items.country_id");
               // i just want to get all country id as a String, not all Country class object
               //cant use As()
            //List<Country> allCountries = response.jsonPath().getList("items",Country.class);
           //here im getting everything from jsonPath

        all_IDs.forEach(System.out::println);
        DB_Utility.runQuery("SELECT * FROM COUNTRIES");
        List<String> expectedListFromDB = DB_Utility.getColumnDataAsList("COUNTRY_ID");
        expectedListFromDB.forEach(System.out::println);

        assertThat(all_IDs,equalTo(expectedListFromDB));
    }

    @DisplayName("GET /departments and save List of POJO")
    @Test
    public void testDepartmentJsonArrayToListOfPojo(){

        List<Department> allDepartment =given()
              .log().all()
              .when()
              .get("/departments")
              .jsonPath()
              .getList("items",Department.class);//we cannot use As here, we cannot directly go

        allDepartment.forEach(System.out::println);
        System.out.println("=================================================================");
        List<Department>allDepartmentCopy = new ArrayList<>(allDepartment);
        allDepartmentCopy.removeIf(each->each.getManager_id()==0);//remove if it is 0
        allDepartmentCopy.forEach(System.out::println);
    }

    @DisplayName("GET /departments and filter the result with JsonPath groovy")
    @Test
    public void testFilterResultWithGroovy() {

        JsonPath jp = get("/departments")
                .jsonPath();

        List<Department> allDeps=jp.getList("items.findAll{it.manager_id >0}",Department.class);//first actual jsonpath, and groovy method
        //find the object that has manager id not equal to 0
        //it is each and every item
        //save it as List of Department POJO class
        allDeps.forEach(System.out::println);


        List<Department> allDeps114=jp.getList("items.findAll{it.manager_id == 114}",Department.class);

        List<String> depNames = jp.getList("items.department_name");
        System.out.println("depNames = " + depNames);

        //first filter manager_id >0 , then all department_name
        List<String> depNamesFiltered = jp.getList("items.findAll{it.manager_id>0}.department_name");
        System.out.println("depNamesFiltered = " + depNamesFiltered);

        //if i write items.department_name, i will not have access to the other parts of json object
        //findAll should start in between,
        //should specify what it means : it.department_id
        List<String> depNameFiltered = jp.getList("items.findAll{it.department_id>70 & it.manager_id>114}.department_name");
        System.out.println("depIdFiltered = " + depNameFiltered);//depNameFiltered = [Sales, Accounting]
    }
}
