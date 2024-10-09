package com.fdxUser.app.Models.GeoFencingModels;

import java.util.ArrayList;
import java.util.List;

public class GeoFenceResponseModel {
    public boolean error = false;
    public String message = "";
    public List<GeoLocationListModel> locations = new ArrayList();
}
