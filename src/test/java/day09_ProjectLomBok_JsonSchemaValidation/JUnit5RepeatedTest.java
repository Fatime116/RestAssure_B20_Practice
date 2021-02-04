package day09_ProjectLomBok_JsonSchemaValidation;
import static org.hamcrest.Matchers.*;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
public class JUnit5RepeatedTest {

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
