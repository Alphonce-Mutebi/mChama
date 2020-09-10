package com.mutebi.mchama.models;

public class TransactionList {
    private String amount;
    private String transactionType;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionList(String amount, String transactionType){
        this.amount =amount;
        this.transactionType = transactionType;

    }


}
