package com.example.springmoneytransfer.exceptions;

public class NotEnoughMoney extends RuntimeException {
    public NotEnoughMoney(String msg) {
        super(msg);
    }
}
