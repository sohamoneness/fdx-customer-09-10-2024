package com.fdxUser.app.Models.CouponModels;

import java.util.ArrayList;
import java.util.List;

public class CouponResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CouponListModel> coupons = new ArrayList<>();
    public List<DeliveryCouponsListModel> delivery_coupons = new ArrayList<>();
}
