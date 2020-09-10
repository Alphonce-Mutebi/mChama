package com.mutebi.mchama.models;

public class TransactionList {
    private String amount;
    private String transactionType;
    private String date;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        String transType = "other";
        if(transactionType.equals("1")){
            transType = "Loan Service";
        }
        else if(transactionType.equals("2")){
            transType = "mWallet";
        }

        return transType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDate() {
        String justDate = date.substring(0, Math.min(date.length(), 10));

        return justDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TransactionList(String amount, String transactionType, String date){
        this.amount =amount;
        this.transactionType = transactionType;
        this.date = date;

    }


}
