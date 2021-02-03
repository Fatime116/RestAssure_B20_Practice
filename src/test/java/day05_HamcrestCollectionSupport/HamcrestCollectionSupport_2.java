package day05_HamcrestCollectionSupport;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.* ;
public class HamcrestCollectionSupport_2 {

    @Test
    public void testList(){
        List<Integer> numList = Arrays.asList(2,3,3,7,2,2,2);

        assertThat(numList,hasSize(7));

        assertThat(numList,hasItem(2));

        assertThat(numList,hasItems(2,3));

        // assert that everyItems in the list are more than 1
        assertThat(numList,everyItem(greaterThan(1)));

        assertThat(numList, everyItem( is( greaterThan(1) ) )  );



        List<String> allNames = Arrays.asList("Rory Hogan", "Mariana","Olivia","Gulbadan","Ayxamgul","Kareem","Virginia","Tahir Zohra");

        assertThat(allNames,hasSize(8));

        assertThat(allNames,hasItems("Mariana"));

        assertThat(allNames,everyItem(containsString("a")));

        // check every items has letter a in case insensitive manner
        assertThat(allNames , everyItem(   containsStringIgnoringCase("a")   )     );

        // how to do and or in hamcrest syntax
        // allOf --> and logic , all of the matchers should match or it fails
        assertThat("Murat Degirmenci", allOf(startsWith("Mu"), containsString("men"))) ;
        //i can combine two conditions in one

        //  anyOf -->> or logic  as long as one matcher match it will pass
        assertThat("Ramazan Alic" , anyOf(  is("Ramazan") ,  endsWith("ic") )   ) ;
    }
}
