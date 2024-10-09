package com.fdxUser.app.Activity.EntryPoint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fdxUser.app.Activity.Address.AddAddress;
import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.OtherScreens.ForgotPassword.CheckPhoneNumber;
import com.fdxUser.app.Activity.PolicyScreens.PrivacyPolicy;
import com.fdxUser.app.Activity.PolicyScreens.TermsAndConditions;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.AddressModels.AddressResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.DashboardModels.HomeDataResponseModel;
import com.fdxUser.app.Models.GeoFencingModels.GeoLocationListModel;
import com.fdxUser.app.Models.LoginModels.LoginModel;
import com.fdxUser.app.Models.LoginModels.LoginRequest;
import com.fdxUser.app.Models.TokenUpdateModels.TokenUpdateRequestModel;
import com.fdxUser.app.Models.TokenUpdateModels.TokenUpdateResponseModel;
import com.fdxUser.app.Models.UserDetailsModel;
import com.fdxUser.app.Models.UserModel;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLogin extends AppCompatActivity {

    TextView tvRegisterNow, tvPrivacy, tvTc, tvForgot;
    EditText etEmail, etPass;
    Button btnLogin;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    ApiManagerWithAuth managerAuth = new ApiManagerWithAuth();
    Prefs prefs;
    private String TAG = "EmailLogin";
    String token = "";
    String android_id = "";
    //LocationManager locationManager;
    double lat = 0.0;
    double longi = 0.0;
    boolean inArea = false;
    String loc_Id = "";

    FusedLocationProviderClient mFusedLocationClient;

    List<AddressListModel> addressList = new ArrayList<>();
    List<GeoLocationListModel> geoList = new ArrayList<>();
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        dialogView = new DialogView();
        prefs = new Prefs(EmailLogin.this);
        // mQueue = Volley.newRequestQueue(this);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        tvRegisterNow = findViewById(R.id.tvRegisterNow);
        etPass = findViewById(R.id.etPass);
        etEmail = findViewById(R.id.etEmail);
        btnLogin = findViewById(R.id.btnLogin);
        tvTc = findViewById(R.id.tvTc);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        tvForgot = findViewById(R.id.tvForgot);

        getFirebaseToken();

        android_id = Settings.Secure.getString(EmailLogin.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        getLastLocation();

        //getGeoFencing(lat, longi);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlankValidate()) {

                    LoginRequest loginRequest = new LoginRequest(
                            "+91"+etEmail.getText().toString(),
                            etPass.getText().toString()
                    );

                    getLoggedIn(loginRequest);

                }

            }
        });

        tvTc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLogin.this, TermsAndConditions.class));
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLogin.this, PrivacyPolicy.class));
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailLogin.this, CheckPhoneNumber.class));
                //finish();
            }
        });



        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailLogin.this, Register.class));
            }
        });

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
                    EmailLogin.this,
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


    private void getFirebaseToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.d("firebase>>",token);
                        prefs.saveData(Constants.REFRESH_TOKEN, token);
                        statusCheck();

                        // updateUserToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        //Toast.makeText(EmailLogin.this, "Here it is: "+token+"", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void getLoggedIn(LoginRequest data) {
        dialogView.showCustomSpinProgress(EmailLogin.this);
        Log.d("LOGIN>>", data.toString());
        manager.service.loginWithEmail(data).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {

                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    LoginModel lm = response.body();
                    Log.d("LOGIN-R>>", lm.token);
                    Log.d("code>>",String.valueOf(response.code()));


                    if (lm.error==false) {
                        prefs.saveData(Constants.LOGIN_TOKEN, lm.token);
                        Constants.token = lm.token;
                        getUserDetails();
                        Toast.makeText(EmailLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), lm.message);
                    }


                } else {
                    dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();
                Toast.makeText(EmailLogin.this, "Something went wrong! please try again!", Toast.LENGTH_SHORT).show();

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

    private void updateUserToken(String id) {

        //dialogView.showCustomSpinProgress(EmailLogin.this);


        TokenUpdateRequestModel tokenUpdateRequestModel = new TokenUpdateRequestModel(
                id,
                android_id,
                token
        );

        managerAuth.service.updateDeviceToken(tokenUpdateRequestModel).enqueue(new Callback<TokenUpdateResponseModel>() {
            @Override
            public void onResponse(Call<TokenUpdateResponseModel> call, Response<TokenUpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    TokenUpdateResponseModel turm = response.body();
                    if (!turm.error) {
                        //Toast.makeText(EmailLogin.this, "New user generated!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<TokenUpdateResponseModel> call, Throwable t) {
                //dialogView.dismissCustomSpinProgress();
            }
        });

    }

    private void getUserDetails() {
        managerAuth.service.getUserDetails().enqueue(new Callback<UserDetailsModel>() {
            @Override
            public void onResponse(Call<UserDetailsModel> call, Response<UserDetailsModel> response) {
                if (response.isSuccessful()) {

                    UserDetailsModel udm = response.body();
                    UserModel userModel = udm.user;
                    prefs.saveData(Constants.USER_ID, userModel.id);
                    prefs.saveData(Constants.USER_NAME, userModel.name);
                    prefs.saveData(Constants.USER_MOBILE, userModel.mobile);
                    prefs.saveData(Constants.USER_EMAIL, userModel.email);
                    prefs.saveData(Constants.USER_DOB, userModel.date_of_birth);
                    prefs.saveData(Constants.USER_GENDER, userModel.gender);
                    prefs.saveData(Constants.USER_CITY, userModel.city);
                    prefs.saveData(Constants.USER_ADDRESS, userModel.address);
                    prefs.saveData(Constants.USER_WALLET_ID, userModel.wallet_id);
                    prefs.saveData(Constants.USER_REF_ID, userModel.referal_code);
                    prefs.saveData(Constants.USER_IMG, userModel.image);
                    prefs.saveData(Constants.USER_CANCEL_AMOUNT, userModel.cancel_order_charge);

                    Log.d("cancel>>",userModel.cancel_order_charge);

                    Toast.makeText(EmailLogin.this, "Please wait while fetching your app data!", Toast.LENGTH_SHORT).show();

                    getAddressListData(userModel.id);

                }
            }

            @Override
            public void onFailure(Call<UserDetailsModel> call, Throwable t) {

            }
        });

    }

    private void getAddressListData(String id) {

        dialogView.showCustomSpinProgress(this);

        managerAuth.service.getAddressList(id).enqueue(new Callback<AddressResponseModel>() {
            @Override
            public void onResponse(Call<AddressResponseModel> call, Response<AddressResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AddressResponseModel addressResponseModel = response.body();
                    if (addressResponseModel.error != true) {
                        addressList = addressResponseModel.addresses;
                        updateUserToken(id);
                        if (addressList.size() > 0) {
                            prefs.saveData(Constants.NEW_USER, "0");

                            //startActivity(new Intent(EmailLogin.this, Dashboard.class));
//                            Constants.isNewUser = "0";
//                            startActivity(new Intent(EmailLogin.this, DashboardHome.class));
//                            finish();
                            getAllData(String.valueOf(lat), String.valueOf(longi),prefs.getData(Constants.USER_ID));


                        } else {
                            prefs.saveData(Constants.NEW_USER, "1");
                            AddAddress.is_coming_from = "2";
                            Constants.isNewUser = "1";
                            //Constants.isNewlyOpen = true;
                            AddAddress.coming_from_login = "1";
                            Constants.isFromHome = true;
                            startActivity(new Intent(EmailLogin.this, AddAddress.class));
                            finish();

                        }

                    } else {
                        dialogView.dismissCustomSpinProgress();

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

    private boolean isBlankValidate() {
        if (etEmail.getText().toString().equals("")) {
            dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), "Please enter valid mobile no");
            return false;
        }else if (etEmail.getText().toString().length()<10) {
            dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), "Please enter valid 10 digit mobile no");
            return false;
        } else if (etPass.getText().toString().equals("")) {
            dialogView.errorButtonDialog(EmailLogin.this, getResources().getString(R.string.app_name), "Please enter password!");
            return false;
        } else {
            return true;
        }

    }

    private void getAllData(String lat, String lng,String user_id) {
        dialogView.showCustomSpinProgress(EmailLogin.this);

        manager.service.getDashboardDataForUser(lat, lng, user_id, loc_Id).enqueue(new Callback<HomeDataResponseModel>() {
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
                        Constants.nearRestList.clear();
                        Constants.nearRestList = Constants.nearbyRestList;
                        Constants.cuisinesList = homeDataResponseModel.cuisines;
                        Constants.catList.clear();
                        Constants.catList = Constants.cuisinesList;

                        dialogView.dismissCustomSpinProgress();

                        Intent i;
                        if (prefs.getData(Constants.LOGIN_TOKEN).equals("")) {
                            i = new Intent(EmailLogin.this, Tutorials.class);
                            startActivity(i);
                            finish();
                        } else {
                            if (prefs.getData(Constants.NEW_USER).equals("1")){
                                Constants.isNewlyOpen = true;
                                i = new Intent(EmailLogin.this, AddressList.class);
                                startActivity(i);
                                finish();
                            }else {
                                DashboardHome.inArea = inArea;
                                DashboardHome.loc_Id = loc_Id;
                                i = new Intent(EmailLogin.this, DashboardHome.class);
                                startActivity(i);
                                finish();
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
                    Toast.makeText(EmailLogin.this, "Server Error!", Toast.LENGTH_SHORT).show();
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