package com.salmontest;

import com.salmontest.model.Post;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Epic("JsonPlaceholder API Tests")
@Feature("CRUD Operations on Posts")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonPlaceholderApiTests {

    private static final int TEST_USER_ID = 1;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private final int PostId = 13;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Create a new post with title: {title}, body: {body}, userId: {userId}")
    private Response createPost() {
        Post newPost = new Post("New Post Title", "New Post Body", JsonPlaceholderApiTests.TEST_USER_ID);
        return given()
                .contentType(ContentType.JSON)
                .body(newPost)
                .when()
                .post("/posts");
    }

    @Test
    @Order(1)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Post")
    @Description("Test to create a new post and verify the response")
    public void testCreatePost() {
        Response response = createPost();

        assertAll("Validate post creation",
                () -> assertEquals(201, response.getStatusCode(), "Unexpected status code"),
                () -> response.jsonPath().getInt("id"),
                () -> assertEquals("New Post Title", response.jsonPath().getString("title"), "Title mismatch"),
                () -> assertEquals(TEST_USER_ID, response.jsonPath().getInt("userId"), "User ID mismatch")
        );

        // Cleanup: Delete the created post (Won't work with this API though. Just showing off >_>)
        int newPostId = response.jsonPath().getInt("id");
        deletePost(newPostId);
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.NORMAL)
    @Story("Read Post")
    @Description("Test to retrieve a post by ID and verify the response")
    public void testGetPostById() {
        Response response = given()
                .pathParam("id", PostId)
                .when()
                .get("/posts/{id}");

        assertEquals(200, response.getStatusCode(), "Unexpected status code");
    }

    @Test
    @Order(3)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update Post")
    @Description("Test to update an existing post and verify the response")
    public void testUpdatePost() {
        String updatedTitle = "Updated Title";
        String updatedBody = "Updated Body";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(new Post(updatedTitle, updatedBody, TEST_USER_ID))
                .pathParam("id", PostId)
                .when()
                .put("/posts/{id}");

        assertEquals(200, response.getStatusCode(), "Unexpected status code");
    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Delete Post")
    @Description("Test to delete an existing post and verify the response")
    public void testDeletePost() {

        Response response = deletePost(PostId);

        assertEquals(200, response.getStatusCode(), "Failed to delete the post");

    }

    @Step("Delete post with ID: {postId}")
    private Response deletePost(int postId) {
        return given()
                .pathParam("id", postId)
                .when()
                .delete("/posts/{id}");
    }
}