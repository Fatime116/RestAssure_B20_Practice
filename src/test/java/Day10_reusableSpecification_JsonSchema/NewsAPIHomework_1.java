package Day10_reusableSpecification_JsonSchema;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.NewsAPI;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class NewsAPIHomework_1 {

    @DisplayName("Get All Articles author if source id is not null ")
    @Test
    public void testGetAllArticles(){

        //https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY
        JsonPath jp =
                given()
                        .log().uri()
                        .baseUri("http://newsapi.org")
                        .basePath("/v2")
                        //.header("Authorization","Bearer 03e75cdc200c4f27a77f9b1473bbde8d")
                        .queryParam("apiKey","d39d53da33db434791d77f7b58658007")
                        .queryParam("country","us").
                        when()
                        .get("/top-headlines").prettyPeek()
                        .jsonPath();
       //all AUthor with no filter
        List<String> allAuthorWithNoFilter = jp.getList("articles.author");
        System.out.println("allAuthorWithNoFilter = " + allAuthorWithNoFilter);
        System.out.println("allAuthorWithNoFilter.size() = " + allAuthorWithNoFilter.size());

        //print all article authors if source id is not null
        //articles.findAll{it.source.id != null}.author
       List<String> allArticleAuthor =   jp.getList("articles.findAll{it.source.id!=null}.author");
       allArticleAuthor.forEach(System.out::println);
        System.out.println("allArticleAuthor.size() = " + allArticleAuthor.size());

        System.out.println("=======================================");
        //remove any author with null value
        List<String> allAuthorsWithNoNull = jp.getList("articles.findAll{it.source.id !=null && it.author !=null }.author");
        System.out.println("allAuthorsWithNoNull = " + allAuthorsWithNoNull);
        System.out.println("allAuthorsWithNoNull.size() = " + allAuthorsWithNoNull.size());

        System.out.println("=======================================");
        List<NewsAPI> allArticles = jp.getList("articles.findAll{it.source.id !=null && it.author !=null }",NewsAPI.class);
        //articles.findAll{it.source.id !=null && it.author !=null }.author ===>cannot write like this, because in POJO we other fields as well

        allArticles.forEach(System.out::println);
        System.out.println("allArticles.size() = " + allArticles.size());

    }
}
