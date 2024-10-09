package com.fdxUser.app.Models.TokenUpdateModels;

public class TokenUpdateRequestModel {

    public String id = "";
    public String device_id = "";
    public String device_token = "";

    public TokenUpdateRequestModel(String id, String device_id, String device_token) {
        this.id = id;
        this.device_id = device_id;
        this.device_token = device_token;
    }
}
