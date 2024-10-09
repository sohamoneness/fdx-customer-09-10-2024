package com.fdxUser.app.Models.RestaurantDetailsModels;

import java.util.ArrayList;
import java.util.List;

public class ItemsModel {
    public String id = "";
    public String restaurant_id = "";
    public String category_id = "";
    public String name = "";
    public String image = "";
    public String description = "";
    public String price = "";
    public String offer_price = "";
    public String is_veg = "";
    //public String avalability = "";
    public String addon_status = "";
    public String is_cutlery_required = "";
    public String min_item_for_cutlery = "";
    public String in_stock = "";
    public String status = "";
    public String created_at = "";
    public String updated_at = "";
    public String quantity = "";
    public String is_bestseller = "";
    //public String meal = "";
    public String cart_id = "";
    public int flag = 0;
    public List<AddOnModels> addonItems = new ArrayList<>();
}
