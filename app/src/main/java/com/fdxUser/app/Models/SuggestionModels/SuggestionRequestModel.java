package com.fdxUser.app.Models.SuggestionModels;

public class SuggestionRequestModel {
    public String user_id = "";
    public String type = "";
    public String name = "";

    public SuggestionRequestModel(String user_id, String type, String name) {
        this.user_id = user_id;
        this.type = type;
        this.name = name;
    }
}
