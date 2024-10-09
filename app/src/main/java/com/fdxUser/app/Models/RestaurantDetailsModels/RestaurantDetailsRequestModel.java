package com.fdxUser.app.Models.RestaurantDetailsModels;

public class RestaurantDetailsRequestModel {
    public String user_id = "";
    public String restaurant_id = "";

    public RestaurantDetailsRequestModel(String user_id, String restaurant_id) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
    }
}
