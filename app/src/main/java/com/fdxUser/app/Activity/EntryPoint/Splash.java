package com.fdxUser.app.Activity.EntryPoint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.OrderHistory.OrderHistory;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.DashboardModels.HomeDataResponseModel;
import com.fdxUser.app.Models.GeoFencingModels.GeoLocationListModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 4000;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    Prefs prefs;
    String TAG = "Splash";
    //LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    double lat = 0.0;
    double longi = 0.0;

    ApiManager manager1 = new ApiManager();

    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    List<GeoLocationListModel> geoList = new ArrayList<>();
    boolean inArea = false;
    String loc_Id = "";
    private Uri uri;
    String required_param = "";

    private static final String ONESIGNAL_APP_ID = "dced0f1d-2e20-422d-a19d-bb30a07e722b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        uri = getIntent().getData();

        // checking if the uri is null or not.
        if (uri != null) {

            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            List<String> parameters = uri.getPathSegments();

            // after that we are extracting string
            // from that parameters.
            String param = parameters.get(parameters.size() - 1);
            required_param = param;

            // on below line we are setting that string
            // to our text view which we got as params.
            Toast.makeText(Splash.this,param,Toast.LENGTH_SHORT).show();
        }else{
            required_param = "";
        }

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(Splash.this);
        dialogView = new DialogView();
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                checkPermission();
            } else {
                navigationMethod();
            }

        } else {
            if (!checkPermissions()) {
                checkPermission();
            } else {
                navigationMethod();
            }

        }
    }

    public void setUpOneSignal(String email, String mobile){
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.setEmail(email);
        OneSignal.setSMSNumber(mobile);
        
        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
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


    /*@Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }*/
    void navigationMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d("continue>>", "i am running");
                navigation();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    void navigation() {
        if (!prefs.getData(Constants.LOGIN_TOKEN).equals("")) {
            getLastLocation();

        }else {
            Intent i = null;
            i = new Intent(Splash.this, Tutorials.class);
            startActivity(i);
            finish();
        }
        //}
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
                    Splash.this,
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

    private void checkLocationValidation(double latitude, double longitude) {
        GeoCheckRequestModel geoCheckRequestModel = new GeoCheckRequestModel(
                String.valueOf(latitude),
                String.valueOf(longitude)
        );

        Log.d("LAT_LNG>>", geoCheckRequestModel.toString());
        manager1.service.checkLocation(geoCheckRequestModel).enqueue(new Callback<CheckGeoResponseModel>() {
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

                    if (inArea){
                        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);
                        Log.d("TOKEN>>", Constants.token);
                        if (Constants.isFromHome == true){
                            Log.d("LOC_ID1", loc_Id);
                            getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng,prefs.getData(Constants.USER_ID), loc_Id);

                        }else{

                            getAllData(String.valueOf(lat), String.valueOf(longi),prefs.getData(Constants.USER_ID), loc_Id);
                        }

                    }else {
                        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);
                        if (!Constants.token.equals("")){
                            Log.d("LOC_ID2", loc_Id);
                            getAllData(String.valueOf(lat), String.valueOf(longi),prefs.getData(Constants.USER_ID), loc_Id);
                                /*DashboardHome.inArea = inArea;
                                Intent i = new Intent(Splash.this, DashboardHome.class);
                                startActivity(i);
                                finish();*/
                        }else {

                            Intent i = new Intent(Splash.this, EmailLogin.class);
                            startActivity(i);
                            finish();
                        }

                    }

                }else {
                    Gson gson = new Gson();
                    String resp = gson.toJson(response.body());
                    Log.d("RESP1>>", resp);
                    Toast.makeText(Splash.this, "Wrong!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckGeoResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
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
            checkLocationValidation(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        }
    };


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
    //checkPermission
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(Splash.this,Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(Splash.this,Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                Splash.this,Manifest.permission.ACCESS_COARSE_LOCATION)/*+ ContextCompat.checkSelfPermission(
                Splash.this,Manifest.permission.READ_EXTERNAL_STORAGE)+ ContextCompat.checkSelfPermission(
                Splash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)*/
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    Splash.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(
                    Splash.this,Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    Splash.this,Manifest.permission.ACCESS_COARSE_LOCATION)/*|| ActivityCompat.shouldShowRequestPermissionRationale(
                    Splash.this,Manifest.permission.READ_EXTERNAL_STORAGE)|| ActivityCompat.shouldShowRequestPermissionRationale(
                    Splash.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)*/){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setMessage("Camera , Location and Storage" +
                        "  permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                Splash.this,
                                new String[]
                                        {
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                               // Manifest.permission.READ_EXTERNAL_STORAGE,
                                               // Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        startActivity(new Intent(Splash.this,Splash.class));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        Splash.this,
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
        }else {

            navigation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // When request is cancelled, the results array are empty

                if (
                        (grantResults.length > 0) &&
                                (grantResults[0]
                                        + grantResults[1] + grantResults[2] /*+ grantResults[3] + grantResults[4]*/
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ) {
                    navigation();
                } else {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (!checkPermissions()){
                            checkPermission();
                        }else{
                            navigationMethod();
                        }

                    }
                    else
                    {
                        if (!checkPermissions()){
                            checkPermission();
                        }else{
                            navigationMethod();
                        }

                    }
                    /*startActivity(new Intent(Splash.this, Splash.class));
                    finish();*/
                }

                return;
            }
        }
    }

    private void getAllData(String lat, String lng,String user_id, String loc_id) {
        setUpOneSignal(prefs.getData(Constants.USER_EMAIL),prefs.getData(Constants.USER_MOBILE));
        dialogView.showCustomSpinProgress(Splash.this);

        manager.service.getDashboardDataForUser(lat, lng, user_id, loc_id).enqueue(new Callback<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, Response<HomeDataResponseModel> response) {
                if (response.isSuccessful()) {

                    HomeDataResponseModel homeDataResponseModel = response.body();
                    if (!homeDataResponseModel.error) {
                        //skeletonViewDashboard.showOriginal();
                        Constants.bannerList = homeDataResponseModel.banners;
                        Constants.nearbyRestList = homeDataResponseModel.nearby_restaurants;
                        Constants.pocketFriendlyList = homeDataResponseModel.pocket_frinedly_restaurants;

                        Constants.allTimeFavList = homeDataResponseModel.all_time_favourite_restaurants;

                        Constants.popularRestList = homeDataResponseModel.home_restaurants;
                        Constants.cakeList = homeDataResponseModel.cakes_restaurants;
                        Constants.preOrderList = homeDataResponseModel.pre_order_only_restaurants;
                        Constants.offerForYouList = homeDataResponseModel.offers_for_you_restaurants;
                        Constants.referralList = homeDataResponseModel.referral_settings;
                        Constants.nearRestList.clear();
                        Constants.nearRestList = Constants.nearbyRestList;
                        Constants.cuisinesList = homeDataResponseModel.cuisines;
                        Constants.catList.clear();
                        Constants.catList = Constants.cuisinesList;

                        Constants.orderCount = homeDataResponseModel.order_count;

                        dialogView.dismissCustomSpinProgress();

                        Intent i;
                        if (prefs.getData(Constants.LOGIN_TOKEN).equals("")) {
                            i = new Intent(Splash.this, Tutorials.class);
                            startActivity(i);
                            finish();
                        } else {
                            if (Constants.notification_type.equals("1")){
                                i = new Intent(Splash.this, OrderHistory.class);
                                startActivity(i);
                                finish();
                            }else{
                                if (prefs.getData(Constants.NEW_USER).equals("1")){
                                    Constants.isNewlyOpen = true;
                                    i = new Intent(Splash.this, AddressList.class);
                                    startActivity(i);
                                    finish();
                                }else {

                                    if (required_param.equals("")){
                                        DashboardHome.inArea = inArea;
                                        DashboardHome.loc_Id = loc_id;
                                        i = new Intent(Splash.this, DashboardHome.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        i = new Intent(Splash.this, RestaurantDetails.class).putExtra(Constants.REST_ID, required_param);
                                        startActivity(i);
                                        //finish();
                                    }

                                }
                            }


                        }


                    } else {

                        //  Toast.makeText(DashboardHome.this, "I am here!", Toast.LENGTH_SHORT).show();
                        if (homeDataResponseModel.message.equals("Token_expired")){
                            prefs.clearAllData();
                        }

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(Splash.this, "Server Error!", Toast.LENGTH_SHORT).show();
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
}