package ru.shanalotte.serviceregistry.openapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        description = "Service registry API that provides active service search by name",
        contact = @Contact(
            email = "drizhiloda@gmail.com",
            name = "Drizhilo D."
        ),
        version = "1.0",
        title = "Service registry API Definition"
    )
)
@Configuration
public class OpenApiConfig {
}
