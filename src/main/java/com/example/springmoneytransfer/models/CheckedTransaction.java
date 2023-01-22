package com.example.springmoneytransfer.models;

public class CheckedTransaction {
    private CardsData donateCard;
    private CardsData acceptorCard;
    private Amount amountToTransfer;

    public CheckedTransaction(CardsData donateCard, CardsData acceptorCard, Amount amountToTransfer) {
        this.donateCard = donateCard;
        this.acceptorCard = acceptorCard;
        this.amountToTransfer = amountToTransfer;
    }

    public CardsData getDonateCard() {
        return donateCard;
    }

    public void setDonateCard(CardsData donateCard) {
        this.donateCard = donateCard;
    }

    public CardsData getAcceptorCard() {
        return acceptorCard;
    }

    public void setAcceptorCard(CardsData acceptorCard) {
        this.acceptorCard = acceptorCard;
    }

    public Amount getAmountToTransfer() {
        return amountToTransfer;
    }

    public void setAmountToTransfer(Amount amountToTransfer) {
        this.amountToTransfer = amountToTransfer;
    }

}
