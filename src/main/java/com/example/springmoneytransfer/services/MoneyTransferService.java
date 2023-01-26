package com.example.springmoneytransfer.services;

import com.example.springmoneytransfer.enums.OperationType;
import com.example.springmoneytransfer.exceptions.NotEnoughMoney;
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
        return new OperationResponse(checkTransaction(transferRequest));
    }

    public OperationResponse confirmAndFinishTransfer(ConfirmRequest confirmRequest) {
        CheckedTransaction transferRequestChecked = cardsRepository.getTransaction(
                confirmRequest.getOperationId(), confirmRequest.getCode());
        makeCheckedTransaction(transferRequestChecked, confirmRequest.getOperationId());
        return new OperationResponse(confirmRequest.getOperationId());
    }

    public String checkTransaction(TransferRequest transferRequest) {
        String operationId = java.util.UUID.randomUUID().toString();
        cardsRepository.checkAcceptorCard(transferRequest.getCardToNumber());
        if (cardsRepository.checkDonateCard(
                transferRequest.getCardFromNumber(), transferRequest.getCardFromCVV(),
                transferRequest.getCardFromValidTill(), transferRequest.getAmount().getCurrency(),
                (double) transferRequest.getAmount().getValue())) {
            cardsRepository.generateVerificationCodeForOperation(operationId);
            cardsRepository.addCheckedTransaction(operationId, transferRequest.getCardFromNumber(),
                    transferRequest.getCardToNumber(), transferRequest.getAmount());
            Logger.write(OperationType.SUCCESSFUL_TRANSFER_PREPARATION_CHECK, operationId,
                    (double) transferRequest.getAmount().getValue(), transferRequest.getAmount().getCurrency(),
                    transferRequest.getCardFromNumber(), transferRequest.getCardToNumber());
            return operationId;
        } else throw new NotEnoughMoney("donate card does not have enough money to proceed");
    }

    private void makeCheckedTransaction(CheckedTransaction transaction, String operationId) {
        double fee = transaction.getAmountToTransfer().getValue() * cardsRepository.getChargeCommission();
        double amountToDecrease = transaction.getAmountToTransfer().getValue() + fee;
        transaction.getDonateCard().decreaseAmount(amountToDecrease, transaction.getAmountToTransfer().getCurrency());
        transaction.getAcceptorCard().increaseAmount(transaction.getAmountToTransfer().getValue(), transaction.getAmountToTransfer().getCurrency());
        Logger.write(OperationType.MONEY_TRANSFER, operationId,
                (double) transaction.getAmountToTransfer().getValue(), transaction.getAmountToTransfer().getCurrency(),
                transaction.getDonateCard().getNumber(), transaction.getAcceptorCard().getNumber());
        if (transaction.getAmountToTransfer().getCurrency().equals("RUR")) cardsRepository.setProfitRurAccount(cardsRepository.getProfitRurAccount() + fee);
        if (transaction.getAmountToTransfer().getCurrency().equals("USD")) cardsRepository.setProfitUsdAccount(cardsRepository.getProfitUsdAccount() + fee);  // we have only 2 supported currencies
        Logger.write(OperationType.CHARGE_COMMISSION, operationId, fee, transaction.getAmountToTransfer().getCurrency(),
                transaction.getDonateCard().getNumber(), null);
    }

}
