package com.fdxUser.app.Models.OrderSummeryModels;

public class OrderDateFilterRequestModel {
    public String user_id = "";
    public String start_date = "";
    public String end_date = "";

    public OrderDateFilterRequestModel(String user_id, String start_date, String end_date) {
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
