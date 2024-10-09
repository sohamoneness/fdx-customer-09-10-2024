package com.fdxUser.app.Models.RestaurantModels;

public class SearchItemsRequestModel {
    public String restaurant_id = "";
    public String user_id = "";
    public String keyword = "";

    public SearchItemsRequestModel(String restaurant_id, String user_id, String keyword) {
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
        this.keyword = keyword;
    }
}
