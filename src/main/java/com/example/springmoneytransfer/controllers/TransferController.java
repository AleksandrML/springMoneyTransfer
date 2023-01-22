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


//TODO: logs, optimize code in cardsrepository (probably move part to services), tests
//{"cardFromNumber": "5540000000000000", "cardFromValidTill": "12/25", "cardFromCVV": "330", "cardToNumber": "5530000000000000",
//        "amount": {"value": 1000, "currency": "RUR"}}
//        {"operationId": "0f851972-45d9-4bd1-a443-433e6b985f7c", "code": "0000"}
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
