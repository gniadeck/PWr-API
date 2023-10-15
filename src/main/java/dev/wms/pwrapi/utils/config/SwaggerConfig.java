package dev.wms.pwrapi.utils.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final BuildProperties buildProperties;
    @Value("${git.commit.id}")
    private String buildCommitId;

    @Bean
    public OpenAPI openApi() {
        String securitySchemeName = "Api-Key";

        return new OpenAPI()
                .info(new Info().title("PWr-API")
                        .description("OpenAPI documentation of PWr-API")
                        .version(buildProperties.getVersion() == null ? "development" : buildProperties.getVersion())
                        .description("Build revision: %s".formatted(buildCommitId)))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .in(SecurityScheme.In.HEADER)
                                .type(SecurityScheme.Type.APIKEY)));
    }

}
