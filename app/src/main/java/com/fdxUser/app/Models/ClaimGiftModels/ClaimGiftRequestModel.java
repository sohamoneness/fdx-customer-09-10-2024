package com.fdxUser.app.Models.ClaimGiftModels;

public class ClaimGiftRequestModel {

    public String user_id = "";
    public String gender = "";
    public String date_of_birth = "";
    public String date_of_anniversary = "";

    public ClaimGiftRequestModel(String user_id, String gender, String date_of_birth, String date_of_anniversary) {
        this.user_id = user_id;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.date_of_anniversary = date_of_anniversary;
    }
}
