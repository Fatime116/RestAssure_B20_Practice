package day09_ProjectLomBok_JsonSchemaValidation;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testbase.HR_ORDS_TestBase;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;

import pojo.Department;

public class TestingOutLombokDependency_2 extends HR_ORDS_TestBase {
//Lombok is  java library,that automatically plugs into your editor and build tools, spicing up your java.
//Never write another getter or equals method again, with one annotation your class has a fully featured builder,
// Automate your logging variables, and much more.

    @Test
    public  void testDepartmentPOJO() {
            Department d = new Department();
            d.setDepartment_id(100);
            System.out.println("d.getDepartment_id() = " + d.getDepartment_id());

            Department d2 = new Department(100,"ABC",11,3);
            System.out.println("d2 = " + d2);
        }

    @DisplayName("GET /departments and save List of POJO")
    @Test
    public void testDepartmentJsonArrayToListOfPojo(){
        List<Department> allDeps = get("/departments").prettyPeek()
                                     .jsonPath().getList("items",Department.class);//specify what kind of list you want to get by Department.class
 // because original allDeps is unmodifiable, we copied it in order to use predicate to remove some certain conditions
        //allDeps.forEach(System.out::println);
        // COPY THE CONTENT OF THIS LIST INTO NEW LIST
        // AND ONLY PRINT IF THE DEP MANAGER ID IS NOT NULL

        List<Department> allDepsCopy = new ArrayList<>(allDeps);

        allDepsCopy.removeIf( eachDep -> eachDep.getManager_id()==0 ) ;
        allDepsCopy.forEach(System.out::println);
    }
    @DisplayName("GET /departments and filter the result with JsonPath groovy")
    @Test
    public void testFilterResultWithGroovy() {

        JsonPath jp = get("/departments").jsonPath();
        //instead of using predicate to remove, we use groovy
        List<Department> allDeps = jp.getList("items.findAll {it.manager_id > 0}",Department.class);
        allDeps.forEach(System.out::println);

        // what if I just wanted to get List<String> to store DepartmentName
        List<String> depNames = jp.getList("items.department_name");//giving all the department name
        System.out.println("depNames = " + depNames);

        // -->> items.department_name (all)
        // -->> items.findAll {it.manager_id>0 }.department_name (filtered for manager_id more than 0)
        List<String > depNamesFiltered = jp.getList("items.findAll{it.manager_id >0}.department_name");
        System.out.println("depNamesFiltered = " + depNamesFiltered);

        // Get all departments ID if its more than 70
        List<Integer>  allDepsIDs = jp.getList("items.findAll{it.department_id>70}.department_id");
        System.out.println("allDepsIDs = " + allDepsIDs);
    }
}
