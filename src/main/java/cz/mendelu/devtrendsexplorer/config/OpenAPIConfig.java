package cz.mendelu.devtrendsexplorer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    String authServerUrl;

    private static final String OAUTH_SCHEME_NAME = "keycloak_security_schema";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme())
                )
                .addSecurityItem(
                        new SecurityRequirement().addList(OAUTH_SCHEME_NAME)
                )
                .info(new Info()
                        .title("DevTrends Explorer API")
                        .description("API dokumentace pro analytický nástroj GitHub repozitářů.")
                        .version("1.0")
                );
    }

    private SecurityScheme createOAuthScheme() {
        var flow = new OAuthFlow()
                .tokenUrl(authServerUrl + "/protocol/openid-connect/token");
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().password(flow));
    }
}