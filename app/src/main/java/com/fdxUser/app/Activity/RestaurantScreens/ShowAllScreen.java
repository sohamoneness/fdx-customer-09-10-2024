package com.fdxUser.app.Activity.RestaurantScreens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Adapters.CustChoiceOrderAdapter;
import com.fdxUser.app.Adapters.DifferentCatAdapter;
import com.fdxUser.app.Adapters.NearbyRestAdapter;
import com.fdxUser.app.Adapters.PopularRestAdapter;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.DashboardModels.AllCuisinesResponseModel;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.RestaurantModels.ResponseDataModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowAllScreen extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView nearRestShowAllRv;
    NearbyRestAdapter nbAdapter;
    CustChoiceOrderAdapter ccoAdapter;
    //DifferentCatAdapter dcAdapter;
    TextView tvHeaderTxt;
    DifferentCatAdapter dcAdapter;
    PopularRestAdapter popRestAdapter;
    ImageView ivSearch;
    //LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    double lat = 0.0;
    double longi = 0.0;

    public static List<RestaurantModel> allNearbyRestList = new ArrayList<>();
    public static List<RestaurantModel> allPopRestList = new ArrayList<>();
    public static List<CuisinesModel> allCatList = new ArrayList<>();

    public static String fromWhere = "";
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;
    Prefs prefs;
    int count = 0;
    ProgressBar pbLoader;
    NestedScrollView nestedSv;
    boolean inArea = false;
    public static String loc_Id = "";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    //SkeletonScreen skeletonScreen;
    //SkeletonScreen skeletonScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_screen);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }


        prefs = new Prefs(this);
        dialogView = new DialogView();
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ivBack = findViewById(R.id.iv_back);
        ivSearch = findViewById(R.id.ivSearch);
        nearRestShowAllRv = findViewById(R.id.near_all_rest_rv);
        tvHeaderTxt = findViewById(R.id.tv_head_text);
        //pbLoader = findViewById(R.id.pbLoader);
        //nestedSv = findViewById(R.id.nestedSv);

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

        getLastLocation();
        /*Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            lat = locationGPS.getLatitude();
            longi = locationGPS.getLongitude();
            Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);

            //latitude = String.valueOf(lat);
            //longitude = String.valueOf(longi);
            //showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
        } else {
            Location locationGPS1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGPS1 != null){
                lat = locationGPS1.getLatitude();
                longi = locationGPS1.getLongitude();
                Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);

            }else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                Log.d("LAT>>", "lat: " + lat + "lng: " + longi);
            }

        }*/



        if (fromWhere.equals("Cat")){

            tvHeaderTxt.setText("Top Cuisines");

            callAllCuisinesList(String.valueOf(lat), String.valueOf(longi));


        }else if (fromWhere.equals("Nearby")){

            tvHeaderTxt.setText("Nearby Restaurants");

            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);
            Log.d("NEAR>>", "LAT-LNG" + String.valueOf(lat) + ", " + String.valueOf(longi));

        }else if (fromWhere.equals("pop")){

            tvHeaderTxt.setText("Popular Restaurants");

            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);

        }else if (fromWhere.equals("choice")){

            tvHeaderTxt.setText("Pre Order Only");

            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);

        }else if (fromWhere.equals("pocket")){

            tvHeaderTxt.setText("Pocket Friendly");

            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);

        }else if (fromWhere.equals("OfferForYou")){
            tvHeaderTxt.setText("Offers Near You");
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);
        }else if (fromWhere.equals("Pickedforyou")){
            tvHeaderTxt.setText("All Time Favourite");
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);
        }else if (fromWhere.equals("Cakes")){
            tvHeaderTxt.setText("Cakes & Desserts");
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi), loc_Id);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllScreen.this, SearchActivity.class));
            }
        });

        nearRestShowAllRv.addOnItemTouchListener(new RecyclerItemClickListener(ShowAllScreen.this, nearRestShowAllRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                if (fromWhere.equals("Cat")) {
                    //Constants.TAG = 0;
                    CuisinesWiseRestaurantActivity.loc_Id = loc_Id;
                    startActivity(new Intent(ShowAllScreen.this, CuisinesWiseRestaurantActivity.class)
                            .putExtra(Constants.CUISINE_ID, allCatList.get(position).id)
                            .putExtra(Constants.CUISINE_NAME, allCatList.get(position).title));
                }else if(fromWhere.equals("pop")){
                    Constants.TAG = 0;
                    startActivity(new Intent(ShowAllScreen.this, RestaurantDetails.class)
                            .putExtra(Constants.REST_ID, allPopRestList.get(position).id)
                    );
                }else{
                    Constants.TAG = 0;
                    startActivity(new Intent(ShowAllScreen.this, RestaurantDetails.class)
                            .putExtra(Constants.REST_ID, allNearbyRestList.get(position).id)
                    );
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        //getData();
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

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.d("NEW_LAT>>",location.getLatitude() + "");
                            Log.d("NEW_LNG>>",location.getLongitude() + "");
                            lat = location.getLatitude();
                            longi = location.getLongitude();
                            checkLocationValidation(location.getLatitude(), location.getLongitude());

                            //getGeoFencing(location.getLatitude(), location.getLongitude());
                            //getGeoFencing(23.0803291, 88.4899213);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            ActivityCompat.requestPermissions(
                    ShowAllScreen.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            //Manifest.permission.READ_EXTERNAL_STORAGE,
                            //Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    MY_PERMISSIONS_REQUEST_CODE
            );

        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED /*&& ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED*/;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.d("Latitude: " ,mLastLocation.getLatitude() + "");
            Log.d("Longitude: " , mLastLocation.getLongitude() + "");
            lat = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();
            checkLocationValidation(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        }
    };

    private void checkLocationValidation(double latitude, double longitude) {
        GeoCheckRequestModel geoCheckRequestModel = new GeoCheckRequestModel(
                String.valueOf(latitude),
                String.valueOf(longitude)
        );
        Log.d("LAT_LNG>>", geoCheckRequestModel.toString());
        manager.service.checkLocation(geoCheckRequestModel).enqueue(new Callback<CheckGeoResponseModel>() {
            @Override
            public void onResponse(Call<CheckGeoResponseModel> call, Response<CheckGeoResponseModel> response) {
                Gson gson1 = new Gson();
                String resp1 = gson1.toJson(response.body());
                Log.d("RESP>>", resp1);
                if (response.isSuccessful()){

                    CheckGeoResponseModel cgrm = response.body();
                    if (!cgrm.error){
                        inArea = true;
                        loc_Id = cgrm.location_id;
                    }else {
                        inArea = false;
                        loc_Id = "";
                    }


                }else {
                    Gson gson = new Gson();
                    String resp = gson.toJson(response.body());
                    Log.d("RESP1>>", resp);
                    //Toast.makeText(EmailLogin.this, "Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckGeoResponseModel> call, Throwable t) {

            }
        });
    }


    /*private void getData() {
        if (fromWhere.equals("Cat")){
            callAllCuisinesList(String.valueOf(lat), String.valueOf(longi));
        }else if (fromWhere.equals("Nearby")){
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi));
        }else if (fromWhere.equals("pop")){
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi));
        }else if (fromWhere.equals("choice")){
            callAllNearByRest(String.valueOf(lat), String.valueOf(longi));
        }*/


    private void callAllNearByRest(String lat, String lng, String locId) {
        dialogView.showCustomSpinProgress(ShowAllScreen.this);

        Log.d("loc_ID", locId);

        manager.service.getRestaurants(lat, lng, locId).enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    ResponseDataModel rdm = response.body();
                    if (!rdm.error){
                        allNearbyRestList = rdm.restaurants;
                        allPopRestList = rdm.restaurants;

                        if (allNearbyRestList.size()>0){
                            /*if(fromWhere.equals("Nearby")){
                                nbAdapter = new NearbyRestAdapter(ShowAllScreen.this, allNearbyRestList);
                                nearRestShowAllRv.setLayoutManager(new LinearLayoutManager(ShowAllScreen.this, LinearLayoutManager.VERTICAL, false));
                                nearRestShowAllRv.setAdapter(nbAdapter);
                            }else {*/
                                popRestAdapter = new PopularRestAdapter(ShowAllScreen.this, allNearbyRestList);
                                nearRestShowAllRv.setLayoutManager(new LinearLayoutManager(ShowAllScreen.this, LinearLayoutManager.VERTICAL, false));
                                nearRestShowAllRv.setAdapter(popRestAdapter);

                            //}
                        }
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

    private void callAllCuisinesList(String lat, String lng) {
        dialogView.showCustomSpinProgress(this);
        manager.service.getAllCuisines().enqueue(new Callback<AllCuisinesResponseModel>() {
            @Override
            public void onResponse(Call<AllCuisinesResponseModel> call, Response<AllCuisinesResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AllCuisinesResponseModel allCuisinesResponseModel = response.body();

                    if (!allCuisinesResponseModel.error){
                        allCatList = allCuisinesResponseModel.cuisines;
                        if (allCatList.size()>0){
                            dcAdapter = new DifferentCatAdapter(ShowAllScreen.this, allCatList);
                            nearRestShowAllRv.setLayoutManager(new GridLayoutManager(ShowAllScreen.this, 4));
                            nearRestShowAllRv.setAdapter(dcAdapter);
                        }
                    }else{

                    }
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
}