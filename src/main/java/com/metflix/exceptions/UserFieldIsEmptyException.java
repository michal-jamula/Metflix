package com.metflix.exceptions;

public class UserFieldIsEmptyException extends Exception{

    public UserFieldIsEmptyException(String errorMessage) {
        super(errorMessage);
    }
}
