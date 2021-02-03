package day01_Junit_Hamcrest_Intro;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Day1 Hello Test")
public class HelloTest {
//Junit5 annotations:
    //@BeforeAll , @AfterAll , @BeforeEach , @AfterEach
    //@BeforeAll : run only once, and its gonna be static, run before everything else
    //@DisplayName: to name my each test, we can give custom name on top of each test class or for each test
    //@Disabled: if we add this annotation, it wont run
    //since we used  static import, we dont need to call the method through class name

    @BeforeAll
    public static void setUp(){
        System.out.println("BeforeAll is running");
    }

    @AfterAll
    public static void tearDown(){
        System.out.println("AfterAll is running");
    }

    //run before each test
    @BeforeEach
    public void setUpTest(){
        System.out.println("@BeforeEach is running");
    }
    @AfterEach
    public void tearDownTest(){
        System.out.println("@AfterEach is running");
    }

    @DisplayName("Test 1 + 3 =4")
    @Test
    public void test(){
        System.out.println("Test 1 is running");
    Assertions.assertEquals(4,1+3);
}
   @Disabled
   @DisplayName("Test 3 * 4 =12")
    @Test
    public void test2(){
    System.out.println("Test 2 is running");
    //since we are using static import, we dont need to call the method through class name
    assertEquals(12,4*3);
}

}
