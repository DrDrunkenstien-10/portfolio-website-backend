package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ajadhav.contact.service.email.EmailService;
import com.ajadhav.contact.dto.ContactResponseDTO;

import java.time.LocalDateTime;

public class RateLimitTest extends BaseApiTest {

	@MockitoBean
	EmailService emailService;

	@Test
	void shouldReturn429_WhenRateLimitExceeded() {
		// Mock the email service response
		var mockResponse = new ContactResponseDTO("Mail Sent Successfully.", LocalDateTime.now());
		org.mockito.Mockito.when(emailService.sendEmail(org.mockito.Mockito.any()))
				.thenReturn(mockResponse);

		// Send 5 allowed requests
		for (int i = 0; i < 5; i++) {
			given()
					.contentType("application/json")
					.body("""
							    {
							      "name": "Test User",
							      "email": "test@example.com",
							      "message": "Hello!"
							    }
							""")
					.when()
					.post("/api/v1/contacts")
					.then()
					.statusCode(200);
		}

		// 6th request should be blocked by the rate limiter
		given()
				.contentType("application/json")
				.body("""
						    {
						      "name": "Test User",
						      "email": "test@example.com",
						      "message": "Hello!"
						    }
						""")
				.when()
				.post("/api/v1/contacts")
				.then()
				.statusCode(429)
				.body("message", equalTo("Too many requests. Retry later."));
	}
}
