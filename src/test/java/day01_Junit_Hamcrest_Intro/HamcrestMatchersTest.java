package day01_Junit_Hamcrest_Intro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class HamcrestMatchersTest {


    //Hamcrest library that provides assertion and makes your tests readable with different Matchers
    //if we do method chaining in restAssure, the way it shows in BDD style, whenever it comes to validation part of entire chain,
    //it uses the hamcrest library for assertion when we chain the method
    //if we want to get full power of restAssure, write one statement for one entire test, Hamcrest library will take u far
    //restAssure uses hamcrest in method-chaining, and hamcrest library comes with RestAssure library in pom file, no separate dependencies needed

    //if we dont use that part of restAssure, if we want to do it step by step, we can stick with Junit5


    // Hamcrest assertion library already part of
// our RestAssured Dependency in pom file
// No separate dependency needed
    @DisplayName("Test 1  + 3 is 4")
    @Test
    public void test1(){


        //is,equal,not, called Matcher
        assertThat(1+3,is(4));
        assertThat(1+3,equalTo(4));

        //add some error message if it fails
        assertThat("Wrong result!",1+3,equalTo(5));

        //negate the result
        assertThat(1+3,not(5));
        assertThat(1+3,is(not(5)));
        assertThat(1+3,not(equalTo(5)));

        //less than or greater than
        assertThat(1+3,lessThan(5));
        assertThat(1+3,greaterThan(5));

    }

    @DisplayName("Common Matchers for Strings")
    @Test
    public void testString(){

        String str = "Rest Assure is cool so far";

        // assert the str is "Rest Assured is cool so far"
        assertThat(str, is("Rest Assured is cool so far"));

        // assert the str is "Rest Assured IS COOL so far" in case insensitive manner
        assertThat(str, equalToIgnoringCase("Rest Assured IS COOL so far") );

        // assert the str startWith "Rest"
        assertThat(str, startsWith("Rest") );

        // assert the str endWith "so far"
        assertThat(str , endsWith("so far") );

        // assert the str contains "is cool"
        assertThat(str , containsString("is cool") );

        // assert the str contains "IS COOL" case insensitive manner
        assertThat(str, containsStringIgnoringCase("IS COOL"));

    }

}
