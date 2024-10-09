package com.fdxUser.app.Models.RegisterModels;

public class RegisterRequest {

    public String name = "";
    public String email = "";
    public String mobile = "";
    public String password = "";
    public String password_confirmation = "";
    public String referrer_code = "";

    public RegisterRequest(String name, String email, String mobile, String password,
                           String password_confirmation, String referrer_code) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.referrer_code = referrer_code;
    }
}
