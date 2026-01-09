package com.novus.novusbank.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String error) {
        super(error);
    }

}
