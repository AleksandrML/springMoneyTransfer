package com.example.springmoneytransfer.services;

import com.example.springmoneytransfer.enums.OperationType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    public static final String logFileName = "logs.log";

    public static void write(OperationType operationType, String operationId, Double amount, String currency,
                             String cardFrom, String cardTo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFileName, true))) {
            String logMessage = getLoggerMessage(operationType, operationId, amount, currency, cardFrom, cardTo);
            bw.write(logMessage);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static String getLoggerMessage(OperationType operationType, String operationId, Double amount, String currency,
                                             String cardFrom, String cardTo) {
        if (cardTo != null) {
            return LocalDateTime.now() + "; id: " + operationId + ", operation: " + operationType + ", amount: " + amount + currency + ", card from: " + cardFrom + ", card to: " + cardTo + "\n";
        }
        return LocalDateTime.now() + "; id: " + operationId + ", operation: " + operationType + ", amount: " + amount + currency + ", card from: " + cardFrom + "\n";
    }
}
