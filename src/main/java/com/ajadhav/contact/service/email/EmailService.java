package com.ajadhav.contact.service.email;

import com.ajadhav.contact.dto.ContactRequestDTO;
import com.ajadhav.contact.dto.ContactResponseDTO;

public interface EmailService {
    ContactResponseDTO sendEmail(ContactRequestDTO contactRequestDTO);
}
