package com.example.springmoneytransfer.exceptions;

public class VerificationCodeIsNotCorrect extends RuntimeException {
    public VerificationCodeIsNotCorrect(String msg) {
        super(msg);
    }
}
