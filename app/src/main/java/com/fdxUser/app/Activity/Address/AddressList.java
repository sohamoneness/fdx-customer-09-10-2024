package com.fdxUser.app.Activity.Address;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.Profile.Profile;
import com.fdxUser.app.Activity.RestaurantScreens.CheckOutActivity;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Adapters.AddressAdapter;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.AddressModels.AddressResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.DashboardModels.HomeDataResponseModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressList extends AppCompatActivity {

    RelativeLayout addAddressLL;
    RecyclerView addressRv;
    ImageView ivBack;
    List<AddressListModel> addressList = new ArrayList<>();
    public static List<CartsModel> cartItemList = new ArrayList<>();
    AddressAdapter adrsAdapter;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    ApiManager apiManager = new ApiManager();
    Prefs prefs;
    DialogView dialogView;
    String userID = "", restId = "";
    Button btnSelect;
    AddressListModel addressListModel = new AddressListModel();
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String TAG = "AddressList";
    final static int REQUEST_LOCATION = 199;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static String coming_from_login = "0";
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    boolean inArea = false;
    String loc_Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(AddressList.this);
        dialogView = new DialogView();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        userID = prefs.getData(Constants.USER_ID);
        if (Constants.isFor.equals("Select")) {
            restId = getIntent().getStringExtra(Constants.REST_ID);
        }


        ivBack = findViewById(R.id.iv_back);
        addAddressLL = findViewById(R.id.add_address_ll);
        addressRv = findViewById(R.id.address_rv);
        btnSelect = findViewById(R.id.btnSelect);

        Log.d("HOME>>", Constants.isFromHome + "");

        if (Constants.isFromHome == true) {

            btnSelect.setVisibility(View.VISIBLE);
            addAddressLL.setVisibility(View.VISIBLE);

        }else if (Constants.isFromAddressSave == true) {

            btnSelect.setVisibility(View.VISIBLE);
            addAddressLL.setVisibility(View.VISIBLE);

        } else {
            if (Constants.isFor.equals("Select")) {
                btnSelect.setVisibility(View.VISIBLE);
                restId = getIntent().getStringExtra(Constants.REST_ID);

            } else {
                btnSelect.setVisibility(View.GONE);
            }
        }

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions();
            }
        } else {
            displayLocationSettingsRequest(AddressList.this);
        }


        getAddressListData(userID);


        addressRv.addOnItemTouchListener(new RecyclerItemClickListener(AddressList.this, addressRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*Constants.isFor = "Edit";
                startActivity(new Intent(AddressList.this, AddAddress.class));*/
                Log.d("HERE>>", "ALL IN");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showPopup(addressList.get(position).id, position);
                Log.d("HERE2>>", "ALL IN");

            }
        }));

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckOutActivity.cartItemsResponseModel = Constants.cartItemsResponseModel;

                if(addressList.size()>0){
                    addressListModel = addressList.get(0);
                }
                for (int i = 0; i < addressList.size(); i++) {
                    if (addressList.get(i).flag == 1) {
                        addressListModel = addressList.get(i);
                    }
                }
                Constants.addressListModel = addressListModel;
                Constants.addressSelected = true;
                Constants.isDefault = false;
                Log.d("ADR_LAT>>", Constants.addressListModel.lat + ", " + Constants.addressListModel.lng);

                if (Constants.isFromHome == true) {
                    checkLocationValidation(Double.parseDouble(Constants.addressListModel.lat), Double.parseDouble(Constants.addressListModel.lng));
                    //startActivity(new Intent(AddressList.this, DashboardHome.class));
                   // Constants.isFromHome = false;
                    //finish();
                } else {
                    startActivity(new Intent(AddressList.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restId));
                    finish();
                }

            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addAddressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.isFor.equals("Select")) {
                    Constants.fromAddressSearch = false;
                    startActivity(new Intent(AddressList.this, AddAddress.class).putExtra(Constants.REST_ID, restId));
                    finish();
                } else {
                    Constants.fromAddressSearch = false;
                    startActivity(new Intent(AddressList.this, AddAddress.class));
                    finish();
                }


            }
        });


    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //getLastLocation();
            } else {
                /*if (!checkPermissions()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions();
                    }
                }else {
                    displayLocationSettingsRequest(AddressList.this);
                }*/
            }
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(AddressList.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void checkLocationValidation(double latitude, double longitude) {
        GeoCheckRequestModel geoCheckRequestModel = new GeoCheckRequestModel(
                String.valueOf(latitude),
                String.valueOf(longitude)
        );
        Log.d("LAT_LNG>>", geoCheckRequestModel.toString());
        manager.service.checkLocation(geoCheckRequestModel).enqueue(new Callback<CheckGeoResponseModel>() {
            @Override
            public void onResponse(Call<CheckGeoResponseModel> call, Response<CheckGeoResponseModel> response) {

                if (response.isSuccessful()) {
                    CheckGeoResponseModel cgrm = response.body();
                    if (!cgrm.error) {
                        inArea = true;
                        loc_Id = cgrm.location_id;
                    } else {
                        inArea = false;
                        loc_Id = "";
                    }

                    if (inArea) {

                        if (Constants.isFromHome == true) {
                            Log.d("MY_ID", prefs.getData(Constants.USER_ID));
                            getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID), loc_Id);
                        }
                    } else {
                        if (Constants.isFromHome == true) {
                            getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID), loc_Id);

                        }
                    }

                } else {

                }

        }

        @Override
        public void onFailure (Call < CheckGeoResponseModel > call, Throwable t){

        }
    });
}

    private void getAllData(String lat, String lng, String user_id, String locId) {
        dialogView.showCustomSpinProgress(AddressList.this);

        apiManager.service.getDashboardDataForUser(lat, lng, user_id, locId).enqueue(new Callback<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, Response<HomeDataResponseModel> response) {
                if (response.isSuccessful()) {

                    HomeDataResponseModel homeDataResponseModel = response.body();
                    if (!homeDataResponseModel.error) {
                        //skeletonViewDashboard.showOriginal();
                        Constants.bannerList.clear();
                        Constants.bannerList = homeDataResponseModel.banners;
                        Constants.nearbyRestList = homeDataResponseModel.nearby_restaurants;
                        Constants.pocketFriendlyList = homeDataResponseModel.pocket_frinedly_restaurants;

                        Constants.allTimeFavList = homeDataResponseModel.all_time_favourite_restaurants;

                        Constants.popularRestList = homeDataResponseModel.home_restaurants;
                        Constants.cakeList = homeDataResponseModel.cakes_restaurants;
                        Constants.preOrderList = homeDataResponseModel.pre_order_only_restaurants;
                        Constants.offerForYouList = homeDataResponseModel.offers_for_you_restaurants;
                        Constants.nearRestList.clear();
                        Constants.nearRestList = Constants.nearbyRestList;
                        Constants.cuisinesList = homeDataResponseModel.cuisines;
                        Constants.catList.clear();
                        Constants.catList = Constants.cuisinesList;

                        dialogView.dismissCustomSpinProgress();

                        Intent i;

                        DashboardHome.inArea = inArea;
                        i = new Intent(AddressList.this, DashboardHome.class);
                        startActivity(i);
                        Constants.isFromHome = false;
                        finish();


                    } else {

                        //  Toast.makeText(DashboardHome.this, "I am here!", Toast.LENGTH_SHORT).show();
                        if (homeDataResponseModel.message.equals("Token_expired")) {
                            prefs.clearAllData();
                        }

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(AddressList.this, "Server Error!", Toast.LENGTH_SHORT).show();
                    // Toast.makeText(DashboardHome.this, "I am here 2!", Toast.LENGTH_SHORT).show();
                    // if (homeDataResponseModel.message.equals("Token_expired")){
                    //prefs.clearAllData();
                    //showTokenAlert();
                    //}
                }
            }

            @Override
            public void onFailure(Call<HomeDataResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                //Toast.makeText(DashboardHome.this, "I am here 3!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showPopup(String id, int pos) {
        LayoutInflater layoutInflater = LayoutInflater.from(AddressList.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(AddressList.this).create();
        TextView tvHeader = (TextView) promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg = (TextView) promptView.findViewById(R.id.tvMsg);
        tvMsg.setText("Are you sure to remove this address?");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.isFor = "Edit";

                AddAddress.addressListModel = addressList.get(pos);


                startActivity(new Intent(AddressList.this, AddAddress.class));

                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        try {
            alertD.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }

    private void getAddressListData(String uId) {

        dialogView.showCustomSpinProgress(this);

        manager.service.getAddressList(uId).enqueue(new Callback<AddressResponseModel>() {
            @Override
            public void onResponse(Call<AddressResponseModel> call, Response<AddressResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AddressResponseModel addressResponseModel = response.body();
                    if (addressResponseModel.error != true) {
                        addressList = addressResponseModel.addresses;
                        if (addressList.size() > 0) {
                            //btnSelect.setVisibility(View.VISIBLE);
                            adrsAdapter = new AddressAdapter(AddressList.this, addressList);
                            addressRv.setLayoutManager(new LinearLayoutManager(AddressList.this, LinearLayoutManager.VERTICAL, false));
                            addressRv.setAdapter(adrsAdapter);

                        } else {
                            // btnSelect.setVisibility(View.GONE);
                        }

                    } else {

                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddressResponseModel> call, Throwable t) {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (addressList.size() == 0) {
            Toast.makeText(this, "No address added", Toast.LENGTH_SHORT).show();
        } else {
            if (Constants.isNewUser.equals("1")) {
                startActivity(new Intent(AddressList.this, DashboardHome.class));
                Constants.isNewUser = "0";
                prefs.saveData(Constants.NEW_USER, "0");
            } else if (Constants.isNewlyOpen) {
                Constants.isNewlyOpen = false;
                startActivity(new Intent(AddressList.this, DashboardHome.class));
                finish();

            } else if (Constants.isFromHome) {
                Constants.isFromHome = false;
                startActivity(new Intent(AddressList.this, DashboardHome.class));
                finish();
            } else {
                if (Constants.isFor.equals("Select")) {
                    CheckOutActivity.cartItemList = cartItemList;
                    CheckOutActivity.cartItemsResponseModel = Constants.cartItemsResponseModel;
                    //startActivity(new Intent(CartActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
                    startActivity(new Intent(AddressList.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restId));
                    finish();
                } else {
                    startActivity(new Intent(AddressList.this, Profile.class));
                    finish();
                }

            }
        }
        //finishAffinity();
    }
}