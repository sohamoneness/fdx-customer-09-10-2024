package com.fdxUser.app.Activity.OtherScreens;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.FavouriteAdapter;
import com.fdxUser.app.Models.FavouriteModels.FavouriteResponseModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView favouriteRv;
    FavouriteAdapter favAdapter;
    List<RestaurantModel> favRestList = new ArrayList<>();
    LocationManager locationManager;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    double lat = 0.0;
    double longi = 0.0;
    String userId = "";
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        dialogView = new DialogView();
        prefs = new Prefs(FavouriteActivity.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        favouriteRv = findViewById(R.id.favouriteRv);
        ivBack = findViewById(R.id.ivBack);

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
            Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);


            Geocoder geocoder = new Geocoder(FavouriteActivity.this, Locale.getDefault());
            try {
                Address address = geocoder.getFromLocation(lat, longi, 1).get(0);
                //tv_del_address.setText(address.getAddressLine(0));
                //tv_del_address.setText(address.getSubLocality());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //latitude = String.valueOf(lat);
            //longitude = String.valueOf(longi);
            //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
        } else {
            Location locationGPS1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGPS1 != null){
                lat = locationGPS1.getLatitude();
                longi = locationGPS1.getLongitude();
                Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);


                Geocoder geocoder = new Geocoder(FavouriteActivity.this, Locale.getDefault());
                try {
                    Address address = geocoder.getFromLocation(lat, longi, 1).get(0);
                    //tv_del_address.setText(address.getAddressLine(0));
                    //tv_del_address.setText(address.getSubLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                Log.d("LAT>>", "lat: " + lat + "lng: " + longi);
            }

        }

        userId = prefs.getData(Constants.USER_ID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getFavList(String.valueOf(lat), String.valueOf(longi));

    }

    private void getFavList(String lat, String longi) {
        //dialogView.showCustomSpinProgress(FavouriteActivity.this);
        manager.service.getFavRestaurants(userId, lat, longi).enqueue(new Callback<FavouriteResponseModel>() {
            @Override
            public void onResponse(Call<FavouriteResponseModel> call, Response<FavouriteResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    FavouriteResponseModel frm = response.body();
                    if (!frm.error){
                        favRestList = frm.restaurants;
                        if (favRestList.size()>0){
                            favAdapter = new FavouriteAdapter(FavouriteActivity.this, favRestList);
                            favouriteRv.setLayoutManager(new LinearLayoutManager(FavouriteActivity.this, LinearLayoutManager.VERTICAL, false));
                            favouriteRv.setAdapter(favAdapter);
                        }else{

                        }
                    }else{

                    }

                }else {
                   // dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<FavouriteResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

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
    protected void onResume() {
        super.onResume();
        getFavList(String.valueOf(lat), String.valueOf(longi));
    }
}