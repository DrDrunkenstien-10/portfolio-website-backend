# Portfolio Website Backend

## Overview

This repository, portfolio-website-backend, contains the backend code for my personal portfolio website.
At present, it implements a production-style Spring Boot REST API for handling Contact Form submissions.

The Contact API is designed with real-world backend engineering concerns in mind, including asynchronous processing, fault tolerance, rate limiting, and testability. The API accepts contact requests, responds immediately to the client, and triggers email delivery asynchronously in the background.

While the current scope is limited to the contact form functionality, the project is structured to be extensible and serves as a foundation for adding additional backend features for the portfolio website in the future.

## Key Features

### RESTful Contact API
- Exposes a POST endpoint to accept contact form submissions
- Validates input and returns clear API responses

### Asynchronous Email Processing
- Email sending is performed asynchronously using Spring's `@Async`
- Prevents request blocking and improves API responsiveness
- Uses a custom `ThreadPoolTaskExecutor` for controlled concurrency

### Retry with Backoff for Email Delivery
- Retries email delivery up to a fixed number of attempts
- Adds a backoff delay between retries to handle transient failures
- Fails gracefully without crashing the application

### Rate Limiting
- Limits the number of contact submissions per client
- Prevents spam and abuse of the API
- Returns HTTP 429 (Too Many Requests) when the limit is exceeded

### Clean Separation of Concerns
- Controller layer handles HTTP requests and responses
- Service layer contains business logic
- Configuration layer handles async execution and infrastructure concerns

### Comprehensive Testing
- Unit tests for service-layer logic
- Controller/API tests for request-response behavior
- Rate limit behavior verified via API tests
- External dependencies (email sending) mocked for deterministic tests

## API Endpoint

### POST /api/v1/contacts

**Request Body (JSON):**
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "message": "Hello!"
}
```

**Successful Response:**
```
HTTP 200
{
    "message": "Message received successfully."
}
```

**Rate Limit Exceeded Response:**
```
HTTP 429
{
    "message": "Too many requests. Retry later."
}
```

## High-Level Architecture

1. Client sends contact request
2. Controller validates request and returns success response
3. Email sending is triggered asynchronously
4. Email service handles retries and backoff internally
5. Rate limiting is enforced at the API boundary
6. The request thread is never blocked by email delivery

## Async Design Details

- `@EnableAsync` is used to enable asynchronous execution
- A custom `ThreadPoolTaskExecutor` is configured to:
    - Limit concurrent async tasks
    - Avoid uncontrolled thread creation
    - Improve observability with named threads

This ensures production-safe background processing.

## Testing Strategy

### Unit Tests
- Focus on business logic (email retries, graceful failures)
- External dependencies are mocked
- Fast and deterministic

### Controller / API Tests
- Validate request mappings and response contracts
- Verify delegation to service layer
- Test rate limiting behavior

This layered testing approach ensures correctness at every level.

## Configuration

Sensitive information (SMTP credentials, secrets) is **not** committed. Configuration values are injected via application properties or environment variables.

An example configuration file can be provided for local setup.

## Design Decisions

- Email sending is asynchronous to avoid blocking user requests
- Retry logic is implemented to handle transient SMTP failures
- Rate limiting is added to protect the API from abuse
- Logging is used for observability and debugging
- Tests are written to verify behavior, not implementation details

## Intended Use

This project is intended for:

- Showcasing backend engineering skills as part of a personal portfolio website
- Demonstrating production-style Spring Boot application design
- Learning, experimentation, and reference for backend patterns and best practices
- Serving as a foundation for future backend features of the portfolio website  