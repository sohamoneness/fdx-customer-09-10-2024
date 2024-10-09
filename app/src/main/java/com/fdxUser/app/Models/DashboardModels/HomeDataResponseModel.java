package com.fdxUser.app.Models.DashboardModels;

import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class HomeDataResponseModel {

    public boolean error = false;
    public String message = "";
    public List<BannerModel> banners = new ArrayList<>();
    public List<CuisinesModel> cuisines = new ArrayList();
    public List<RestaurantModel> restaurants = new ArrayList();
    public List<RestaurantModel> pocket_frinedly_restaurants = new ArrayList();
    public List<RestaurantModel> home_restaurants = new ArrayList();
    public List<RestaurantModel> nearby_restaurants = new ArrayList();
    public List<RestaurantModel> offers_for_you_restaurants = new ArrayList();
    public List<RestaurantModel> all_time_favourite_restaurants = new ArrayList();
    public List<RestaurantModel> cakes_restaurants = new ArrayList();
    public List<RestaurantModel> pre_order_only_restaurants = new ArrayList();
    public String order_count = "";
    public String version_code = "";
    public List<ReferralSettingsModel> referral_settings = new ArrayList();
}
