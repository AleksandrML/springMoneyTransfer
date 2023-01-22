package com.example.springmoneytransfer.models;

public class Amount {
    private int value;
    private String currency;

    public Amount() {
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value/100;  // this way works https://serp-ya.github.io/card-transfer/ front -
        // it sends int value plus 2 zeros (100 times higher), front bug or a hint that 2 last digits are cents?
        // Not clear really, I believe I may consider as I wish then:)
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
