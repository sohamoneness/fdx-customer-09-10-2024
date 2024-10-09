package com.fdxUser.app.Models.DemoModels;

import java.util.ArrayList;
import java.util.List;

public class OrderHistModel {
    public String rest_name = "";
    public String rest_address = "";
    public String rating = "";
    public String status = "";
    public String tot_amount = "";
    public List<OrderItemModel> orderItemModelList = new ArrayList<>();

    public List<OrderItemModel> getOrderItemModelList() {
        return orderItemModelList;
    }

    public void setOrderItemModelList(List<OrderItemModel> orderItemModelList) {
        this.orderItemModelList = orderItemModelList;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_address() {
        return rest_address;
    }

    public void setRest_address(String rest_address) {
        this.rest_address = rest_address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTot_amount() {
        return tot_amount;
    }

    public void setTot_amount(String tot_amount) {
        this.tot_amount = tot_amount;
    }
}