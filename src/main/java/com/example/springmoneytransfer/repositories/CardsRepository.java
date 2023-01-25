package com.example.springmoneytransfer.repositories;

import com.example.springmoneytransfer.exceptions.CardIsNotValid;
import com.example.springmoneytransfer.exceptions.VerificationCodeIsNotCorrect;
import com.example.springmoneytransfer.models.Amount;
import com.example.springmoneytransfer.models.CardsData;
import com.example.springmoneytransfer.models.CheckedTransaction;
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
    private double profitUsdAccount = 0;
    private double profitRurAccount = 0;
    private double chargeCommission = 0.1;

    // some service has to generate verification code and send it to a client, but we do not have that, so we use hardcoded code:
    final private String verificationCode = "0000";

    public double getProfitUsdAccount() {
        return profitUsdAccount;
    }

    public double getProfitRurAccount() {
        return profitRurAccount;
    }

    public double getChargeCommission() {
        return chargeCommission;
    }

    public void setProfitUsdAccount(double profitUsdAccount) {
        this.profitUsdAccount = profitUsdAccount;
    }

    public void setProfitRurAccount(double profitRurAccount) {
        this.profitRurAccount = profitRurAccount;
    }

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
            return proceedCard.testDecreasingAmount(transferAmount * (1 + chargeCommission), transferCurrency) >= 0;
        }
        else throw new CardIsNotValid("Donate card is not valid");
    }

    public CheckedTransaction getTransaction(String id, String verificationCode) {
        if (verificationCodes.containsKey(id)) {
            if (verificationCodes.get(id).equals(verificationCode)) {
                return transactions.get(id);
            }
        }
        throw new VerificationCodeIsNotCorrect("there is no such transaction or code is wrong");
    }

    public void generateVerificationCodeForOperation(String operationId) {
        verificationCodes.put(operationId, verificationCode);
    }

    public void addCheckedTransaction(String operationId, String cardFromNumber, String cardToNumber, Amount amount) {
        transactions.put(operationId, new CheckedTransaction(cardsDbLike.get(cardFromNumber), cardsDbLike.get(cardToNumber), amount));
    }

}
