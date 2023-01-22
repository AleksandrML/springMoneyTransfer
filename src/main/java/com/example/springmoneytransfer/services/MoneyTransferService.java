package com.example.springmoneytransfer.services;

import com.example.springmoneytransfer.models.CheckedTransaction;
import com.example.springmoneytransfer.models.ConfirmRequest;
import com.example.springmoneytransfer.models.OperationResponse;
import com.example.springmoneytransfer.models.TransferRequest;
import com.example.springmoneytransfer.repositories.CardsRepository;
import org.springframework.stereotype.Service;


@Service
public class MoneyTransferService {
    CardsRepository cardsRepository;

    public MoneyTransferService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    public OperationResponse beginTransferProcess(TransferRequest transferRequest) {
        return new OperationResponse(cardsRepository.checkTransaction(transferRequest));
    }

    public OperationResponse confirmAndFinishTransfer(ConfirmRequest confirmRequest) {
        CheckedTransaction transferRequestChecked = cardsRepository.getTransaction(
                confirmRequest.getOperationId(), confirmRequest.getCode());
        double feeProfit = cardsRepository.makeCheckedRequest(transferRequestChecked);
        cardsRepository.increaseProfitFromFee(feeProfit);
        return new OperationResponse(confirmRequest.getOperationId());
    }

}
