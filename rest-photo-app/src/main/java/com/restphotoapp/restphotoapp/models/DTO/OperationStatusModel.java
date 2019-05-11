package com.restphotoapp.restphotoapp.models.DTO;

public class OperationStatusModel {
    private String operationResult;
    private String operationName;

    /**
     * @return the operationResult
     */
    public String getOperationResult() {
        return operationResult;
    }

    /**
     * @return the operationName
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * @param operationName the operationName to set
     */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * @param operationResult the operationResult to set
     */
    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }
}