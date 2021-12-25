package com.toquery.framework.example.core.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Clever Framework Demo API", version = "1.0.0", description = "Clever Framework Demo API"))
public class OpenApiConfig {

}
