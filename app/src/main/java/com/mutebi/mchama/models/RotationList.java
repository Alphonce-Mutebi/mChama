package com.mutebi.mchama.models;

public class RotationList {
    private String name;
    private String rotationTurn;
    private String userType;

    /*
    public String getTransactionType() {
        String transType = "other";
        if(rotationTurn.equals("1")){
            transType = "Loan Service";
        }
        else if(rotationTurn.equals("2")){
            transType = "mWallet";
        }

        return transType;
    }

     */


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRotationTurn() {
        return rotationTurn;
    }

    public void setRotationTurn(String rotationTurn) {
        this.rotationTurn = rotationTurn;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public RotationList(String name, String rotationTurn, String userType){
        this.name =name;
        this.rotationTurn = rotationTurn;
        this.userType = userType;

    }
}
