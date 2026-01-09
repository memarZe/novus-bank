package com.novus.novusbank.exceptions;

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String error) {
        super(error);
    }

}
