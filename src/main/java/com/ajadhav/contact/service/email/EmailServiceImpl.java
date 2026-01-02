package com.ajadhav.contact.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ajadhav.contact.dto.ContactRequestDTO;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final int MAX_RETRIES = 3;
    private static final long BACKOFF_MS = 2_000;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    // For unit testing
    void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    @Async
    public void sendEmail(ContactRequestDTO contactRequestDTO) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.info(
                        "Sending email attempt {}/{} for '{}'",
                        attempt, MAX_RETRIES, contactRequestDTO.getEmail());

                send(contactRequestDTO);

                log.info(
                        "Email successfully sent to '{}' on attempt {}",
                        sender, attempt);
                return;

            } catch (Exception ex) {
                log.warn(
                        "Email attempt {}/{} failed for '{}'",
                        attempt, MAX_RETRIES, contactRequestDTO.getEmail(), ex);

                if (attempt == MAX_RETRIES) {
                    log.error(
                            "Email delivery permanently failed for '{}'",
                            contactRequestDTO.getEmail(), ex);
                    return; // graceful failure
                }

                sleepBeforeRetry();
            }
        }
    }

    private void send(ContactRequestDTO dto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(sender);
        mailMessage.setReplyTo(dto.getEmail());

        mailMessage.setSubject(
                "[Portfolio Contact] %s wants to reach you."
                        .formatted(dto.getName()));

        mailMessage.setText("""
                Name: %s
                Email: %s
                Message: %s
                """.formatted(
                dto.getName(),
                dto.getEmail(),
                dto.getMessage()));

        javaMailSender.send(mailMessage);
    }

    private void sleepBeforeRetry() {
        try {
            Thread.sleep(BACKOFF_MS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
