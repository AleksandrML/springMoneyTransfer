package com.example.springmoneytransfer.exceptions;

public class CardIsNotValid extends RuntimeException {
    public CardIsNotValid(String msg) {
        super(msg);
    }
}
