package com.example.springmoneytransfer.models;

public class OperationResponse {
    private String operationId;

    public OperationResponse(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

}
