package com.fdxUser.app.Models.RestaurantModels;

import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantModel {
    public String id = "";
    public String name = "";
    public String mobile = "";
    public String email = "";
    public String password = "";
    public String image = "";
    public String logo = "";
    public String description = "";
    public String address = "";
    public String location = "";
    public String lat = "";
    public String lng = "";
    public String start_time = "";
    public String close_time = "";
    public String special_items_string = "";
    public String is_pure_veg = "";
    public String commission_rate = "";
    public String estimated_delivery_time = "";
    public String is_not_taking_orders = "";
    public String is_pre_order_only = "";
    public String pre_order_duration = "";
    public String status = "";
    public String created_at = "";
    public String updated_at = "";
    public List<SpecialItemModel> special_items = new ArrayList<>();
    public String distance = "";
    public String rating = "";
    public String delivery_charge = "";
    public String is_favorite = "";
    public String is_closed = "";
    public String payment_mode = "";
    public String delivery_mode = "";
}
