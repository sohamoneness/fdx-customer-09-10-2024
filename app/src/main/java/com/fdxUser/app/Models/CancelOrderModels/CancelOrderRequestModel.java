package com.fdxUser.app.Models.CancelOrderModels;

public class CancelOrderRequestModel {
    public String id = "";
    public String cancellation_reason = "";

    public CancelOrderRequestModel(String id, String cancellation_reason) {
        this.id = id;
        this.cancellation_reason = cancellation_reason;
    }
}
