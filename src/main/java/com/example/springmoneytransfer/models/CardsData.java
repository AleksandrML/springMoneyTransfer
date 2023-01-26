package com.example.springmoneytransfer.models;

import com.example.springmoneytransfer.exceptions.NotEnoughMoney;

public class CardsData {
    private String number;
    private String cvc;
    private String validTill;
    private String currency;
    private double amount;

    public CardsData(String number, String cvc, String validTill, String currency, double amount) {
        this.number = number;
        this.cvc = cvc;
        this.validTill = validTill;
        this.currency = currency;
        this.amount = amount;
    }

    public boolean checkCvc(String cvc) {
        return this.cvc.equals(cvc);
    }

    // in fact, we had to check expire date also but right now Russian paying cards working forever because of sanctions,
    // so we do not need to limit our customers by that bothering problem anymore:)
    public boolean checkValidTill(String validTill) {
        return this.validTill.equals(validTill);
    }

    public void increaseAmount(double amountPlus, String currency) {
        if (this.currency.equals(currency)) {
            this.amount += amountPlus;
        } else {
            this.amount += getRate(currency, this.currency) * amountPlus;
        }
    }

    public Double testDecreasingAmount(double amountMinus, String currency) {
        if (this.currency.equals(currency)) {
            return this.amount - amountMinus;
        } else {
            return this.amount - getRate(currency, this.currency) * amountMinus;
        }
    }

    public void decreaseAmount(double amountMinus, String currency) {
        Double newAmount = testDecreasingAmount(amountMinus, currency);
        if (newAmount >= 0) {
            this.amount = newAmount;
        } else {
            throw new NotEnoughMoney("money is not enough on a card");
        }
    }

    // consider that as external api
    // our service will support only RUB and USD, that is a kind a simulation of an external service, so it is simplified
    private Float getRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return 1f;
        } else if (fromCurrency.equals("RUR")) {
            return 0.01f;
        } else {
            return 100f;
        }
    }

    public String getNumber() {
        return number;
    }

    public double getAmount() {
        return amount;
    }

}
