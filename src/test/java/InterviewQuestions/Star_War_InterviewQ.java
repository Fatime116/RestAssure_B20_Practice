package InterviewQuestions;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.util.*;

import  static io.restassured.RestAssured.* ;

public class Star_War_InterviewQ {

    @Test
    public void extractHeight(){

        JsonPath jp = given()
                .contentType(ContentType.JSON).
                when().
                get("https://swapi.dev/api/people").
                  then()
          .extract()
          .jsonPath();


       // List<Integer> allHeights = jp.getList("results.height", Integer.class);
        List<Integer> allHeights = jp.getList("results.height",Integer.class);

        System.out.println("allHeights = " + allHeights);

        int sum = 0;
        for(Integer each : allHeights){
            sum +=each;
        }
        double avg = sum/allHeights.size();
        System.out.println(avg);

    }

    @DisplayName("Average height with Groovy ")
    @Test
    public void testFindAverageHeightOfPeople2() {

        JsonPath jp = given()
                .contentType(ContentType.JSON).
                        when()
                .get("https://swapi.dev/api/people").
                        then()
                .extract()
                .jsonPath();

        List<String> allHairColor = jp.getList("results.hair_color");
        int size = allHairColor.size();
//
//        for(String eachHair : allHairColor){
//
//           if(eachHair.equals("n/a") || eachHair.equals("none")){
//               continue;
//            }
//            System.out.print(eachHair + ", ");
//
//
//        }
        allHairColor.removeIf(p -> p.equals("n/a") || p.equals("none"));
        HashSet<String> allUniqueHair = new HashSet<>(allHairColor);//remove duplicated value
        System.out.println(allUniqueHair);




    }
    @Test
    public void test1(){

        Response response = get("https://swapi.dev/api/people");

        Set<String> setOfPeopleHairColor =  new TreeSet<>();
        System.out.println(response.jsonPath().getList("results.hair_color"));
        int size = response.jsonPath().getList("results.hair_color").size();
        for (int i = 0; i < size; i++) {
            String root = response.body().jsonPath().getString("results["+i+"].hair_color");
            if ((root.contains(","))){
                setOfPeopleHairColor.add(root.replace(", ","-"));
            }else if (root.contains("n/a") || root.contains("none")) {

            }else{
                setOfPeopleHairColor.add(root);
            }
        }

        System.out.println("setOfPeopleHairColor = " + setOfPeopleHairColor);

    }
}
