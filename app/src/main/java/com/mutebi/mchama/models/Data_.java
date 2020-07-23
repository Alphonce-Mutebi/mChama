package com.mutebi.mchama.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data_ {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("authorized")
    @Expose
    private Boolean authorized;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private UserResponse userResponse;
    @SerializedName("token")
    @Expose
    private String token;

    public Data_(Integer success, Boolean authorized, String message, UserResponse userResponse, String token) {
        this.success = success;
        this.authorized = authorized;
        this.message = message;
        this.userResponse = userResponse;
        this.token = token;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Data_{" +
                "success=" + success +
                ", authorized=" + authorized +
                ", message='" + message + '\'' +
                ", userResponse=" + userResponse +
                ", token='" + token + '\'' +
                '}';
    }
}
