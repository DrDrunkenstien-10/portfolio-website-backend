package com.ajadhav.contact.service.email;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ajadhav.contact.dto.ContactRequestDTO;
import com.ajadhav.contact.dto.ContactResponseDTO;
import com.ajadhav.contact.exceptions.EmailSendException;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    // For unit testing
    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public ContactResponseDTO sendEmail(ContactRequestDTO contactRequestDTO) {
        log.info("Attempting to send email from '{}' in behalf of '{}'", sender, contactRequestDTO.getEmail());

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(sender);
            mailMessage.setReplyTo(contactRequestDTO.getEmail());

            String subject = "[Portfolio Contact] %s wants to reach you.".formatted(contactRequestDTO.getName());
            mailMessage.setSubject(subject);

            String body = """
                    Name: %s
                    Email: %s
                    Message: %s
                    """.formatted(
                    contactRequestDTO.getName(),
                    contactRequestDTO.getEmail(),
                    contactRequestDTO.getMessage());
            mailMessage.setText(body);

            javaMailSender.send(mailMessage);

            log.info("Email sent successfully to '{}'", sender);
            return new ContactResponseDTO("Mail Sent Successfully.", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error while sending email to '{}': {}", sender, e.getMessage(), e);
            throw new EmailSendException("Failed to send email");
        }
    }
}
