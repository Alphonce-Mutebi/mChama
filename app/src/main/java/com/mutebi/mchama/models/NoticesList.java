package com.mutebi.mchama.models;

public class NoticesList {
    private String message;
    private String created_at;

    public NoticesList(String message, String created_at) {
        this.message = message;
        this.created_at = created_at;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        String justDate = created_at.substring(0, Math.min(created_at.length(), 10));

        return justDate;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
