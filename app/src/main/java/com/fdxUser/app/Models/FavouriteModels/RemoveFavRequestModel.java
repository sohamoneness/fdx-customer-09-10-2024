package com.fdxUser.app.Models.FavouriteModels;

public class RemoveFavRequestModel {
    public String user_id = "";
    public String restaurant_id = "";

    public RemoveFavRequestModel(String user_id, String restaurant_id) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
    }
}
