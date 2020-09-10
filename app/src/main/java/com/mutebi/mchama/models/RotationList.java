package com.mutebi.mchama.models;

public class RotationList {
    private String name;
    private String rotationTurn;
    private String userType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRotationTurn() {
        String rotation = "No";
        if(rotationTurn.equals("0")){
            rotation = "No";
        }
        else if(rotationTurn.equals("1")){
            rotation = "On Rotation";
        }

        return rotation;
    }

    public void setRotationTurn(String rotationTurn) {
        this.rotationTurn = rotationTurn;
    }

    public String getUserType() {
        String justDate = "Joined: "+userType.substring(0, Math.min(userType.length(), 10));
        return justDate;
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
