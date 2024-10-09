package com.fdxUser.app.Models.RestaurantDetailsModels;

import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class ResponseRestDetailsModel {
    public RestaurantModel restaurant;
    public List<ItemCategoryModel> categories = new ArrayList<>();
    public List<RestReviewModel> reviews = new ArrayList<>();
    public List<RestWiseCouponModel> coupons = new ArrayList<>();
}

