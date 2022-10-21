package com.metflix.exceptions;

import lombok.experimental.StandardException;

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
