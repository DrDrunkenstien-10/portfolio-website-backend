package com.ajadhav.contact.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class ContactControllerValidationTest extends BaseApiTest {

	@Test
	void shouldReturn400_WhenInvalidEmail() {
		given()
				.contentType("application/json")
				.body("""
						{
						  "name": "Atharva",
						  "email": "invalid",
						  "message": "Hello!"
						}
						""")
				.when()
				.post("/api/v1/contacts")
				.then()
				.statusCode(400)
				.body("email", equalTo("Invalid email format"));
	}

	@Test
	void shouldReturn400_WhenFieldsAreBlank() {
		given()
				.contentType("application/json")
				.body("""
						{
						  "name": "",
						  "email": "",
						  "message": ""
						}
						""")
				.when()
				.post("/api/v1/contacts")
				.then()
				.statusCode(400)
				.body("name", equalTo("Name is required"))
				.body("email", equalTo("Email is required"))
				.body("message", equalTo("Message is required"));
	}

	@Test
	void shouldSanitizeInput_WhenUnsafeCharactersProvided() {
		String unsafeName = "Alice\\r\\nBCC: victim@example.com";
		String unsafeMessage = "<script>alert('xss')</script>Hello!";

		given()
				.contentType("application/json")
				.body(String.format("""
						{
						  "name": "%s",
						  "email": "alice@example.com",
						  "message": "%s"
						}
						""", unsafeName, unsafeMessage))
				.when()
				.post("/api/v1/contacts")
				.then()
				.log().all()
				.statusCode(200) // ← Now valid to check sanitization result
				.body("name", not(containsString("\r")))
				.body("name", not(containsString("\n")))
				.body("message", not(containsString("<script>")));
	}
}
