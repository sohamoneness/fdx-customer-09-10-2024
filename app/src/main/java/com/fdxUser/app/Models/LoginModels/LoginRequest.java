package com.fdxUser.app.Models.LoginModels;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;

    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
