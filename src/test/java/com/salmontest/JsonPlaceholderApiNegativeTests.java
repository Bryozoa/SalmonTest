package com.salmontest;

import com.salmontest.model.Post;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.internal.shadowed.jackson.core.type.TypeReference;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Epic("JsonPlaceholder API Tests")
@Feature("CRUD Operations on Posts Negative")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceholderApiNegativeTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    @Order(1)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verify fetching a post with an invalid ID returns 404 Not Found")
    public void testGetPostWithInvalidId() {
        int invalidPostId = 9999;  // A post ID that does not exist

        given()
                .log().all()
                .pathParam("postId", invalidPostId)
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(404)
                .body(is(equalTo("{}")));  // Expecting no body content
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verify fetching a post with a non-numeric ID returns 404 Not Found")
    public void testGetPostWithNonNumericId() {
        String nonNumericPostId = "invalid";

        given()
                .pathParam("postId", nonNumericPostId)
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(404)
                .body(is(equalTo("{}")));  // Expecting no body content
    }

    @Test
    @Order(3)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verify response when sending an invalid query parameter")
    public void testInvalidQueryParamResponse() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        String responseBody = given()
                .queryParam("invalidParam", "someValue")
                .when()
                .get("/posts")
                .then()
                .statusCode(200)  // Expecting 200 OK
                .contentType(ContentType.JSON)
                .extract().asString();

        List<Post> posts = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        assertThat(posts.size(), equalTo(100));

        assertThat(posts.getFirst().getTitle(), equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"));
    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verify POST with missing required fields returns 201")
    public void testCreatePostWithMissingFields() {
        String incompletePostData = "{ \"title\": \"New Post\" }";  // Missing 'body' and 'userId'

        given()
                .contentType(ContentType.JSON)
                .body(incompletePostData)
                .when()
                .post("/posts")
                .then()
                .statusCode(201);
    }

    @Test
    @Order(5)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Verify PUT to an invalid endpoint returns 404 Not Found")
    public void testUpdatePostToInvalidEndpoint() {
        String updatedPostData = "{ \"title\": \"Updated Title\", \"body\": \"Updated Body\", \"userId\": 1 }";

        given()
                .contentType(ContentType.JSON)
                .body(updatedPostData)
                .when()
                .put("/invalidEndpoint")
                .then()
                .statusCode(404)
                .body(is(equalTo("{}")));  // Expecting no body content
    }
}