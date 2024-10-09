package com.fdxUser.app.Activity.RestaurantScreens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Activity.OtherScreens.Notification;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Activity.Profile.Profile;
import com.fdxUser.app.Adapters.BannerAdapter;
import com.fdxUser.app.Adapters.DifferentCatAdapter;
import com.fdxUser.app.Adapters.NearbyRestAdapter;
import com.fdxUser.app.Adapters.PopularRestAdapter;
import com.fdxUser.app.Models.DashboardModels.BannerModel;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//public class Dashboard extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {
public class Dashboard extends AppCompatActivity {

    ImageView ivUser, ivNoti;
    TextView tvDelAddress;
    RecyclerView nearbyRestRv;
    List<RestaurantModel> nearbyRestList = new ArrayList<>();
    List<SpecialItemModel> specialItemList = new ArrayList<>();
    NearbyRestAdapter nbrAdapter;
    DifferentCatAdapter dcAdapter;
    PopularRestAdapter prAdapter;
    //CustChoiceOrderAdapter ccoAdapter;
    RecyclerView difCatRv, popularRestRv, custChoiceRv, bannerRv, offerRv;
    TextView tvNearbyShowAll, tvDifCatAll, tvCustChoiceAll, tvPopAllRest;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    BannerAdapter bannerAdapter;
    TextView etSearch, tv_del_address;

    //SliderLayout mDemoSlider;

    List<BannerModel> bannerList = new ArrayList<>();
    List<CuisinesModel> cuisinesList = new ArrayList<>();
    LocationManager locationManager;

    //FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //setContentView(R.layout.activity_dashboard_home);
        dialogView = new DialogView();

