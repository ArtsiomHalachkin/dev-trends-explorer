package cz.mendelu.devtrendsexplorer.domain.performance;

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

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/test-data/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/test-data/base-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ApiPerformanceTest {

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
    public void testGetAllRepositories_PerformanceCheck() {
        given()
                .auth().oauth2(token)
        .when()
                .get("/api/repos")
        .then()
                .statusCode(200)
                .time(lessThan(600L), TimeUnit.MILLISECONDS);
    }

    @Test
    public void testGetAnalysisHistory_PerformanceCheck() {
        given()
                .auth().oauth2(token)
        .when()
                .get("/api/analysis/history")
        .then()
                .statusCode(200)
                .time(lessThan(500L), TimeUnit.MILLISECONDS);
    }

    @Test
    public void testPublicDomains_PerformanceCheck() {
        given()
        .when()
                .get("/api/domains")
        .then()
                .statusCode(200)
                .time(lessThan(300L), TimeUnit.MILLISECONDS);
    }
}