package day05_HamcrestCollectionSupport;

import org.junit.jupiter.api.* ;
import org.junit.jupiter.api.DisplayName;

// by default the test is running on "alphabetical order"
// or the test method name

//@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //if we use static import, we dont use MethodOrderer.
//@TestMethodOrder(OrderAnnotation.class)//we provide ordering mechanisim using @TestMethodOrder
//@TestMethodOrder(Random.class)
//@TestMethodOrder(MethodName.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)//will order by DisplayName
public class TestOrderingInJunit5_4 {

    @Order(3)
    @DisplayName("3. Test A Method")
    @Test
    public void testA(){
        System.out.println("running test A");
    }

    @Order(1)
    @DisplayName("1. Test C Method")
    @Test
    public void testC(){
        System.out.println("running test C");
    }

    @Order(2)
    @DisplayName("4. Test D Method")
    @Test
    public void testD(){
        System.out.println("running test D");
    }

    @Order(4)
    @DisplayName("2. Test B Method")
    @Test
    public void testB(){
        System.out.println("running test B");
    }


//    running test C
//    running test B
//    running test A
//    running test D

}
