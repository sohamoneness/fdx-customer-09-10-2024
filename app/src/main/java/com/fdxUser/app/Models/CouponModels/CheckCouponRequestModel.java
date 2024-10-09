package com.fdxUser.app.Models.CouponModels;

public class CheckCouponRequestModel {
    public String user_id = "";
    public String code = "";

    public CheckCouponRequestModel(String user_id, String code) {
        this.user_id = user_id;
        this.code = code;
    }
}
