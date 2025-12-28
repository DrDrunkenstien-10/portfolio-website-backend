package com.ajadhav.contact.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajadhav.contact.dto.ContactRequestDTO;
import com.ajadhav.contact.dto.ContactResponseDTO;
import com.ajadhav.contact.service.email.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {
    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<ContactResponseDTO> sendEmail(@Valid @RequestBody ContactRequestDTO request) {
        log.info("Received contact request from '{}'", request.getEmail());
        ContactResponseDTO response = emailService.sendEmail(request);

        log.info("Successfully processed contact request for '{}'", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
