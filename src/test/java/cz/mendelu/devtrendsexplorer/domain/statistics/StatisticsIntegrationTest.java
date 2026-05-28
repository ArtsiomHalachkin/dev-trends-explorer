package cz.mendelu.devtrendsexplorer.domain.statistics;

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
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/test-data/base-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class StatisticsIntegrationTest {

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
    public void testGetStatistics_Success() {
        given()
                .auth().oauth2(token)
                .when()
                .get("/api/repos/stats")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    public void testGetTopStars_Success() {
        given()
                .auth().oauth2(token)
                .when()
                .get("/api/repos/stats/top-stars")
                .then()
                .statusCode(200)
                .body("items", notNullValue());
    }

    @Test
    public void testGetHealthScores_Success() {
        given()
                .auth().oauth2(token)
                .when()
                .get("/api/repos/stats/health")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    public void testGetLanguageDominance_Success() {
        given()
                .auth().oauth2(token)
                .when()
                .get("/api/repos/stats/languages")
                .then()
                .statusCode(200)
                .body("content", notNullValue());
    }

    @Test
    public void testGetStatistics_Unauthorized() {
        given()
                .auth().none()
                .when()
                .get("/api/repos/stats")
                .then()
                .statusCode(401);
    }
}