package com.belleange.mall.exception;

public class InvalidCertificationNumberException extends RuntimeException {
    public InvalidCertificationNumberException() {
        super("Invalid certification number.");
    }
}
