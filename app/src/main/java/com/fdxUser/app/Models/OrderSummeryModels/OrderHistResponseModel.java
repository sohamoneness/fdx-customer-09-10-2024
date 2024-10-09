package com.fdxUser.app.Models.OrderSummeryModels;

import java.util.ArrayList;
import java.util.List;

public class OrderHistResponseModel {

    public boolean error = false;
    public String message = "";
    public List<OrderHistoryListModel> orders = new ArrayList<>();
    public String total_order_count = "";
    public String is_gift_claimed = "";
}
