package com.example.springmoneytransfer.handler;

import com.example.springmoneytransfer.exceptions.CardIsNotValid;
import com.example.springmoneytransfer.exceptions.NotEnoughMoney;
import com.example.springmoneytransfer.exceptions.VerificationCodeIsNotCorrect;
import com.example.springmoneytransfer.models.ErrorCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerController {

    @ExceptionHandler({CardIsNotValid.class, NotEnoughMoney.class, VerificationCodeIsNotCorrect.class})
    public ResponseEntity<ErrorCustomResponse> clientError(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorCustomResponse(exception.getMessage(), 0), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorCustomResponse> serverError(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorCustomResponse(exception.getMessage(), 1), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
