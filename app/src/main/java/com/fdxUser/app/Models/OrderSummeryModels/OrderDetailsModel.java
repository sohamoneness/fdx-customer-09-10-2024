package com.fdxUser.app.Models.OrderSummeryModels;

import com.fdxUser.app.Models.RestaurantModels.BoyModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsModel {

    public String id = "";
    public String unique_id = "";
    public String restaurant_id = "";
    public String user_id = "";
    public String name = "";
    public String mobile = "";
    public String email = "";
    public String delivery_address = "";
    public String delivery_landmark = "";
    public String delivery_country = "";
    public String delivery_city = "";
    public String delivery_pin = "";
    public String delivery_lat = "";
    public String delivery_lng = "";
    public String amount = "";
    public String coupon_code = "";
    public String discounted_amount = "";
    public String delivery_charge = "";
    public String packing_price = "";
    public String tax_amount = "";
    public String total_amount = "";
    public String status = "";
    public String cancelled_by = "";
    public String cancellation_reason = "";
    public String refund_status = "";
    public String transaction_id = "";
    public String payment_status = "";
    public String is_deleted = "";
    public String created_at = "";
    public String updated_at = "";
    public String delivery_boy_id = "";
    public String extra_preparation_time = "";
    public String is_ready = "";
    public String is_dispatched = "";
    public String dispatch_otp = "";
    public String preparation_time = "";
    public String notes = "";
    public String tips = "";
    public String time_diff = "";
    public String is_cancellable = "";
    public String timer = "";
    public String cancel_order_charge = "";
    public String cancel_order_reason = "";
    public RestaurantModel restaurant;
    public List<OrderItemModel> items = new ArrayList<>();
    public BoyModel boy;
    public String order_type = "";
}
