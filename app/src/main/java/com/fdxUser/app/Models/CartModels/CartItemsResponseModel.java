package com.fdxUser.app.Models.CartModels;

import java.util.ArrayList;
import java.util.List;

public class CartItemsResponseModel {

    public boolean error = false;
    public String message = "";
    public List<CartsModel> carts = new ArrayList<>();
    public List<UpsellItemModel> upsell_items = new ArrayList<>();
    public String discounted_amount = "";
    public String delivery_charge = "";
    public String packing_price = "";
    public String tax_amount = "";
    public String tax_rate = "";
    public String total_amount = "";
    public String cart_amount = "";
    public String offer_discount = "";
    public String min_discount_to_go = "";
    public String offer_id = "";

}
