package day09_ProjectLomBok_Groovy;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.hamcrest.MatcherAssert.assertThat;
public class JUnit5RepeatedTest_3 {

    @DisplayName("Repeated Test Demo")
    @RepeatedTest(10)
    public void testRepeating() {

        //repeating same set of test for the given number
        //if i want fill up the server with some data using post request.
        Faker faker = new Faker();

        System.out.println(faker.funnyName().name());

        System.out.println(faker.chuckNorris().fact());

    }

}
