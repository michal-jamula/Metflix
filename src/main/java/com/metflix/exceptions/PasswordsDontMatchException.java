package com.metflix.exceptions;


public class PasswordsDontMatchException extends Exception{
    public PasswordsDontMatchException() {
    }

    public PasswordsDontMatchException(String message) {
        super(message);
    }

    public PasswordsDontMatchException(Throwable cause) {
        super(cause);
    }
}
