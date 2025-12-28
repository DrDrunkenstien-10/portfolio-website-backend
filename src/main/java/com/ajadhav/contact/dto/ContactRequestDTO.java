package com.ajadhav.contact.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class ContactRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Trim, remove line breaks, sanitize HTML
        if (name != null) {
            this.name = Jsoup.clean(name.trim().replaceAll("[\\r\\n]", ""), Safelist.none());
        } else {
            this.name = null;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Trim and remove line breaks to prevent header injection
        if (email != null) {
            this.email = email.trim().replaceAll("[\\r\\n]", "");
        } else {
            this.email = null;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        // Trim and sanitize HTML, allow basic formatting if needed
        if (message != null) {
            this.message = Jsoup.clean(message.trim(), Safelist.basic());
        } else {
            this.message = null;
        }
    }
}
