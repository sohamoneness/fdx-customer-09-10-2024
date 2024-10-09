package com.fdxUser.app.Models.RestaurantModels;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataModel {
    public boolean error = false;
    public String message = "";
    public List<RestaurantModel> restaurants = new ArrayList<>();
}
