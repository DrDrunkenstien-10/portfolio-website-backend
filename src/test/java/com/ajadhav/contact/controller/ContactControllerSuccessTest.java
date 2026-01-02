package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ajadhav.contact.service.email.EmailService;

public class ContactControllerSuccessTest extends BaseApiTest {

    @MockitoBean
    EmailService emailService;

    @Test
    void shouldReturn200_WhenValidRequest() {
        // Arrange
        doNothing()
                .when(emailService)
                .sendEmail(org.mockito.Mockito.any());

        // Act + Assert
        given()
                .contentType("application/json")
                .body("""
                        {
                          "name": "Atharva",
                          "email": "abc@example.com",
                          "message": "Hello!"
                        }
                        """)
                .when()
                .post("/api/v1/contacts")
                .then()
                .statusCode(200)
                .body("message", equalTo("Message received successfully."));

        // Verify async trigger
        verify(emailService)
                .sendEmail(org.mockito.Mockito.any());
    }
}
