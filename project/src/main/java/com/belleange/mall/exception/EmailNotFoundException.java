package com.belleange.mall.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("Email not found.");
    }
}
