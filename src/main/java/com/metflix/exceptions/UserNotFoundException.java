package com.metflix.exceptions;

public class UserNotFoundException extends Exception{


    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
