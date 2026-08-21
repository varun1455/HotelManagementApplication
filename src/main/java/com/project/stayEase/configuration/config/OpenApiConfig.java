package com.project.stayEase.configuration.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stayEaseOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("StayEase Hotel Booking API")
                        .description(
                                "REST API for the StayEase hotel booking application"
                        )
                        .version("1.0.0")
                )
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("User registration, login and JWT token management"),

                        new Tag()
                                .name("Hotel Search")
                                .description("Search and retrieve hotels"),

                        new Tag()
                                .name("Hotel Booking")
                                .description("Create, manage, pay for and cancel hotel bookings"),
                        new Tag()
                                .name("Payment Webhooks")
                                .description("Webhook endpoints used by payment providers to notify StayEase about payment events"),

                        new Tag()
                                .name("System User Management")
                                .description("System administrator APIs for managing system administrators and hotel managers"),

                        new Tag()
                                .name("Hotel Management")
                                .description("Hotel management APIs for hotel managers"),

                        new Tag()
                                .name("Hotel Pricing Configuration")
                                .description("Hotel managers Api to configure dynamic pricing rules for their hotels"),

                        new Tag()
                                .name("Room Management")
                                .description("Room management APIs for hotel managers"),

                        new Tag()
                                .name("Room Type Management")
                                .description("System-defined room types"),

                        new Tag()
                                .name("Bed Type Management")
                                .description("System-defined bed types"),

                        new Tag()
                                .name("Holiday Management")
                                .description("Holiday synchronization and holiday pricing rules")
                ))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearerAuth")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
