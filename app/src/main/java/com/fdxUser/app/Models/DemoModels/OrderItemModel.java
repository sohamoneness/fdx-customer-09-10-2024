package com.fdxUser.app.Models.DemoModels;

public class OrderItemModel {
    public boolean isVeg = false;
    public String food_name = "";
    public String food_sub_name = "";
    public String amount = "";

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_sub_name() {
        return food_sub_name;
    }

    public void setFood_sub_name(String food_sub_name) {
        this.food_sub_name = food_sub_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
