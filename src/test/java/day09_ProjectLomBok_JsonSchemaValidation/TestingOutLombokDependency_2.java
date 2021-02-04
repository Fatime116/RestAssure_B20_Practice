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
        List<Department> allDeps = jp.getList("items.findAll {it.manager_id > 0}",Department.class);//all fields as Department class
        allDeps.forEach(System.out::println);

        // what if I just wanted to get List<String> to store DepartmentName
        List<String> depNames = jp.getList("items.department_name");//giving all the department name
        System.out.println("depNames = " + depNames);

        // -->> items.department_name (all)
        // -->> items.findAll {it.manager_id>0 }.department_name (filtered for manager_id more than 0)
        //first filter manager_id >0 , then filter out all department_id
        //we dont need to removeIf method
        List<String > depNamesFiltered = jp.getList("items.findAll{it.manager_id >0}.department_name");
        System.out.println("depNamesFiltered = " + depNamesFiltered);

        // Get all departments ID if its more than 70
        //it represents department_id
       // List<Integer>  allDepsIDs = jp.getList("items.findAll{it.department_id>70}.department_id");//same
        List<Integer>  allDepsIDs1 = jp.getList("items.department_id.findAll{it>70}");
        System.out.println("allDepsIDs1 = " + allDepsIDs1);
     //   System.out.println("allDepsIDs = " + allDepsIDs);


        List<Integer> filteredDeptID70To100 = jp.getList("items.department_id.findAll{it>=70 && it<=100}");
       // List<Integer> filteredDeptID70To100 =jp.getList("items.findAll{it.department_id >=70 &&it.department_id<=100 }.department_id");//same result
                System.out.println("filteredDeptID70To100 = " + filteredDeptID70To100);

       //at the end will return filtered department_name
        List<String> depNameFiltered = jp.getList("items.findAll{it.department_id>70 & it.manager_id>114}.department_name");
        System.out.println("depIdFiltered = " + depNameFiltered);//depNameFiltered = [Sales, Accounting]

        //get deppartment 10, we get single object
      String dept10 = jp.getString("items.find{it.department_id==10}.department_name");
        System.out.println("dept10 = " + dept10);

        //give me the department name if manager is is 114
        String dept_114 = jp.getString("items.find{it.manager_id==114}.department_name");
        System.out.println("dept_114 = " + dept_114);//Purchasing

      //sum of whole department id
        int sumOfAllDepIDs = jp.getInt("items.department_id.sum()");


        System.out.println("sumOfAllDepIDs = " + sumOfAllDepIDs);//3017

        int minOfDepIDs = jp.getInt("items.department_id.min()");
        System.out.println("sumOfMaxDepIDs = " +minOfDepIDs);//10

        int maxOfDepIDs = jp.getInt("items.department_id.max()");
        System.out.println("maxOfDepIDs = " + maxOfDepIDs);//240

        System.out.println("jp.getInt(\"items.department_id[4]\") = " + jp.getInt("items.department_id[4]"));//40

        System.out.println("last department id " + jp.getInt("items.department_id[-1]"));//240

        System.out.println("department_id 70-90" + jp.getList("items.department_id.findAll{it>=70 && it<=100}"));



        //print from index 7 till index 10 dep_id
        System.out.println("jp.getInt(\"items.department_id[7..10]\") = " + jp.getList("items.department_id[7..10]"));//[70, 80, 90, 100]
    }
}
