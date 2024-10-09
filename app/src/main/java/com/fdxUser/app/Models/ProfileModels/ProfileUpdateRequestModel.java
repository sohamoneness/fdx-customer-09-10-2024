package com.fdxUser.app.Models.ProfileModels;

public class ProfileUpdateRequestModel {

    public String id = "";
    public String name = "";
    public String email = "";
    public String gender = "";
    public String date_of_birth = "";
    public String image = "";

    public ProfileUpdateRequestModel(String id, String name, String email, String gender, String date_of_birth, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.image = image;
    }
}
