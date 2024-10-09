package com.fdxUser.app.Activity.OtherScreens;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.CuisinesWiseRestaurantActivity;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Activity.RestaurantScreens.ShowAllScreen;
import com.fdxUser.app.Adapters.DifferentCatAdapter;
import com.fdxUser.app.Adapters.NearbyRestAdapter;
import com.fdxUser.app.Adapters.SearchAdapter;
import com.fdxUser.app.Adapters.SearchCuisineAdapter;
import com.fdxUser.app.Adapters.SearchOrderHistAdapter;
import com.fdxUser.app.Models.DashboardModels.AllCuisinesResponseModel;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;
import com.fdxUser.app.Models.RestaurantModels.ResponseDataModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Models.SearchModels.SearchCustomModel;
import com.fdxUser.app.Models.SearchModels.SearchCustomProductModel;
import com.fdxUser.app.Models.SearchModels.SearchRequestModel;
import com.fdxUser.app.Models.SearchModels.SearchResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView actSearch;
    ImageView ivClose;
    RecyclerView searchRv, catRv, nearRestRv, past_order_rest_rv;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    List<String> productsList = new ArrayList<>();
    List<SearchCustomProductModel> allProductsList = new ArrayList<>();
    List<RestaurantModel> allNearbyRestList = new ArrayList<>();
    List<CuisinesModel> allCatList = new ArrayList<>();
    List<CuisinesModel> searchCatList = new ArrayList<>();
    List<RestaurantModel> searchRestList = new ArrayList<>();
    List<SearchCustomModel> searchCustomList = new ArrayList<>();
    SearchAdapter searchAdapter;
    ArrayAdapter productsAdapter;
    LocationManager locationManager;
    TextView catShowAll, nearShowAll,tvNoPast;
    String key = "";
    double lat = 0.0;
    double longi = 0.0;
    public static String loc_Id = "";
    NestedScrollView showDataLL;
    NearbyRestAdapter nbrAdapter;
    DifferentCatAdapter dcAdapter;
    SearchCuisineAdapter scAdapter1;
    SearchCuisineAdapter scAdapter2;
    SearchOrderHistAdapter ohAdapter;
    List<OrderHistoryListModel> orderHistList = new ArrayList<>();
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(SearchActivity.this);
        dialogView = new DialogView();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        actSearch = findViewById(R.id.actSearch);
        catRv = findViewById(R.id.dif_cat_rv);
        ivClose = findViewById(R.id.ivCross);
        searchRv = findViewById(R.id.searchRv);
        past_order_rest_rv = findViewById(R.id.past_order_rest_rv);
        catShowAll = findViewById(R.id.tv_dif_cat_all);
        showDataLL = findViewById(R.id.showDataScroll);
        nearShowAll = findViewById(R.id.tv_nearby_show_all);
        nearRestRv = findViewById(R.id.nearby_rest_rv);
        tvNoPast = findViewById(R.id.tvNoPast);
        ivBack = findViewById(R.id.ivBack);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowAllScreen.allCatList = cuisinesList;
                ShowAllScreen.fromWhere = "Cat";
                startActivity(new Intent(SearchActivity.this, ShowAllScreen.class));
                finish();
            }
        });

        nearShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.fromWhere = "Nearby";

                startActivity(new Intent(SearchActivity.this, ShowAllScreen.class));
            }
        });

        scAdapter1 = new SearchCuisineAdapter(SearchActivity.this, Constants.catList, Constants.nearRestList, 4, "cat");
        catRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        catRv.setAdapter(scAdapter1);

        scAdapter2 = new SearchCuisineAdapter(SearchActivity.this, Constants.catList, Constants.nearRestList, 4, "rest");
        nearRestRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        nearRestRv.setAdapter(scAdapter2);

        /*nbrAdapter = new NearbyRestAdapter(SearchActivity.this, Constants.nearRestList);
        nearRestRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            *//*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*//*
        nearRestRv.setAdapter(nbrAdapter);*/


        nearRestRv.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, nearRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                Constants.TAG = 0;
                startActivity(new Intent(SearchActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, Constants.nearRestList.get(position).id));


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        catRv.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, catRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(SearchActivity.this, CuisinesWiseRestaurantActivity.class)
                        .putExtra(Constants.CUISINE_ID, Constants.catList.get(position).id)
                        .putExtra(Constants.CUISINE_NAME, Constants.catList.get(position).title));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            lat = locationGPS.getLatitude();
            longi = locationGPS.getLongitude();
        }

        callAllNearByRest(String.valueOf(lat), String.valueOf(longi));

        productsList = new ArrayList<>();

        productsAdapter = new
                ArrayAdapter(this, android.R.layout.simple_list_item_1, productsList);

        actSearch.setAdapter(productsAdapter);
        actSearch.setThreshold(1);

        actSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                key = productsAdapter.getItem(i).toString();

                SearchRequestModel searchRequestModel = new SearchRequestModel(
                    key
                );
                makeSearch(searchRequestModel);
                ivClose.setVisibility(View.VISIBLE);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchRv.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, searchRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (searchCustomList.get(position).type.equals("Cuisine")){
                    startActivity(new Intent(SearchActivity.this, CuisinesWiseRestaurantActivity.class)
                            .putExtra(Constants.CUISINE_ID, searchCustomList.get(position).id)
                            .putExtra(Constants.CUISINE_NAME, searchCustomList.get(position).name)
                    );
                }else{

                    startActivity(new Intent(SearchActivity.this, RestaurantDetails.class)
                            .putExtra(Constants.REST_ID, searchCustomList.get(position).id)
                    );

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

//        past_order_rest_rv.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, past_order_rest_rv, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                OrderSummeryActivity.ordId = orderHistList.get(position).id;
//                startActivity(new Intent(SearchActivity.this, OrderSummeryActivity.class));
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        }));

    }

    private void makeSearch(SearchRequestModel searchRequestModel) {
        dialogView.showCustomSpinProgress(SearchActivity.this);
        manager.service.getSearchData(searchRequestModel).enqueue(new Callback<SearchResponseModel>() {
            @Override
            public void onResponse(Call<SearchResponseModel> call, Response<SearchResponseModel> response) {
                if (response.isSuccessful()){
                   dialogView.dismissCustomSpinProgress();
                   SearchResponseModel searchResponseModel = response.body();
                   if (!searchResponseModel.error){

                       searchCatList = searchResponseModel.cuisines;
                       searchRestList = searchResponseModel.restaurants;

                       for (int i = 0; i < searchCatList.size(); i++){
                           SearchCustomModel searchCustomModel = new SearchCustomModel();
                           searchCustomModel.id = searchCatList.get(i).id;
                           searchCustomModel.name = searchCatList.get(i).title;
                           searchCustomModel.image = searchCatList.get(i).image;
                           searchCustomModel.type = "Cuisine";
                           searchCustomList.add(searchCustomModel);
                       }
                       for (int j = 0; j < searchRestList.size(); j++){
                           SearchCustomModel searchCustomModel = new SearchCustomModel();
                           searchCustomModel.id = searchRestList.get(j).id;
                           searchCustomModel.name = searchRestList.get(j).name;
                           searchCustomModel.image = searchRestList.get(j).image;
                           searchCustomModel.type = "Restaurant";
                           searchCustomList.add(searchCustomModel);
                       }

                       if (searchCustomList.size()>0){
                           searchRv.setVisibility(View.VISIBLE);
                           showDataLL.setVisibility(View.GONE);
                           searchAdapter = new SearchAdapter(SearchActivity.this, searchCustomList);
                           searchRv.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                           searchRv.setAdapter(searchAdapter);
                       }else {
                           searchRv.setVisibility(View.GONE);
                           showDataLL.setVisibility(View.VISIBLE);
                       }

                   }else{

                   }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<SearchResponseModel> call, Throwable t) {
                dialogView.dismissProgress();

            }
        });

    }

    private void callAllNearByRest(String lat, String lng) {
        dialogView.showCustomSpinProgress(SearchActivity.this);

        manager.service.getRestaurants(lat, lng, loc_Id).enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ResponseDataModel rdm = response.body();
                    if (!rdm.error){
                        allNearbyRestList = rdm.restaurants;

                        for (int i = 0; i < allNearbyRestList.size(); i++){
                            productsList.add(allNearbyRestList.get(i).name);
                        }

                        getAllCuisines();



                    }
                }else{
                    dialogView.dismissCustomSpinProgress();

                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getAllCuisines() {
        dialogView.showCustomSpinProgress(this);
        manager.service.getAllCuisines().enqueue(new Callback<AllCuisinesResponseModel>() {
            @Override
            public void onResponse(Call<AllCuisinesResponseModel> call, Response<AllCuisinesResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AllCuisinesResponseModel allCuisinesResponseModel = response.body();

                    if (!allCuisinesResponseModel.error){

                        allCatList = allCuisinesResponseModel.cuisines;
                        for (int i = 0; i < allCatList.size(); i++){
                            productsList.add(allCatList.get(i).title);
                        }

                    }else{

                    }

                    getOrderHistList(prefs.getData(Constants.USER_ID));
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AllCuisinesResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void getOrderHistList(String userID) {
        dialogView.showCustomSpinProgress(SearchActivity.this);
        manager.service.getOrderList(userID).enqueue(new Callback<OrderHistResponseModel>() {
            @Override
            public void onResponse(Call<OrderHistResponseModel> call, Response<OrderHistResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderHistResponseModel orderHistResponseModel = response.body();
                    if (orderHistResponseModel.error != true){

                        orderHistList = orderHistResponseModel.orders;
                        Collections.reverse(orderHistList);
                        /*for(int i = 0; i < orderHistList.size(); i++){

                        }*/

                        if (orderHistList.size() > 0){
                            tvNoPast.setVisibility(View.GONE);
                            past_order_rest_rv.setVisibility(View.VISIBLE);
                            //ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList, orderItemList);
                            ohAdapter = new SearchOrderHistAdapter(SearchActivity.this, orderHistList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            past_order_rest_rv.setLayoutManager(layoutManager);
                            past_order_rest_rv.setAdapter(ohAdapter);
                        }else {
                            tvNoPast.setVisibility(View.VISIBLE);
                            past_order_rest_rv.setVisibility(View.GONE);
                        }

                    }else{

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderHistResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }
}