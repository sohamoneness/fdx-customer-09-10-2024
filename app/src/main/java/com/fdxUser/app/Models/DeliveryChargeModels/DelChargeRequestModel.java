package com.fdxUser.app.Models.DeliveryChargeModels;

public class DelChargeRequestModel {

    public String delivery_lat = "";
    public String delivery_lng = "";
    public String restaurant_id = "";
    public String user_id = "";

    public DelChargeRequestModel(String delivery_lat, String delivery_lng, String restaurant_id,
                                 String user_id) {
        this.delivery_lat = delivery_lat;
        this.delivery_lng = delivery_lng;
        this.restaurant_id = restaurant_id;
        this.user_id = user_id;
    }

}
