package com.fdxUser.app.Models.FavouriteModels;

public class AddFavRequestModel {
    public String user_id = "";
    public String restaurant_id = "";

    public AddFavRequestModel(String user_id, String restaurant_id) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
    }
}
