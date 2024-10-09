package com.fdxUser.app.Activity.RestaurantScreens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Activity.OrderHistory.OrderHistory;
import com.fdxUser.app.Activity.OtherScreens.Notification;
import com.fdxUser.app.Activity.OtherScreens.OrderStatusActivity;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Activity.Profile.Profile;
import com.fdxUser.app.Adapters.AllTimeFavAdapter;
import com.fdxUser.app.Adapters.BannerAdapter;
import com.fdxUser.app.Adapters.CakesAdapter;
import com.fdxUser.app.Adapters.CustChoiceOrderAdapter;
import com.fdxUser.app.Adapters.DifferentCatAdapter;
import com.fdxUser.app.Adapters.NearbyRestAdapter;
import com.fdxUser.app.Adapters.OfferForYouAdapter;
import com.fdxUser.app.Adapters.PopularRestAdapter;
import com.fdxUser.app.Adapters.PreOrderOnlyAdapter;
import com.fdxUser.app.Fragments.BottomSheets.ClaimGiftBottomSheet;
import com.fdxUser.app.Fragments.BottomSheets.SuggestProductBottomSheet;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CheckGeofenceModels.CheckGeoResponseModel;
import com.fdxUser.app.Models.CheckGeofenceModels.GeoCheckRequestModel;
import com.fdxUser.app.Models.DashboardModels.BannerModel;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.DashboardModels.HomeDataResponseModel;
import com.fdxUser.app.Models.DashboardModels.ReferralSettingsModel;
import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.Models.DemoModels.GeoFenceDemoModel;
import com.fdxUser.app.Models.GeoFencingModels.GeoFenceResponseModel;
import com.fdxUser.app.Models.GeoFencingModels.GeoLocationListModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardHome extends AppCompatActivity {

    ImageView ivUser, ivNoti;
    TextView tvDelAddress, tvCount;
    RecyclerView nearbyRestRv;
    LinearLayout li_coming_soon;
    LinearLayout current_order_li, menu_li;
    RelativeLayout claimGiftRl;
    List<RestaurantModel> nearbyRestList = new ArrayList<>();
    List<RestaurantModel> pocketFriendlyList = new ArrayList<>();
    List<RestaurantModel> popularRestList = new ArrayList<>();
    List<RestaurantModel> offerForYouList = new ArrayList<>();
    List<RestaurantModel> allTimeFavList = new ArrayList<>();
    List<RestaurantModel> cakeList = new ArrayList<>();
    List<RestaurantModel> preOrderList = new ArrayList<>();
    List<SpecialItemModel> specialItemList = new ArrayList<>();
    List<ReferralSettingsModel> refList = new ArrayList<>();
    List<CartsModel> cartList = new ArrayList<>();
    NearbyRestAdapter nbrAdapter;
    DifferentCatAdapter dcAdapter;
    PopularRestAdapter prAdapter;
    CustChoiceOrderAdapter ccoAdapter;
    RecyclerView difCatRv, popularRestRv, pocketFrndRv, bannerRv, offerRv;
    ImageView tvNearbyShowAll, tvDifCatAll, tvCustChoiceAll, tvPopAllRest;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    BannerAdapter bannerAdapter;
    TextView etSearch, tv_del_address, tvCurrentOrderRestaurant, tvCurrentOrderStatus;
    public static boolean inArea = false;
    Button btnChangeLocation;
    public static String loc_Id = "";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    //SliderLayout mDemoSlider;

    List<BannerModel> bannerList = new ArrayList<>();
    List<CuisinesModel> cuisinesList = new ArrayList<>();
    //LocationManager locationManager;

    TextView tvSuggestProduct, tvOffer;
    double lat = 0.0;
    double longi = 0.0;

    RelativeLayout homeRl, profileRl, cartRl, subcriptionRl;

    RecyclerView preOrderRv, allTimeRv, cakesRv;
    ImageView tv_alltime_all, tv_pre_order_all, tv_cake_all, tv_offer_show_all;
    AllTimeFavAdapter allTimeFavAdapter;
    CakesAdapter cakesAdapter;
    PreOrderOnlyAdapter preOrderOnlyAdapter;
    OfferForYouAdapter offerForYouAdapter;
    LinearLayout subcriptionLL, homeLL, allDataLL, allLocDataLL;
    ImageView ivHome, ivCrown;
    TextView tvHome, tvCrown;
    Dialog popupdialog;
    Button btnClaim;
    //skeleton
    //Skeleton skeletonViewDashboard;
    RelativeLayout locationRL;
    RelativeLayout outOfAreaDataRL;
    List<GeoFenceDemoModel> demoFenceList = new ArrayList<>();
    FusedLocationProviderClient mFusedLocationClient;

    //View view;

    //FusedLocationProviderClient client;
    int k = 0;

    List<GeoLocationListModel> geoList = new ArrayList<>();
    List<OrderHistoryListModel> orderHistList = new ArrayList<>();
    ApiManager manager1 = new ApiManager();
    String userID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
        setContentView(R.layout.activity_dashboard_home);

        //view = new View(DashboardHome.this);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }


        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }*/
        dialogView = new DialogView();

        prefs = new Prefs(DashboardHome.this);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        // skeletonViewDashboard = findViewById(R.id.skeletonViewDashboard);
        //mainRL = findViewById(R.id.mainRL);
        ivUser = findViewById(R.id.iv_user);
        ivNoti = findViewById(R.id.iv_noti);
        locationRL = (RelativeLayout) findViewById(R.id.locationRL);
        tv_alltime_all = findViewById(R.id.tv_alltime_all);
        tv_cake_all = findViewById(R.id.tv_cake_all);
        tv_pre_order_all = findViewById(R.id.tv_pre_order_all);
        tvNearbyShowAll = findViewById(R.id.tv_nearby_show_all);
        tvDifCatAll = findViewById(R.id.tv_dif_cat_all);
        tvCustChoiceAll = findViewById(R.id.tv_cust_choice_all);
        tvPopAllRest = findViewById(R.id.tvPopAllRest);
        tvDelAddress = findViewById(R.id.tv_del_address);
        nearbyRestRv = findViewById(R.id.nearby_rest_rv);
        difCatRv = findViewById(R.id.dif_cat_rv);
        popularRestRv = findViewById(R.id.pop_rest_rv);
        pocketFrndRv = findViewById(R.id.pocketFrndRv);
        bannerRv = findViewById(R.id.bannerRv);
        etSearch = findViewById(R.id.et_search);
        btnClaim = findViewById(R.id.btnClaim);
        // tv_del_address = findViewById(R.id.tv_del_address);
        offerRv = findViewById(R.id.offerRv);
        cakesRv = findViewById(R.id.cakesRv);
        allTimeRv = findViewById(R.id.allTimeRv);
        preOrderRv = findViewById(R.id.preOrderRv);
        tvSuggestProduct = findViewById(R.id.tvSuggestProduct);
        homeRl = findViewById(R.id.homeRl);
        profileRl = findViewById(R.id.profileRl);
        cartRl = findViewById(R.id.cartRl);
        subcriptionRl = findViewById(R.id.subcriptionRl);
        subcriptionLL = findViewById(R.id.subcriptionLL);
        homeLL = findViewById(R.id.homeLL);
        ivHome = findViewById(R.id.ivHome);
        ivCrown = findViewById(R.id.ivCrown);
        tvHome = findViewById(R.id.tvHome);
        tvCrown = findViewById(R.id.tvCrown);
        allDataLL = findViewById(R.id.allDataLL);
        tv_offer_show_all = findViewById(R.id.tv_offer_show_all);
        li_coming_soon = findViewById(R.id.li_coming_soon);
        current_order_li = findViewById(R.id.current_order_li);
        tvCurrentOrderRestaurant = findViewById(R.id.tvCurrentOrderRestaurant);
        tvCurrentOrderStatus = findViewById(R.id.tvCurrentOrderStatus);
        claimGiftRl = findViewById(R.id.claimGiftRl);
        menu_li = findViewById(R.id.menu_li);
        outOfAreaDataRL = findViewById(R.id.outOfAreaDataRL);
        allLocDataLL = findViewById(R.id.allLocDataLL);
        btnChangeLocation = findViewById(R.id.btnChangeLocation);
        tvCount = findViewById(R.id.tvCount);
        tvOffer = findViewById(R.id.tvOffer);

        ViewCompat.setNestedScrollingEnabled(popularRestRv, false);

        offerRv.setNestedScrollingEnabled(false);
        nearbyRestRv.setNestedScrollingEnabled(false);
        allTimeRv.setNestedScrollingEnabled(false);
        cakesRv.setNestedScrollingEnabled(false);
        bannerRv.setNestedScrollingEnabled(false);
        popularRestRv.setNestedScrollingEnabled(false);

        //fab = findViewById(R.id.fab);

        popupdialog = new Dialog(DashboardHome.this);
        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);

       // uri = getIntent().getData();





        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //

                if (Constants.addressSelected == true) {
                    tvDelAddress.setText(Constants.addressListModel.address);

                }

                getCartData();


                handler.postDelayed(this, 100);
            }
        };
        runnable.run();


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


        if (inArea) {
            outOfAreaDataRL.setVisibility(View.GONE);
            menu_li.setVisibility(View.VISIBLE);
            allLocDataLL.setVisibility(View.VISIBLE);
            if (Constants.isNewUser.equals("0")) {
                if (Constants.isFromHome == true) {
                    //checkLocationValidation(Double.parseDouble(Constants.addressListModel.lat), Double.parseDouble(Constants.addressListModel.lng));
                    //getGeoFencing(Double.parseDouble(Constants.addressListModel.lat), Double.parseDouble(Constants.addressListModel.lng));
                    getAllData1(Constants.addressListModel.lat, Constants.addressListModel.lng);

                } else {
                    getAllData1(String.valueOf(lat), String.valueOf(longi));
                }
            } else {
                if (Constants.isFromHome == true) {
                    //checkLocationValidation(Double.parseDouble(Constants.addressListModel.lat), Double.parseDouble(Constants.addressListModel.lng));
                    getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));

                } else {
                    getAllData(String.valueOf(lat), String.valueOf(longi), prefs.getData(Constants.USER_ID));
                }
            }

        } else {
            current_order_li.setVisibility(View.GONE);
            outOfAreaDataRL.setVisibility(View.VISIBLE);
            menu_li.setVisibility(View.GONE);
            allLocDataLL.setVisibility(View.GONE);
        }
        /*if (Constants.isNewUser.equals("0")) {
            if (Constants.isFromHome == true) {
                getAllData1(Constants.addressListModel.lat, Constants.addressListModel.lng);

            } else {
                getAllData1(String.valueOf(lat), String.valueOf(longi));
            }
        }else {
            if (Constants.isFromHome == true) {
                getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));

            } else {
                getAllData(String.valueOf(lat), String.valueOf(longi),prefs.getData(Constants.USER_ID));
            }
        }*/


        locationRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.isFromHome = true;
                Constants.isFor = "";
                startActivity(new Intent(DashboardHome.this, AddressList.class));
            }
        });

        profileRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardHome.this, Profile.class));
            }
        });

        cartRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isFromHomeToHistory = true;
                startActivity(new Intent(DashboardHome.this, OrderHistory.class));
            }
        });

        homeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  homeLL.setBackground(getResources().getDrawable(R.drawable.side_round_orange_bg));
                tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
                //ivHome.setColorFilter(Color.argb(1, 255, 255, 255));
                ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                //subcriptionLL.setBackgroundResource(R.color.white);
                tvCrown.setTextColor(getResources().getColor(R.color.black));
                //ivCrown.setColorFilter(Color.argb(1, 0, 0, 0));
                ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                //ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
                if (popupdialog.isShowing()) {
                    popupdialog.dismiss();
                }

            }
        });

        subcriptionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //subcriptionLL.setBackground(getResources().getDrawable(R.drawable.side_round_orange_bg));
                tvCrown.setTextColor(getResources().getColor(R.color.colorAccent));
                //ivCrown.setColorFilter(Color.argb(1, 255, 255, 255));
                ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                //ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                // homeLL.setBackgroundResource(R.color.white);
                tvHome.setTextColor(getResources().getColor(R.color.black));
                //ivHome.setColorFilter(Color.argb(1, 0, 0, 0));
                ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), PorterDuff.Mode.SRC_IN);
                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    subcriptionRl.setTooltipText("Coming Soon!");
                }*/
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    fab.setTooltipText("Coming Soon!");
                }*/

                //showPopup();

                li_coming_soon.setVisibility(View.VISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Do some stuff
                                li_coming_soon.setVisibility(View.GONE);
                                tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
                                //ivHome.setColorFilter(Color.argb(1, 255, 255, 255));
                                ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                                //subcriptionLL.setBackgroundResource(R.color.white);
                                tvCrown.setTextColor(getResources().getColor(R.color.black));
                                //ivCrown.setColorFilter(Color.argb(1, 0, 0, 0));
                                ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
            }
        });

        btnChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.isFromHome = true;
                Constants.isFor = "";
                startActivity(new Intent(DashboardHome.this, AddressList.class));
            }
        });


        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.loc_Id = loc_Id;
                startActivity(new Intent(DashboardHome.this, SearchActivity.class));
            }
        });

        tvSuggestProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuggestProductBottomSheet suggestBottomSheet = new SuggestProductBottomSheet();
                suggestBottomSheet.show((DashboardHome.this).getSupportFragmentManager(), "callAddOn");
            }
        });

       /* popularRestRv.addOnItemTouchListener(new RecyclerItemClickListener(DashboardHome.this, popularRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //showPopup(position);
                //Constants.TAG = 0;
                Intent intent = new Intent(context, RestaurantDetails.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(DashboardHome.this, new Pair<View, String>());
                //ActivityCompat.startActivity(context, intent, options.toBundle());
               // startActivity(new Intent(DashboardHome.this, RestaurantDetails.class).putExtra(Constants.REST_ID, nearbyRestList.get(position).id));

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/


        difCatRv.addOnItemTouchListener(new RecyclerItemClickListener(DashboardHome.this, difCatRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CuisinesWiseRestaurantActivity.lat = lat;
                CuisinesWiseRestaurantActivity.longi = longi;
                CuisinesWiseRestaurantActivity.loc_Id = loc_Id;
                Log.d("LOC", loc_Id);
                startActivity(new Intent(DashboardHome.this, CuisinesWiseRestaurantActivity.class)
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
                startActivity(new Intent(DashboardHome.this, Notification.class));
            }
        });

        /*pocketFrndRv.addOnItemTouchListener(new RecyclerItemClickListener(DashboardHome.this, nearbyRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                Constants.TAG = 0;
                startActivity(new Intent(DashboardHome.this, RestaurantDetails.class).putExtra(Constants.REST_ID, nearbyRestList.get(position).id));

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/

       /* nearbyRestRv.addOnItemTouchListener(new RecyclerItemClickListener(DashboardHome.this, nearbyRestRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //showPopup(position);
                Constants.TAG = 0;
                startActivity(new Intent(DashboardHome.this, RestaurantDetails.class).putExtra(Constants.REST_ID, nearbyRestList.get(position).id));


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartActivity.isForm = "Home";
                startActivity(new Intent(DashboardHome.this, CartActivity.class));
            }
        });

        tvNearbyShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "Nearby";
                ShowAllScreen.loc_Id = loc_Id;

                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));
            }
        });

        tvCustChoiceAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "pocket";
                ShowAllScreen.loc_Id = loc_Id;

                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));
            }
        });

        tvDifCatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.allCatList = cuisinesList;
                ShowAllScreen.fromWhere = "Cat";
                ShowAllScreen.loc_Id = loc_Id;
                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));

            }
        });

        tvPopAllRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowAllScreen.allNearbyRestList = nearbyRestList;
                ShowAllScreen.fromWhere = "pop";
                ShowAllScreen.loc_Id = loc_Id;
                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));

            }
        });

        tv_alltime_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.allNearbyRestList = allTimeFavList;
                ShowAllScreen.fromWhere = "Pickedforyou";
                ShowAllScreen.loc_Id = loc_Id;
                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));
            }
        });

        tv_pre_order_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.allNearbyRestList = preOrderList;
                ShowAllScreen.fromWhere = "choice";
                ShowAllScreen.loc_Id = loc_Id;
                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));

            }
        });

        tv_cake_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAllScreen.allNearbyRestList = cakeList;
                ShowAllScreen.fromWhere = "Cakes";
                ShowAllScreen.loc_Id = loc_Id;

                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));
            }
        });

        tv_offer_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAllScreen.allNearbyRestList = offerForYouList;
                ShowAllScreen.fromWhere = "OfferForYou";
                ShowAllScreen.loc_Id = loc_Id;

                startActivity(new Intent(DashboardHome.this, ShowAllScreen.class));
            }
        });

        claimGiftRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaimGiftBottomSheet claimGiftBottomSheet = new ClaimGiftBottomSheet();
                claimGiftBottomSheet.show(DashboardHome.this.getSupportFragmentManager(), "callClaimGift");
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaimGiftBottomSheet claimGiftBottomSheet = new ClaimGiftBottomSheet();
                claimGiftBottomSheet.show(DashboardHome.this.getSupportFragmentManager(), "callClaimGift");
            }
        });

        userID = prefs.getData(Constants.USER_ID);

        getCurrentOrders(userID);
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
                            Log.d("NEW_LAT>>", location.getLatitude() + "");
                            Log.d("NEW_LNG>>", location.getLongitude() + "");
                            lat = location.getLatitude();
                            longi = location.getLongitude();
                            Geocoder geocoder = new Geocoder(DashboardHome.this, Locale.getDefault());
                            try {
                                Address address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                                tvDelAddress.setText(address.getAddressLine(0));
                                //tv_del_address.setText(address.getSubLocality());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //getGeoFencing(location.getLatitude(), location.getLongitude());
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
                    DashboardHome.this,
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
            Log.d("Latitude: ", mLastLocation.getLatitude() + "");
            Log.d("Longitude: ", mLastLocation.getLongitude() + "");
            lat = mLastLocation.getLatitude();
            longi = mLastLocation.getLongitude();
            //getGeoFencing(mLastLocation.getLatitude(), mLastLocation.getLongitude());
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
                        outOfAreaDataRL.setVisibility(View.GONE);
                        menu_li.setVisibility(View.VISIBLE);
                        allLocDataLL.setVisibility(View.VISIBLE);
                        Log.d("NEW_USER>>", Constants.isNewUser);
                        if (Constants.isNewUser.equals("0")) {
                            if (Constants.isFromHome == true) {
                                getAllData1(Constants.addressListModel.lat, Constants.addressListModel.lng);

                            } else {
                                getAllData1(String.valueOf(lat), String.valueOf(longi));
                            }
                        } else {
                            if (Constants.isFromHome == true) {
                                getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));

                            } else {
                                getAllData(String.valueOf(lat), String.valueOf(longi), prefs.getData(Constants.USER_ID));
                            }
                        }

                    } else {
                        current_order_li.setVisibility(View.GONE);
                        outOfAreaDataRL.setVisibility(View.VISIBLE);
                        menu_li.setVisibility(View.GONE);
                        allLocDataLL.setVisibility(View.GONE);
                    }


                } else {
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

    private void getGeoFencing(double lat, double longi) {
        manager1.service.getGeoFenceLocations().enqueue(new Callback<GeoFenceResponseModel>() {
            @Override
            public void onResponse(Call<GeoFenceResponseModel> call, Response<GeoFenceResponseModel> response) {
                if (response.isSuccessful()) {
                    GeoFenceResponseModel gfrm = response.body();
                    if (!gfrm.error) {
                        geoList = gfrm.locations;
                        Log.d("HOME_GEO>>", geoList.size() + "");
                        if (geoList.size() > 0) {
                            for (int i = 0; i < geoList.size(); i++) {
                                for (int j = 0; j < geoList.get(i).geo_fencing.size(); j++) {
                                    if (String.valueOf(lat).equals(geoList.get(i).geo_fencing.get(j).lat)
                                            && String.valueOf(longi).equals(geoList.get(i).geo_fencing.get(j).lng)) {
                                        inArea = true;
                                        loc_Id = geoList.get(i).geo_fencing.get(j).location_id;
                                        break;
                                    } else {
                                        inArea = false;
                                    }
                                }
                            }
                        }
                        if (inArea) {
                            outOfAreaDataRL.setVisibility(View.GONE);
                            menu_li.setVisibility(View.VISIBLE);
                            allLocDataLL.setVisibility(View.VISIBLE);
                            Log.d("NEW_USER>>", Constants.isNewUser);
                            if (Constants.isNewUser.equals("0")) {
                                if (Constants.isFromHome == true) {
                                    getAllData1(Constants.addressListModel.lat, Constants.addressListModel.lng);

                                } else {
                                    getAllData1(String.valueOf(lat), String.valueOf(longi));
                                }
                            } else {
                                if (Constants.isFromHome == true) {
                                    getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));

                                } else {
                                    getAllData(String.valueOf(lat), String.valueOf(longi), prefs.getData(Constants.USER_ID));
                                }
                            }

                        } else {
                            current_order_li.setVisibility(View.GONE);
                            outOfAreaDataRL.setVisibility(View.VISIBLE);
                            menu_li.setVisibility(View.GONE);
                            allLocDataLL.setVisibility(View.GONE);
                        }
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<GeoFenceResponseModel> call, Throwable t) {

            }
        });
    }

    private void getCartData() {
        manager.service.getCartList(prefs.getData(Constants.USER_ID)).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                    CartItemsResponseModel cirm = response.body();
                    if (!cirm.error) {
                        cartList = cirm.carts;
                        int cartSize = cartList.size();
                        if (cartSize != 0) {
                            tvCount.setText(String.valueOf(cartSize));
                            tvCount.setVisibility(View.VISIBLE);
                        } else {
                            tvCount.setVisibility(View.GONE);
                            prefs.saveData(Constants.RestId_FromCart, "");
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<CartItemsResponseModel> call, Throwable t) {

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

    private void showPopup() {
        //final Dialog popupdialog = new Dialog(DashboardHome.this);
        popupdialog.setContentView(R.layout.coming_soon_popup_lay);

        popupdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final ImageView ivCross = (ImageView) popupdialog.findViewById(R.id.ivCross);

        ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
                //ivHome.setColorFilter(Color.argb(1, 255, 255, 255));
                ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                //subcriptionLL.setBackgroundResource(R.color.white);
                tvCrown.setTextColor(getResources().getColor(R.color.black));
                //ivCrown.setColorFilter(Color.argb(1, 0, 0, 0));
                ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                popupdialog.dismiss();
            }
        });

        popupdialog.setCanceledOnTouchOutside(false);
        popupdialog.setCancelable(false);

        popupdialog.show();
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

    private void getAllData1(String lat, String lng) {
        bannerList = Constants.bannerList;
        nearbyRestList = Constants.nearbyRestList;
        pocketFriendlyList = Constants.pocketFriendlyList;

        allTimeFavList = Constants.allTimeFavList;

        popularRestList = Constants.popularRestList;
        cakeList = Constants.cakeList;
        preOrderList = Constants.preOrderList;
        offerForYouList = Constants.offerForYouList;
        //Constants.nearRestList.clear();
        Constants.nearRestList = Constants.nearbyRestList;
        //cuisinesList = homeDataResponseModel.cuisines;
        //Constants.catList.clear();
        //Constants.catList = cuisinesList;
        cuisinesList = Constants.cuisinesList;

        allDataLL.setVisibility(View.VISIBLE);

        if (bannerList.size() > 0) {
                            /*bannerAdapter = new BannerAdapter(DashboardHome.this, bannerList);
                            bannerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            bannerRv.setAdapter(bannerAdapter);*/
            tvOffer.setVisibility(View.VISIBLE);
            offerRv.setVisibility(View.VISIBLE);
            bannerAdapter = new BannerAdapter(DashboardHome.this, bannerList);
            offerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
            offerRv.setAdapter(bannerAdapter);
        } else {
            tvOffer.setVisibility(View.GONE);
            offerRv.setVisibility(View.GONE);
        }


        if (nearbyRestList.size() > 0) {

            nbrAdapter = new NearbyRestAdapter(DashboardHome.this, nearbyRestList);
            nearbyRestRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            /*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*/
            nearbyRestRv.setAdapter(nbrAdapter);

            allTimeFavAdapter = new AllTimeFavAdapter(DashboardHome.this, allTimeFavList);
            allTimeRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
            allTimeRv.setAdapter(allTimeFavAdapter);

            cakesAdapter = new CakesAdapter(DashboardHome.this, cakeList);
            cakesRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
            cakesRv.setAdapter(cakesAdapter);

            offerForYouAdapter = new OfferForYouAdapter(DashboardHome.this, offerForYouList);
            bannerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
            bannerRv.setAdapter(offerForYouAdapter);

            prAdapter = new PopularRestAdapter(DashboardHome.this, popularRestList);
            popularRestRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.VERTICAL, false));
            popularRestRv.setAdapter(prAdapter);

                            /*preOrderOnlyAdapter = new PreOrderOnlyAdapter(DashboardHome.this, nearbyRestList);
                            preOrderRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            preOrderRv.setAdapter(preOrderOnlyAdapter);*/

        }

        if (cuisinesList.size() > 0) {

            dcAdapter = new DifferentCatAdapter(DashboardHome.this, cuisinesList, 8);
            difCatRv.setLayoutManager(new GridLayoutManager(DashboardHome.this, 4));
            difCatRv.setAdapter(dcAdapter);

        }

        if (pocketFriendlyList.size() > 0) {

            ccoAdapter = new CustChoiceOrderAdapter(DashboardHome.this, pocketFriendlyList);
            pocketFrndRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            /*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*/
            pocketFrndRv.setAdapter(ccoAdapter);

            preOrderOnlyAdapter = new PreOrderOnlyAdapter(DashboardHome.this, preOrderList);
            preOrderRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
            preOrderRv.setAdapter(preOrderOnlyAdapter);
        }
    }

    private void getAllData(String lat, String lng, String user_id) {
        dialogView.showCustomSpinProgress(DashboardHome.this);

        manager.service.getDashboardDataForUser(lat, lng, user_id, loc_Id).enqueue(new Callback<HomeDataResponseModel>() {
            @Override
            public void onResponse(Call<HomeDataResponseModel> call, Response<HomeDataResponseModel> response) {
                if (response.isSuccessful()) {

                    HomeDataResponseModel homeDataResponseModel = response.body();
                    if (!homeDataResponseModel.error) {
                        //skeletonViewDashboard.showOriginal();
                        bannerList = homeDataResponseModel.banners;
                        nearbyRestList = homeDataResponseModel.nearby_restaurants;
                        pocketFriendlyList = homeDataResponseModel.pocket_frinedly_restaurants;

                        allTimeFavList = homeDataResponseModel.all_time_favourite_restaurants;
                        refList = homeDataResponseModel.referral_settings;
                        Constants.referralList = refList;

                        popularRestList = homeDataResponseModel.home_restaurants;
                        cakeList = homeDataResponseModel.cakes_restaurants;
                        preOrderList = homeDataResponseModel.pre_order_only_restaurants;
                        offerForYouList = homeDataResponseModel.offers_for_you_restaurants;
                        Constants.nearRestList.clear();
                        Constants.nearRestList = nearbyRestList;
                        cuisinesList = homeDataResponseModel.cuisines;
                        Constants.catList.clear();
                        Constants.catList = cuisinesList;

                        Constants.orderCount = homeDataResponseModel.order_count;

                        try {
                            PackageInfo pInfo = DashboardHome.this.getPackageManager().getPackageInfo(DashboardHome.this.getPackageName(), 0);
                            String version = pInfo.versionName;
                            int verCode = pInfo.versionCode;
                            //Log.e("version>>",String.valueOf(verCode));
                            //Log.e("version old>>",Constants.version_code);
                            try {
                                if (verCode < Integer.parseInt(homeDataResponseModel.version_code)) {
                                    //startActivity(new Intent(MenuActivity.this, OldVersionActivity.class));
                                    showNewUpdatePopup();
                                }
                            } catch (NumberFormatException e) {

                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        allDataLL.setVisibility(View.VISIBLE);

                        if (bannerList.size() > 0) {
                            /*bannerAdapter = new BannerAdapter(DashboardHome.this, bannerList);
                            bannerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            bannerRv.setAdapter(bannerAdapter);*/
                            tvOffer.setVisibility(View.VISIBLE);
                            offerRv.setVisibility(View.VISIBLE);
                            bannerAdapter = new BannerAdapter(DashboardHome.this, bannerList);
                            offerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            offerRv.setAdapter(bannerAdapter);
                        } else {
                            tvOffer.setVisibility(View.GONE);
                            offerRv.setVisibility(View.GONE);
                        }


                        if (nearbyRestList.size() > 0) {

                            nbrAdapter = new NearbyRestAdapter(DashboardHome.this, nearbyRestList);
                            nearbyRestRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            /*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*/
                            nearbyRestRv.setAdapter(nbrAdapter);

                            allTimeFavAdapter = new AllTimeFavAdapter(DashboardHome.this, allTimeFavList);
                            allTimeRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            allTimeRv.setAdapter(allTimeFavAdapter);

                            cakesAdapter = new CakesAdapter(DashboardHome.this, cakeList);
                            cakesRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            cakesRv.setAdapter(cakesAdapter);

                            offerForYouAdapter = new OfferForYouAdapter(DashboardHome.this, offerForYouList);
                            bannerRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            bannerRv.setAdapter(offerForYouAdapter);

                            prAdapter = new PopularRestAdapter(DashboardHome.this, popularRestList);
                            popularRestRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.VERTICAL, false));
                            popularRestRv.setAdapter(prAdapter);

                            /*preOrderOnlyAdapter = new PreOrderOnlyAdapter(DashboardHome.this, nearbyRestList);
                            preOrderRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            preOrderRv.setAdapter(preOrderOnlyAdapter);*/

                        }

                        if (cuisinesList.size() > 0) {

                            dcAdapter = new DifferentCatAdapter(DashboardHome.this, cuisinesList, 8);
                            difCatRv.setLayoutManager(new GridLayoutManager(DashboardHome.this, 4));
                            difCatRv.setAdapter(dcAdapter);

                        }

                        if (pocketFriendlyList.size() > 0) {

                            ccoAdapter = new CustChoiceOrderAdapter(DashboardHome.this, pocketFriendlyList);
                            pocketFrndRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            /*nearbyRestRv.setHasFixedSize(true);
                            nearbyRestRv.setItemViewCacheSize(6);*/
                            pocketFrndRv.setAdapter(ccoAdapter);

                            preOrderOnlyAdapter = new PreOrderOnlyAdapter(DashboardHome.this, preOrderList);
                            preOrderRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            preOrderRv.setAdapter(preOrderOnlyAdapter);
                        }

                        dialogView.dismissCustomSpinProgress();
                    } else {
                        dialogView.dismissCustomSpinProgress();

                        //  Toast.makeText(DashboardHome.this, "I am here!", Toast.LENGTH_SHORT).show();
                        if (homeDataResponseModel.message.equals("Token_expired")) {
                            prefs.clearAllData();
                            showTokenAlert();
                        }

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(DashboardHome.this, "Server Error!", Toast.LENGTH_SHORT).show();
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

    private void showNewUpdatePopup() {
        Dialog dialog = new Dialog(DashboardHome.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.update_alert_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.fdxUser.app"));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    private void showTokenAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(DashboardHome.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;

        final AlertDialog alertD = new AlertDialog.Builder(DashboardHome.this).create();
        TextView tvHeader = (TextView) promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg = (TextView) promptView.findViewById(R.id.tvMsg);
        tvMsg.setText("Session Expired! Please Login Again!");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        btnOk.setBackgroundColor(DashboardHome.this.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Constants.isDialogOn = 0;
                startActivity(new Intent(DashboardHome.this, EmailLogin.class));
                prefs.clearAllData();
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
            alertD.setCancelable(false);
            alertD.setCanceledOnTouchOutside(false);
            alertD.show();

        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }

    }

    /*private void getAllRestaurantList() {
        dialogView.showCustomSpinProgress(DashboardHome.this);

        manager.service.getRestaurants().enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ResponseDataModel rdm = response.body();

                    if (rdm.error == false) {
                        nearbyRestList = rdm.restaurants;

                        if (nearbyRestList.size() > 0) {
                            *//*nbrAdapter = new NearbyRestAdapter(DashboardHome.this, nearbyRestList);
                            nearbyRestRv.setLayoutManager(new LinearLayoutManager(DashboardHome.this, LinearLayoutManager.HORIZONTAL, false));
                            nearbyRestRv.setAdapter(nbrAdapter);*//*
                        }
                    } else {
                        dialogView.errorButtonDialog(DashboardHome.this, getResources().getString(R.string.app_name), "ERROR TRUE!");
                    }

                } else {
                    dialogView.errorButtonDialog(DashboardHome.this, getResources().getString(R.string.app_name), "No Data Found!");
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

                Toast.makeText(DashboardHome.this, "Something went wrong! please try again!", Toast.LENGTH_SHORT).show();
                dialogView.dismissCustomSpinProgress();

            }
        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);
        userID = prefs.getData(Constants.USER_ID);

        if (Constants.addressSelected) {
            Log.d("LAT_LNG_X>>", Constants.addressListModel.lat + ", " + Constants.addressListModel.lng);
            checkLocationValidation(Double.parseDouble(Constants.addressListModel.lat), Double.parseDouble(Constants.addressListModel.lng));
        }

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

        if (Constants.isNewUser.equals("0")) {
            if (Constants.isFromHome == true) {

                if (Constants.popularRestList.size() > 0) {
                    getAllData1(Constants.addressListModel.lat, Constants.addressListModel.lng);
                } else {
                    getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));
                }


            } else {
                if (Constants.popularRestList.size() > 0) {
                    getAllData1(String.valueOf(lat), String.valueOf(longi));
                } else {
                    getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));
                }
            }
        } else {
            if (Constants.isFromHome == true) {
                getAllData(Constants.addressListModel.lat, Constants.addressListModel.lng, prefs.getData(Constants.USER_ID));

            } else {
                getAllData(String.valueOf(lat), String.valueOf(longi), prefs.getData(Constants.USER_ID));
            }
        }

        getCurrentOrders(userID);
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

    @Override
    public void onBackPressed() {

        k++;
        if (k == 1) {
            Toast.makeText(DashboardHome.this, "Click one more time to exit app", Toast.LENGTH_SHORT).show();
        } else {
            //exit app to home screen
            Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeScreenIntent);
        }
    }

    private void getCurrentOrders(String userID) {
        //dialogView.showCustomSpinProgress(DashboardHome.this);
        manager.service.getCurrentOrders(userID).enqueue(new Callback<OrderHistResponseModel>() {
            @Override
            public void onResponse(Call<OrderHistResponseModel> call, Response<OrderHistResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    OrderHistResponseModel orderHistResponseModel = response.body();
                    if (orderHistResponseModel.error != true) {

                        orderHistList = orderHistResponseModel.orders;
                        String isClaimed = orderHistResponseModel.is_gift_claimed;

                        if (isClaimed.equals("0")) {
                            claimGiftRl.setVisibility(View.VISIBLE);
                        } else {
                            claimGiftRl.setVisibility(View.GONE);
                        }

                        if (orderHistList.size() > 0) {
                            current_order_li.setVisibility(View.VISIBLE);
                            tvCurrentOrderRestaurant.setText(orderHistList.get(0).restaurant.name);
                            if (orderHistList.get(0).status.equals("1")) {
                                tvCurrentOrderStatus.setText("Waiting for restaurant to accept order");
                            } else if (orderHistList.get(0).status.equals("2")) {
                                tvCurrentOrderStatus.setText("Restaurant has accepted the order");
                            } else if (orderHistList.get(0).status.equals("3")) {
                                tvCurrentOrderStatus.setText("Rider has been assigned");
                            } else if (orderHistList.get(0).status.equals("4")) {
                                tvCurrentOrderStatus.setText("Order on process");
                            } else if (orderHistList.get(0).status.equals("5")) {
                                tvCurrentOrderStatus.setText("Rider has started towards restaurant");
                            } else if (orderHistList.get(0).status.equals("6")) {
                                tvCurrentOrderStatus.setText("Rider has reached restaurant");
                            } else if (orderHistList.get(0).status.equals("7")) {
                                tvCurrentOrderStatus.setText("Rider has picked your order");
                            }
                        } else {
                            current_order_li.setVisibility(View.GONE);
                        }

                        Constants.total_order_count = orderHistResponseModel.total_order_count;

                        current_order_li.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                OrderStatusActivity.orderId = orderHistList.get(0).id;
                                OrderStatusActivity.from = "Home";
                                startActivity(new Intent(DashboardHome.this, OrderStatusActivity.class));
                                finish();
                                /*Constants.isFromHomeToOrderStat = true;
                                OrderSummeryActivity.ordId = orderHistList.get(0).id;
                                startActivity(new Intent(DashboardHome.this, OrderSummeryActivity.class));*/
                            }
                        });

                    } else {

                    }

                } else {
                    // dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderHistResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });
    }
}