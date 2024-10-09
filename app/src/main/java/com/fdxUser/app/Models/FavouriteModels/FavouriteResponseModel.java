package com.fdxUser.app.Models.FavouriteModels;

import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteResponseModel {
    public boolean error = false;
    public String message = "";
    public List<RestaurantModel> restaurants = new ArrayList<>();
}
