package cz.mendelu.devtrendsexplorer.utils;

import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.restassured.RestAssured.given;

@Service
public class AuthHelper {

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    public String login(String username, String password) {
        return given()
                .contentType(ContentType.URLENC)
                .formParam("username", username)
                .formParam("password", password)
                .formParam("grant_type", "password")
                .formParam("client_id", clientId)
        .when()
                .post(tokenUri)
        .then()
                .statusCode(200)
        .extract()
                .path("access_token");
    }

    public String login(String username) {
        return login(username, "password");
    }
}