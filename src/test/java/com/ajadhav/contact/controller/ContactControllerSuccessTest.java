package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ajadhav.contact.dto.ContactResponseDTO;
import com.ajadhav.contact.service.email.EmailService;

import java.time.LocalDateTime;

public class ContactControllerSuccessTest extends BaseApiTest {

    @MockitoBean
    EmailService emailService;

    @Test
    void shouldReturn200_WhenValidRequest() {
        var mockResponse = new ContactResponseDTO("Mail Sent Successfully.", LocalDateTime.now());
        org.mockito.Mockito.when(emailService.sendEmail(org.mockito.Mockito.any()))
                           .thenReturn(mockResponse);

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
            .body("message", equalTo("Mail Sent Successfully."));
    }
}
