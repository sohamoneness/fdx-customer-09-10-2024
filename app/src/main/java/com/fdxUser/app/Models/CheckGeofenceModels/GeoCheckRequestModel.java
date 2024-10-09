package com.fdxUser.app.Models.CheckGeofenceModels;

public class GeoCheckRequestModel {
    public String lat = "";
    public String lng = "";

    public GeoCheckRequestModel(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
