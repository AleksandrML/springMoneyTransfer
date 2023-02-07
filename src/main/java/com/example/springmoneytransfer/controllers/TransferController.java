package com.example.springmoneytransfer.controllers;

import com.example.springmoneytransfer.models.ConfirmRequest;
import com.example.springmoneytransfer.models.OperationResponse;
import com.example.springmoneytransfer.models.TransferRequest;
import com.example.springmoneytransfer.services.MoneyTransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransferController {
    MoneyTransferService moneyTransferService;

    public TransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<OperationResponse> doTransfer(@RequestBody TransferRequest transferRequest) {
        return new ResponseEntity<>(moneyTransferService.beginTransferProcess(transferRequest), HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<OperationResponse> confirmTransfer(@RequestBody ConfirmRequest confirmRequest) {
        return new ResponseEntity<>(moneyTransferService.confirmAndFinishTransfer(confirmRequest), HttpStatus.OK);
    }

}
