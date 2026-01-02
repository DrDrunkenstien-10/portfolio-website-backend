package com.ajadhav.contact.service.email;

import com.ajadhav.contact.dto.ContactRequestDTO;

public interface EmailService {
    void sendEmail(ContactRequestDTO contactRequestDTO);
}
