package cz.mendelu.devtrendsexplorer.domain.gitrepository;

import cz.mendelu.devtrendsexplorer.utils.AuthHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/test-data/base-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GithubRepositoryIntegrationTest {

    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @Autowired
    private AuthHelper authHelper;

    private String token;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        this.token = authHelper.login("testuser");
    }

    @Test
    public void testGetAllRepositories_Success() {
        given()
                .auth().oauth2(token)
        .when()
                .get("/api/repos")
        .then()
                .statusCode(200)
                .body("items", notNullValue())
                .body("items.size()", greaterThanOrEqualTo(4));
    }

    @Test
    public void testGetRepositoryById_Success() {
        given()
                .auth().oauth2(token)
        .when()
                .get("/api/repos/1")
        .then()
                .statusCode(200)
                .body("content.id", is(1))
                .body("content.name", is("snippet-vault"));
    }

    @Test
    public void testGetRepositoryById_NotFound() {
        given()
                .auth().oauth2(token)
        .when()
                .get("/api/repos/9999")
        .then()
                .statusCode(404);
    }

    @Test
    public void testCreateRepository_Success() {
        String requestBody = """
                {
                    "name": "new-test-repo",
                    "fullName": "user/new-test-repo",
                    "description": "A new repository for testing",
                    "stars": 50,
                    "forks": 5,
                    "issues": 1,
                    "watchers": 10,
                    "hasWiki": true,
                    "domainName": "Web Development",
                    "languageName": "Java",
                    "ownerLogin": "IlliaMelnyk"
                }
                """;

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .post("/api/repos")
        .then()
                .statusCode(201)
                .body("content.id", notNullValue())
                .body("content.name", is("new-test-repo"));
    }

    @Test
    public void testUpdateRepository_Success() {
        String requestBody = """
                {
                    "name": "updated-snippet-vault",
                    "fullName": "IlliaMelnyk/snippet-vault",
                    "description": "Updated description",
                    "stars": 150,
                    "forks": 15,
                    "issues": 0,
                    "watchers": 8,
                    "hasWiki": true,
                    "domainName": "Web Development",
                    "languageName": "TypeScript",
                    "ownerLogin": "IlliaMelnyk"
                }
                """;

        given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .put("/api/repos/1")
        .then()
                .statusCode(200)
                .body("content.name", is("updated-snippet-vault"))
                .body("content.stars", is(150));
    }

    @Test
    public void testDeleteRepository_Success() {
        given()
                .auth().oauth2(token)
        .when()
                .delete("/api/repos/2")
        .then()
                .statusCode(204);

        given()
                .auth().oauth2(token)
        .when()
                .get("/api/repos/2")
        .then()
                .statusCode(404);
    }

    @Test
    public void testGetAllRepositories_Unauthorized() {
        given()
                .auth().none()
        .when()
                .get("/api/repos")
        .then()
                .statusCode(401);
    }
}