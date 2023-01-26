package com.example.springmoneytransfer.controllers;

import com.example.springmoneytransfer.exceptions.CardIsNotValid;
import com.example.springmoneytransfer.exceptions.NotEnoughMoney;
import com.example.springmoneytransfer.exceptions.VerificationCodeIsNotCorrect;
import com.example.springmoneytransfer.models.ConfirmRequest;
import com.example.springmoneytransfer.models.ErrorCustomResponse;
import com.example.springmoneytransfer.models.OperationResponse;
import com.example.springmoneytransfer.models.TransferRequest;
import com.example.springmoneytransfer.services.MoneyTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransferController {
    MoneyTransferService moneyTransferService;

    public TransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping("/transfer")
    public OperationResponse doTransfer(@RequestBody TransferRequest transferRequest) {
        return moneyTransferService.beginTransferProcess(transferRequest);
    }

    @PostMapping("/confirmOperation")
    public OperationResponse confirmTransfer(@RequestBody ConfirmRequest confirmRequest) {
        return moneyTransferService.confirmAndFinishTransfer(confirmRequest);
    }

    @ExceptionHandler({CardIsNotValid.class, NotEnoughMoney.class, VerificationCodeIsNotCorrect.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCustomResponse clientError(RuntimeException exception) {
        return new ErrorCustomResponse(exception.getMessage(), 0);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorCustomResponse serverError(RuntimeException exception) {
        return new ErrorCustomResponse(exception.getMessage(), 1);
    }

}
