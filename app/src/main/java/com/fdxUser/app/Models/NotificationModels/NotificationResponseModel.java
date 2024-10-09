package com.fdxUser.app.Models.NotificationModels;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponseModel {
    public boolean error = false;
    public String message = "";
    public List<NotificationModel> notifications = new ArrayList<>();
}
