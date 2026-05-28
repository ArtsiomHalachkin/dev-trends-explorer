package cz.mendelu.devtrendsexplorer.domain.userwatchlist;

import cz.mendelu.devtrendsexplorer.utils.AuthHelper;
import io.restassured.RestAssured;
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
public class UserWatchlistIntegrationTest {

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
    public void testAddFavorite_Success() {
        given()
                .auth().oauth2(token)
        .when()
                .post("/api/favorites/1")
        .then()
                .statusCode(201)
                .body("content", notNullValue());
    }

    @Test
    public void testAddFavorite_NotFound() {
        given()
                .auth().oauth2(token)
        .when()
                .post("/api/favorites/9999")
        .then()
                .statusCode(404);
    }

    @Test
    public void testGetFavorites_Success() {
        given()
                .auth().oauth2(token)
                .post("/api/favorites/1");

        given()
                .auth().oauth2(token)
        .when()
                .get("/api/favorites")
        .then()
                .statusCode(200)
                .body("items", notNullValue())
                .body("items.size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testRemoveFavorite_Success() {
        given()
                .auth().oauth2(token)
                .post("/api/favorites/2");

        given()
                .auth().oauth2(token)
        .when()
                .delete("/api/favorites/2")
        .then()
                .statusCode(204);
    }

    @Test
    public void testRemoveFavorite_NotFound() {
        given()
                .auth().oauth2(token)
        .when()
                .delete("/api/favorites/9999")
        .then()
                .statusCode(404);
    }

    @Test
    public void testGetFavorites_Unauthorized() {
        given()
                .auth().none()
        .when()
                .get("/api/favorites")
        .then()
                .statusCode(401);
    }
}