        prefs = new Prefs(Dashboard.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        ivUser = findViewById(R.id.iv_user);
        ivNoti = findViewById(R.id.iv_noti);
        tvNearbyShowAll = findViewById(R.id.tv_nearby_show_all);
        tvDifCatAll = findViewById(R.id.tv_dif_cat_all);
        tvCustChoiceAll = findViewById(R.id.tv_cust_choice_all);
        tvPopAllRest = findViewById(R.id.tvPopAllRest);
        tvDelAddress = findViewById(R.id.tv_del_address);
        nearbyRestRv = findViewById(R.id.nearby_rest_rv);
        difCatRv = findViewById(R.id.dif_cat_rv);
        popularRestRv = findViewById(R.id.pop_rest_rv);
        custChoiceRv = findViewById(R.id.frequent_order_rv);
        bannerRv = findViewById(R.id.bannerRv);
        etSearch = findViewById(R.id.et_search);
        tv_del_address = findViewById(R.id.tv_del_address);
        offerRv = findViewById(R.id.offerRv);

        // client = LocationServices.getFusedLocationProviderClient(this);

        //getAllData();
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
            double lat = locationGPS.getLatitude();
            double longi = locationGPS.getLongitude();


            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
            try {
                Address address = geocoder.getFromLocation(lat, longi, 1).get(0);
                tv_del_address.setText(address.getAddressLine(0));
                //tv_del_address.setText(address.getSubLocality());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //latitude = String.valueOf(lat);
            //longitude = String.valueOf(longi);
            //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
        } else {
            Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
        }



        // getAllRestaurantList();

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, SearchActivity.class));
            }
        });

        popularRestRv.addOnItemTouchListener(new RecyclerItemClickListener(Dashboard.this, popularRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                Constants.TAG = 0;
                startActivity(new Intent(Dashboard.this, RestaurantDetails.class).putExtra(Constants.REST_ID, nearbyRestList.get(position).id));


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        difCatRv.addOnItemTouchListener(new RecyclerItemClickListener(Dashboard.this, difCatRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(Dashboard.this, CuisinesWiseRestaurantActivity.class)
                        .putExtra(Constants.CUISINE_ID, cuisinesList.get(position).id)
                        .putExtra(Constants.CUISINE_NAME, cuisinesList.get(position).title));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        ivNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Notification.class));
            }
        });

        nearbyRestRv.addOnItemTouchListener(new RecyclerItemClickListener(Dashboard.this, nearbyRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                Constants.TAG = 0;
                startActivity(new Intent(Dashboard.this, RestaurantDetails.class).putExtra(Constants.REST_ID, nearbyRestList.get(position).id));


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Profile.class));
            }
        });

        tvNearbyShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "Nearby";

                startActivity(new Intent(Dashboard.this, ShowAllScreen.class));
            }
        });

        tvCustChoiceAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "choice";

                startActivity(new Intent(Dashboard.this, ShowAllScreen.class));
            }
        });

        tvDifCatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.allCatList = cuisinesList;
                ShowAllScreen.fromWhere = "Cat";
                startActivity(new Intent(Dashboard.this, ShowAllScreen.class));

            }
        });

        tvPopAllRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "pop";

                startActivity(new Intent(Dashboard.this, ShowAllScreen.class));

            }
        });


    }

    /*private void getAllData() {
        dialogView.showCustomSpinProgress(Dashboard.this);
        manager.service.getDashboardData().enqueue(new Callback<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, Response<HomeDataResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    HomeDataResponseModel homeDataResponseModel = response.body();
                    if (!homeDataResponseModel.error) {
                        bannerList = homeDataResponseModel.banners;
                        nearbyRestList = homeDataResponseModel.restaurants;
                        cuisinesList = homeDataResponseModel.cuisines;

                        if (bannerList.size() > 0) {
                            bannerAdapter = new BannerAdapter(Dashboard.this, bannerList);
                            bannerRv.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            bannerRv.setAdapter(bannerAdapter);
                            bannerAdapter = new BannerAdapter(Dashboard.this, bannerList);
                            offerRv.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            offerRv.setAdapter(bannerAdapter);
                        }


                        if (nearbyRestList.size() > 0) {
                            nbrAdapter = new NearbyRestAdapter(Dashboard.this, nearbyRestList);
                            nearbyRestRv.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            *//*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*//*
                            nearbyRestRv.setAdapter(nbrAdapter);

                            prAdapter = new PopularRestAdapter(Dashboard.this, nearbyRestList);
                            popularRestRv.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.VERTICAL, false));
                            popularRestRv.setAdapter(prAdapter);

                        }

                        if (cuisinesList.size() > 0) {

                            dcAdapter = new DifferentCatAdapter(Dashboard.this, cuisinesList);
                            difCatRv.setLayoutManager(new GridLayoutManager(Dashboard.this, 4));
                            difCatRv.setAdapter(dcAdapter);

                        }


                    } else {

                      //  Toast.makeText(Dashboard.this, "I am here!", Toast.LENGTH_SHORT).show();
                        if (homeDataResponseModel.message.equals("Token_expired")){
                            showTokenAlert();
                        }

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                   // Toast.makeText(Dashboard.this, "I am here 2!", Toast.LENGTH_SHORT).show();
                   // if (homeDataResponseModel.message.equals("Token_expired")){
                        showTokenAlert();
                    //}
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                //Toast.makeText(Dashboard.this, "I am here 3!", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    private void showTokenAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;

        final AlertDialog alertD = new AlertDialog.Builder(Dashboard.this).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg=(TextView)promptView.findViewById(R.id.tvMsg);
        tvMsg.setText("Session Expired! Please Login Again!");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        btnOk.setBackgroundColor(Dashboard.this.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Constants.isDialogOn = 0;
                startActivity(new Intent(Dashboard.this, EmailLogin.class));
                finish();
                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Constants.isDialogOn = 0;
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        try {
            alertD.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }

    /*private void getAllRestaurantList() {
        dialogView.showCustomSpinProgress(Dashboard.this);

        manager.service.getRestaurants().enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ResponseDataModel rdm = response.body();

                    if (rdm.error == false) {
                        nearbyRestList = rdm.restaurants;

                        if (nearbyRestList.size() > 0) {
                            *//*nbrAdapter = new NearbyRestAdapter(Dashboard.this, nearbyRestList);
                            nearbyRestRv.setLayoutManager(new LinearLayoutManager(Dashboard.this, LinearLayoutManager.HORIZONTAL, false));
                            nearbyRestRv.setAdapter(nbrAdapter);*//*
                        }
                    } else {
                        dialogView.errorButtonDialog(Dashboard.this, getResources().getString(R.string.app_name), "ERROR TRUE!");
                    }

                } else {
                    dialogView.errorButtonDialog(Dashboard.this, getResources().getString(R.string.app_name), "No Data Found!");
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

                Toast.makeText(Dashboard.this, "Something went wrong! please try again!", Toast.LENGTH_SHORT).show();
                dialogView.dismissCustomSpinProgress();

            }
        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        //getAllData();
    }

    /*@Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }*/
}