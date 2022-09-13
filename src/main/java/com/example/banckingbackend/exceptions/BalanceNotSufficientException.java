package com.example.banckingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {

    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
