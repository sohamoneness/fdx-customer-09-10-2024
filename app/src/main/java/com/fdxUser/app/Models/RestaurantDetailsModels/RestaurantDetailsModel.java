package com.fdxUser.app.Models.RestaurantDetailsModels;

import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailsModel {
    public boolean error = true;
    public String message = "";
    public ResponseRestDetailsModel restaurantData;
    public List<OrderHistoryListModel> orders = new ArrayList<>();
}
