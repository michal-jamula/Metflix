package com.metflix.exceptions;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException() {
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
