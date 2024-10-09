package com.fdxUser.app.Models.SearchModels;

import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CuisinesModel> cuisines = new ArrayList<>();
    public List<RestaurantModel> restaurants = new ArrayList<>();
}
