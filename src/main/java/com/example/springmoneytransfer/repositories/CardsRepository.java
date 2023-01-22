package com.example.springmoneytransfer.repositories;

import com.example.springmoneytransfer.exceptions.CardIsNotValid;
import com.example.springmoneytransfer.exceptions.NotEnoughMoney;
import com.example.springmoneytransfer.exceptions.VerificationCodeIsNotCorrect;
import com.example.springmoneytransfer.models.CardsData;
import com.example.springmoneytransfer.models.CheckedTransaction;
import com.example.springmoneytransfer.models.TransferRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

// that should be more likely totally external services like gate/api to exact banks, but I use that as just internal repo
// because we do not have any demands or description of external apis
@Repository
public class CardsRepository {

    // virtual db like
    final private Map<String, CardsData> cardsDbLike = Map.of(
            "3530000000000000", new CardsData("3530000000000000", "220", "12/23", "USD", 300.0),
            "5530000000000000", new CardsData("5530000000000000", "230", "12/23", "RUR", 3000.0),
            "5540000000000000", new CardsData("5540000000000000", "330", "12/25", "RUR", 30000.0));

    // virtual db like
    private Map<String, CheckedTransaction> transactions = new HashMap<>(Map.of());
    private Map<String, String> verificationCodes = new HashMap<>(Map.of());
    private double profit = 0;

    // some service has to generate verification code and send it to a client, but we do not have that, so we use hardcoded code:
    final private String verificationCode = "0000";

    private boolean isValidCardFull(String cardNumber, String cvcCard, String validTill) {
        if (cardsDbLike.containsKey(cardNumber)) {
            CardsData proceedCard = cardsDbLike.get(cardNumber);
            if (proceedCard.checkCvc(cvcCard) && proceedCard.checkValidTill(validTill)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCardLite(String cardNumber) {
        return cardsDbLike.containsKey(cardNumber);
    }

    public boolean checkAcceptorCard(String cardNumber) {
        if (isValidCardLite(cardNumber)) {
            return true;
        }
        else throw new CardIsNotValid("Acceptor card is not acceptable for transaction");
    }

    public boolean checkDonateCard(String cardNumber, String cvcCard, String validTill,
                                     String transferCurrency, Double transferAmount) {
        if (isValidCardFull(cardNumber, cvcCard, validTill)) {
            CardsData proceedCard = cardsDbLike.get(cardNumber);
            return proceedCard.testDecreasingAmount(transferAmount * 1.1, transferCurrency) >= 0;
        }
        else throw new CardIsNotValid("Donate card is not valid");
    }

    public String checkTransaction(TransferRequest transferRequest) {
        String operationId = java.util.UUID.randomUUID().toString();
        checkAcceptorCard(transferRequest.getCardToNumber());
        if (checkDonateCard(
                transferRequest.getCardFromNumber(), transferRequest.getCardFromCVV(),
                transferRequest.getCardFromValidTill(), transferRequest.getAmount().getCurrency(),
                (double) transferRequest.getAmount().getValue())) {
            verificationCodes.put(operationId, verificationCode);
            transactions.put(operationId,
                    new CheckedTransaction(cardsDbLike.get(transferRequest.getCardFromNumber()),
                            cardsDbLike.get(transferRequest.getCardToNumber()), transferRequest.getAmount()));
            return operationId;
        } else throw new NotEnoughMoney("donate card does not have enough money to proceed");
    }

    public CheckedTransaction getTransaction(String id, String verificationCode) {
        if (verificationCodes.containsKey(id)) {
            if (verificationCodes.get(id).equals(verificationCode)) {
                return transactions.get(id);
            }
        }
        throw new VerificationCodeIsNotCorrect("there is no such transaction or code is wrong");
    }

    public double makeCheckedRequest(CheckedTransaction transaction) {
        double fee = transaction.getAmountToTransfer().getValue() * 0.1;
        double amountToDecrease = transaction.getAmountToTransfer().getValue() + fee;
        transaction.getDonateCard().decreaseAmount(amountToDecrease, transaction.getAmountToTransfer().getCurrency());
        transaction.getAcceptorCard().increaseAmount(transaction.getAmountToTransfer().getValue(), transaction.getAmountToTransfer().getCurrency());
        return fee;
    }

    public void increaseProfitFromFee(double fee) {
        this.profit += fee;
    }

}
