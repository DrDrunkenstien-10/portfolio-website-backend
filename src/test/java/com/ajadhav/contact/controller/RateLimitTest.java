package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ajadhav.contact.service.email.EmailService;

public class RateLimitTest extends BaseApiTest {

    @MockitoBean
    EmailService emailService;

    @Test
    void shouldReturn429_WhenRateLimitExceeded() {
        // Arrange
        doNothing()
                .when(emailService)
                .sendEmail(org.mockito.Mockito.any());

        String requestBody = """
                {
                  "name": "Test User",
                  "email": "test@example.com",
                  "message": "Hello!"
                }
                """;

        // Send allowed requests
        for (int i = 0; i < 5; i++) {
            given()
                    .contentType("application/json")
                    .body(requestBody)
                    .when()
                    .post("/api/v1/contacts")
                    .then()
                    .statusCode(200)
                    .body("message", equalTo("Message received successfully."));
        }

        // Next request should be rate-limited
        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/contacts")
                .then()
                .statusCode(429)
                .body("message", equalTo("Too many requests. Retry later."));
    }
}
