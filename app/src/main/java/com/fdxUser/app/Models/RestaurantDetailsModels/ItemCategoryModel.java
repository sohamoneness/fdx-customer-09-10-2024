package com.fdxUser.app.Models.RestaurantDetailsModels;

import java.util.ArrayList;
import java.util.List;

public class ItemCategoryModel {

    public String id = "";
    public String restaurant_id = "";
    public String title = "";
    public String description = "";
    public String image = "";
    public String status = "";
    public String created_at = "";
    public String updated_at = "";
    public List<ItemsModel> items = new ArrayList<>();

}

