package dev.jda.api.library.swagger;

import dev.jda.api.library.mapper.api.ApiConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ApiConfiguration apiConfiguration;

    @Bean
    public Info apiInfo() {
        return new Info()
                .title("My API")
                .description("API documentation")
                .version("1.0");
    }

    @Bean
    public OpenAPI openApi(Info apiInfo) {
        return new OpenAPI()
                .info(apiInfo)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
