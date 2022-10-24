package com.metflix.exceptions;

public class RegistrationFieldEmptyException extends Exception{
    public RegistrationFieldEmptyException() {
    }

    public RegistrationFieldEmptyException(String message) {
        super(message);
    }

    public RegistrationFieldEmptyException(Throwable cause) {
        super(cause);
    }
}
