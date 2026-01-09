package com.novus.novusbank.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String error) {
        super(error);
    }

}
