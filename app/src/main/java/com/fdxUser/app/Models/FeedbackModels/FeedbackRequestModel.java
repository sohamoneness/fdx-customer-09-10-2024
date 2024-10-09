package com.fdxUser.app.Models.FeedbackModels;

public class FeedbackRequestModel {
    public String user_id = "";
    public String restaurant_id = "";
    public String order_ref_id = "";
    public String rating = "";
    public String review = "";

    public FeedbackRequestModel(String user_id, String restaurant_id, String order_ref_id, String rating, String review) {
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
        this.order_ref_id = order_ref_id;
        this.rating = rating;
        this.review = review;
    }
}
