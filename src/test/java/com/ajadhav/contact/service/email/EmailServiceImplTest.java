package com.ajadhav.contact.service.email;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ajadhav.contact.dto.ContactRequestDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService.setSender("test@example.com");
    }

    private ContactRequestDTO buildRequest() {
        ContactRequestDTO request = new ContactRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setMessage("Hello!");
        return request;
    }

    @Test
    void sendEmail_shouldSendSuccessfully_onFirstAttempt() {
        // Arrange
        ContactRequestDTO request = buildRequest();

        // Act
        emailService.sendEmail(request);

        // Assert
        verify(javaMailSender, times(1))
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_shouldRetryAndEventuallySucceed() {
        // Arrange
        ContactRequestDTO request = buildRequest();

        doThrow(new RuntimeException("SMTP down"))
                .doThrow(new RuntimeException("Still down"))
                .doNothing()
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(request);

        // Assert
        verify(javaMailSender, times(3))
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_shouldRetryAndGiveUp_afterMaxAttempts() {
        // Arrange
        ContactRequestDTO request = buildRequest();

        doThrow(new RuntimeException("SMTP down"))
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        // Act + Assert
        assertDoesNotThrow(() -> emailService.sendEmail(request));

        verify(javaMailSender, times(3))
                .send(any(SimpleMailMessage.class));
    }
}
