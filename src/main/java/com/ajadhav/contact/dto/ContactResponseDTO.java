package com.ajadhav.contact.dto;

import java.time.LocalDateTime;

public class ContactResponseDTO {
    private String message;
    private LocalDateTime timeStamp;

    // Parameterized Constructor
    public ContactResponseDTO(String message, LocalDateTime timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
