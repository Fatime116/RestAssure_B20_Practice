package Day11_XML_Path;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import testbase.SpartanAdmin_TestBase;

public class Test_XML_Response extends SpartanAdmin_TestBase {

    //get xml response for GET /spartans
    //https://codebeautify.org/xmlviewer

    @Test
    public void testXML()  {

        XmlPath xp = given()
                .spec(adminReqSpec)
                .accept(ContentType.XML)//what kind of data we want to get
                .when()
                .get("/spartans")//this is only endPoint which sends us XML
                .then()
               .log().body()
                .statusCode(is(200))
                .contentType(ContentType.XML)
                .body("List.item.name[0]",is("Maribeth"))
              //  .body("List.item.id[2]",is("172"))
                .body("List.item.id[2].toInteger()",is(172))//treat Number as Number using groovy
                .extract()
                .xmlPath();//something we can work with xml
        //unlike Json, everything is String in XML format
        /*
         <List>
             <item>
                <id>170</id>
                <name>Maribeth</name>
                <gender>Male</gender>
                <phone>6778792227</phone>
             </item>
         */
          String name = xp.getString("List.item.name[0]");//Case Really Matter, just want to get first item
        //List.item[0].name
        System.out.println("name = " + name);//i am asking tag value/element value

        //get third person id
        String id = xp.getString("List.item.id[2]");  //unlike Json, everything is String in XML format
        System.out.println("id = " + id);

       String lastPhone =  xp.getString("List.item.phone[-1]");
        System.out.println("lastPhone = " + lastPhone);



    }

}
