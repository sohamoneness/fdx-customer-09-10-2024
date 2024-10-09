package com.fdxUser.app.Models.RestaurantModels;

import com.fdxUser.app.Models.RestaurantDetailsModels.ItemsModel;

import java.util.ArrayList;
import java.util.List;

public class SearchItemResponseModel {
    public boolean error = false;
    public String message = "";
    public List<ItemsModel> items = new ArrayList<>();
}
