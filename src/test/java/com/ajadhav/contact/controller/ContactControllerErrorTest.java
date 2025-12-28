package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ajadhav.contact.exceptions.EmailSendException;
import com.ajadhav.contact.service.email.EmailService;

public class ContactControllerErrorTest extends BaseApiTest {

	@MockitoBean
	EmailService emailService;

	@Test
	void shouldReturn500_WhenEmailSendFails() {
		org.mockito.Mockito.when(emailService.sendEmail(org.mockito.Mockito.any()))
				.thenThrow(new EmailSendException("Failed to send email"));

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
				.statusCode(500)
				.body("error", equalTo("Failed to send email"))
				.body("timestamp", notNullValue());
	}
}
