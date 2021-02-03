package day03_methodChanining_JsonPathIntro;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.* ;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonPathIntro {

    @BeforeAll
    public static void setUpMethod(){
        baseURI =  "http://3.95.214.153:8000";
        basePath = "/api";
    }

    @AfterAll
    public static void tearDownMethod(){
        reset();
    }

    @DisplayName("Extracting data out of Spartan Json Object")
    @Test
    public void test1SpartanPayload(){

        // send a request to get 1 spartan
        // by providing path params with valid id
        // save it into Response object
        // NEW : create an object with type JsonPath
        // by calling the method jsonPath() on response object
        // extract id , name , gender , phone
        // and save it into variable of correct type

     Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", 3). //return request specification
                when()
                .get("/spartans/{id}")//the moment you use http method, gives you response
                .prettyPeek();//i want to print and return same response object as Object, so you can continue the chain


          //response.prettyPrint();//print out the body as String, and you cannot chain your method after pretty print,it is End of chain


        //our response is json object
        // extract the information using jsonpath method
        // JsonPath is used to navigate through the json payload
        // and extract the the value according to the valid "jsonpath" provided

        JsonPath jp = response.jsonPath();
        //i want to get my id as int from valid "jsonpath" provided
        int id = jp.getInt("id");
        String name = jp.getString("name");
        String gender = jp.getString("gender");
        long phone = jp.getLong("phone");


        System.out.println("id = " + id);
        System.out.println("phone = " + phone);
    }

    @DisplayName("Extracting data from Json Array Response ")
    @Test
    public void getAllSpartanExtractData(){


         //   Response response = get("/spartans");
        //      JsonPath jp = response.jsonPath();

        JsonPath jp = get("/spartans").prettyPeek().jsonPath();
/*
[
    {
        "id": 3,
        "name": "Fidole",
        "gender": "Male",
        "phone": 6105035233
    },
    {
        "id": 4,
        "name": "Paige",
        "gender": "Female",
        "phone": 3786741487
    },
    {
        "id": 5,
        "name": "Blythe",
        "gender": "Female",
        "phone": 3677539542
    }
 ]
 */
        // get the first json object name field
        String firstObjectName = jp.getString("name[0]");
        System.out.println("firstObjectName = " + firstObjectName);

        // get the first json object phone field(only one single field)
        long firstObjectPhone = jp.getLong("phone[0]");
        System.out.println("firstObjectPhone = " + firstObjectPhone);

        // get the 7th json object gender field from json array
        String seventhObjectGender = jp.getString("gender[6]");
        System.out.println("seventhObjectGender = " + seventhObjectGender);

        String seventhObjectName = jp.getString("name[6]");
        System.out.println("seventhObjectName = " + seventhObjectName);


        // get the last json object name field
        System.out.println("last object's name field " + jp.getString("name[-1]"));

        // getting all the name fields from the jsonArray Response
        // and storing as a list
        List<String>  allTheNames = jp.getList("name");
        System.out.println("allTheNames = " + allTheNames);

        // getting all the phone fields from the jsonArray Response
        // and storing as a list
        List<Long> allThePhones = jp.getList("phone");
        System.out.println("allThePhones = " + allThePhones);

    }

    // send request to this request url
    // http://100.26.101.158:8000/api/spartans/search?nameContains=f&gender=Male
    // get the name of first guy in the result
    // get the phone of 3rd guy in the result
    // get all names , all phones save it as list
    // save the value of field called empty under pageable in the response
    // print it out
    @DisplayName("Testing /spartans/search and extracting data")
    @Test
    public void testSearch(){

        JsonPath jp = given()
                // http://100.26.101.158:8000/api/spartans/search?nameContains=de&gender=Male
                .queryParam("nameContains","f")
                //when we say queryParam, it will automatically add question mark? and & for us
                .queryParam("gender","Male").
           when()
                .get("/spartans/search") // we did not get question mark,this is where we get response object
                .prettyPeek()
                .jsonPath();

        /*
        {
    "content": [
        {
            "id": 3,
            "name": "Fidole",
            "gender": "Male",
            "phone": 6105035233
        },
        {
            "id": 8,
            "name": "Rodolfo",
            "gender": "Male",
            "phone": 9601637574
        },
        {
            "id": 83,
            "name": "Franky",
            "gender": "Male",
            "phone": 7647130621
        }
    ]
         */
        String FirstObjectName = jp.getString("content.name[0]");
        System.out.println("FirstObjectName = " + FirstObjectName);

        long thirdGuyPhone = jp.getLong("content[2].phone");
        System.out.println("thirdGuyPhone = " + thirdGuyPhone);

        boolean sortedvalue = jp.getBoolean("pageable.sort.sorted");
        System.out.println("sortedvalue = " + sortedvalue);

        int totalElements = jp.getInt("totalElements");
        System.out.println("totalElements = " + totalElements);

        //since we have more than one name, we use getList method to get all the names
        List<String> allNames = jp.getList("content.name");
        System.out.println("allNames = " + allNames);

        List<Long> allPhones = jp.getList("content.phone");
        System.out.println("allPhones = " + allPhones);

        System.out.println("value of field empty " + jp.getBoolean("pageable.sort.empty"));//true


    }

}
