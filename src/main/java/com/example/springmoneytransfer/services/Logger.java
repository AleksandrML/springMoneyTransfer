package com.example.springmoneytransfer.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
//TODO: use operation type (transfer money or getting comission) plus id of operation
    public static final String logFileName = "logs.log";

    public static void write(String message, String userName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFileName, true))) {
            String text = getLoggerMessage(message, userName);
            bw.write(text);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static String getLoggerMessage(String message, String userName) {
        return LocalDateTime.now() + "; " + userName + ": " + message + "\n";
    }
}

//Logger.write(word, clientName);