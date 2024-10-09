package com.fdxUser.app.Models.WalletModels;

public class WalletCreditRequestModel {
    public String user_id = "";
    public String amount = "";
    public String transaction_id = "";
    public String comment = "";

    public WalletCreditRequestModel(String user_id, String amount, String transaction_id, String comment){
        this.user_id = user_id;
        this.amount = amount;
        this.transaction_id = transaction_id;
        this.comment = comment;
    }
}
