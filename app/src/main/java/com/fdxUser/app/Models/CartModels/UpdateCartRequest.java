package com.fdxUser.app.Models.CartModels;

public class UpdateCartRequest {

    public String id = "";
    public String product_id = "";
    public String product_name = "";
    public String product_description = "";
    public String product_image = "";
    public String price = "";
    public String quantity = "";
    public String add_on_id = "";
    public String add_on_name = "";
    public String add_on_price = "";
    public String add_on_quantity = "";
    public String add_on_id2 = "";
    public String add_on_name2 = "";
    public String add_on_price2 = "";
    public String add_on_quantity2 = "";
    public String is_cutlery_required = "";
    public String cutlery_quantity = "";
    public String cutlery_price = "";

    public UpdateCartRequest(String id, String product_id, String product_name,
                             String product_description, String product_image, String price,
                             String quantity, String add_on_id, String add_on_name,
                             String add_on_price, String add_on_quantity, String add_on_id2,
                             String add_on_name2, String add_on_price2, String add_on_quantity2,
                             String is_cutlery_required, String cutlery_quantity,
                             String cutlery_price) {
        this.id = id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_image = product_image;
        this.price = price;
        this.quantity = quantity;
        this.add_on_id = add_on_id;
        this.add_on_name = add_on_name;
        this.add_on_price = add_on_price;
        this.add_on_quantity = add_on_quantity;
        this.add_on_id2 = add_on_id2;
        this.add_on_name2 = add_on_name2;
        this.add_on_price2 = add_on_price2;
        this.add_on_quantity2 = add_on_quantity2;
        this.is_cutlery_required = is_cutlery_required;
        this.cutlery_quantity = cutlery_quantity;
        this.cutlery_price = cutlery_price;
    }

}
