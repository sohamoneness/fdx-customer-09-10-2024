package com.fdxUser.app.Models.AddressModels;

import java.util.ArrayList;
import java.util.List;

public class AddressResponseModel {

    public boolean error = false;
    public String message = "";
    public List<AddressListModel> addresses = new ArrayList<>();

}
