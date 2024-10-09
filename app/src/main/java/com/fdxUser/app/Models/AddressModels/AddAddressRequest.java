package com.fdxUser.app.Models.AddressModels;

public class AddAddressRequest {

    public String user_id = "";
    public String address = "";
    public String location = "";
    public String lat = "";
    public String lng = "";
    public String country = "";
    public String state = "";
    public String city = "";
    public String pin = "";
    public String tag = "";


    public AddAddressRequest(String user_id, String address, String location, String lat,
                             String lng, String country, String state, String city,
                             String pin, String tag) {
        this.user_id = user_id;
        this.address = address;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pin = pin;
        this.tag = tag;
    }
}
