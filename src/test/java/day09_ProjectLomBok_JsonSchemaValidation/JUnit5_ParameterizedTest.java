package day09_ProjectLomBok_JsonSchemaValidation;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JUnit5_ParameterizedTest {

   // Parameterized Test makes it possible to run a test multiple times with different arguments
    //they are declared inside of @ParameterizedTest annotation
    // you must provide external parameter and declare as least one @ValueSource that will provide argument for each invocation
    //then use those arguments in the test method
    //strings={"","","","",""}
    //short,byte,int,float ,String and add s at the end of each data type

    //all the Test method does not have parameters
    @ParameterizedTest
    @ValueSource(ints = {5,6,7,8,1,2,3})
    public void test1(int myNum ){

        //assert 5,6,7,8,9 all less than 10
        //no loop for us, just let JUnit do the thing for us
        System.out.println("myNum = " + myNum);
        assertThat(myNum,is(lessThan(10)));//will not stop other line
      //  assertTrue(myNum<10);//even if one line is not passed, it will not abort my other tests

    }
    //using CSV file as source for parameterized test
    //it goes straightly to the csv file, and read the data

}
