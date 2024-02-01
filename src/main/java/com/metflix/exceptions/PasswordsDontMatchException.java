package com.metflix.exceptions;


public class PasswordsDontMatchException extends Exception{

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
