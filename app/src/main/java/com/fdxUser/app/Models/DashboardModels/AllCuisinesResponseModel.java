package com.fdxUser.app.Models.DashboardModels;

import java.util.ArrayList;
import java.util.List;

public class AllCuisinesResponseModel {
    public boolean error = false;
    public String message = "";
    public List<CuisinesModel> cuisines = new ArrayList<>();
}
