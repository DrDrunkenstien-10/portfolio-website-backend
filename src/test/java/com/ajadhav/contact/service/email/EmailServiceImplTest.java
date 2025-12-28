package com.ajadhav.contact.service.email;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ajadhav.contact.dto.ContactRequestDTO;
import com.ajadhav.contact.dto.ContactResponseDTO;
import com.ajadhav.contact.exceptions.EmailSendException;

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

    @Test
    void sendEmail_ShouldReturnSuccessResponse() {
        // Arrange
        ContactRequestDTO request = new ContactRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setMessage("Hello!");

        // Act
        ContactResponseDTO response = emailService.sendEmail(request);

        // Assert
        assertNotNull(response);
        assertEquals("Mail Sent Successfully.", response.getMessage());

        // Verify that JavaMailSender.send() was called
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_ShouldThrowEmailSendException_WhenMailSenderFails() {
        // Arrange
        ContactRequestDTO request = new ContactRequestDTO();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setMessage("Hello!");

        doThrow(new RuntimeException("SMTP server down"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        EmailSendException exception = assertThrows(EmailSendException.class, () -> {
            emailService.sendEmail(request);
        });

        assertEquals("Failed to send email", exception.getMessage());
    }
}
