package com.fdxUser.app.Activity.RestaurantScreens;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Activity.OtherScreens.AllCouponsActivity;
import com.fdxUser.app.Adapters.CategoryItemAdapter;
import com.fdxUser.app.Adapters.FeaturedItemAdapter;
import com.fdxUser.app.Adapters.FilterCatAdapter;
import com.fdxUser.app.Adapters.MenuItemAdapter;
import com.fdxUser.app.Adapters.RestWiseCouponAdapter;
import com.fdxUser.app.Adapters.RestaurantOrderHistAdapter;
import com.fdxUser.app.Adapters.ReviewRestAdapter;
import com.fdxUser.app.CustomFonts.ManropeBoldTextView;
import com.fdxUser.app.Fragments.BottomSheets.PricingFeesBottomSheet;
import com.fdxUser.app.Fragments.BottomSheets.ReviewsBottomSheet;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.Models.DemoModels.MenuItemModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavResponseModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ResponseRestDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestReviewModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestWiseCouponModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsRequestModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;
import com.fdxUser.app.Utills.SliderHelper.SliderAdapter;
import com.fdxUser.app.Utills.ViewPager2Adapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetails extends AppCompatActivity implements Animation.AnimationListener {

    TabLayout tabLayout, menuTabLay;
    ViewPager viewPager, menuViewPager;
    LinearLayout menuLL, revLL;
    RecyclerView catRv, revRv;

    MenuItemAdapter miAdapter;
    ReviewRestAdapter riAdapter;
    RelativeLayout viewCartRL;

    List<CartsModel> cartList = new ArrayList<>();
    List<CartsModel> newCartList = new ArrayList<>();
    List<CustomCartModel> customCartDataList = new ArrayList<>();
    SliderAdapter sliderAdapter;
    SliderView imgSlider;

    ViewPager2 catViewPager;

    CircleImageView ivRestLogo;

    //ReviewRestAdapter reviewRestAdapter;
    TextView tvAll, tvVeg, tvNonveg;

    TextView tvCartPrice, tvCartQty, tvViewCart, tvPricingFees;
    TextView tv_rest_name, tv_rest_address, tvRating, tvEstTime, tvDist, tvReviewCount;

    List<MenuItemModel> menuItemList = new ArrayList<>();
    //List<ReviewsModel> reviewsList = new ArrayList<>();
//API
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    String restID = "";

    List<ItemCategoryModel> categoryList = new ArrayList<>();
    //List<ItemCategoryModel> categorySubList = new ArrayList<>();
    //List<ItemCategoryModel> categorySubList2 = new ArrayList<>();
    List<RestReviewModel> reviewList = new ArrayList<>();
    public static List<ItemsModel> itemList = new ArrayList<>();
    List<RestWiseCouponModel> restWiseCouponList = new ArrayList<>();

    TextView tvHeaderTxt, tvCloseTime;
    ImageView ivCart, ivBack;
    ShapeableImageView iv_rest_img;
    TextView tvNoData, et_search, tvOpenStat, tvNoPast;
    Toolbar mToolbar;
    AppBarLayout mAppBarLayout;

    LocationManager locationManager;
    double lat = 0.0;
    double longi = 0.0;

    CategoryItemAdapter ciAdapter;
    ImageView ivGoto;
    List<RestWiseCouponModel> couponList = new ArrayList<>();
    RecyclerView couponsRv;
    LinearLayout noCouponLL;
    RestWiseCouponAdapter restWiseCouponAdapter;

    List<SpecialItemModel> specialItemList = new ArrayList<>();
    RecyclerView featuredItemRv, catFilterRv, menuRv;
    FeaturedItemAdapter fiAdapter;
    CollapsingToolbarLayout collapsing_toolbar;
    Toolbar toolbar;
    FilterCatAdapter filterCatAdapter;
    //NestedScrollView nestedScrollingView;
    LinearLayoutManager layoutManager;
    ProgressBar progressbar;
    ImageView ivShare, ivFav;
    ManropeBoldTextView tvPreTime;

    LinearLayout vegLL, nonvegLL;
    RelativeLayout allDataRL;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    String isFav = "";
    String storeAddress = "", storeName = "", storeOpen = "", storeLat = "", storeLng = "", storeClose = "";
    private boolean isScrolling;

    Uri uri;
    String restaurant_name = "";

    RecyclerView past_order_rest_rv;
    RestaurantOrderHistAdapter ohAdapter;
    List<OrderHistoryListModel> orderHistList = new ArrayList<>();

    RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uri = getIntent().getData();

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
//        }
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_restaurant_details);
        //getWindow().setExitTransition(new Explode());

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeStatusBarColor();


        dialogView = new DialogView();

        prefs = new Prefs(RestaurantDetails.this);
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        //mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);


        if (getIntent().getStringExtra(Constants.REST_ID) != null) {
            restID = getIntent().getStringExtra(Constants.REST_ID);
            Constants.restIdForCartUpdate = restID;
        } else {
            restID = prefs.getData(Constants.REST_ID);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        //progressbar = findViewById(R.id.progressbar);
        noCouponLL = findViewById(R.id.noCouponLL);
        allDataRL = findViewById(R.id.allDataRL);
        vegLL = findViewById(R.id.vegLL);
        ivGoto = findViewById(R.id.ivGoto);
        nonvegLL = findViewById(R.id.nonvegLL);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tv_rest_name = findViewById(R.id.tv_rest_name);
        tv_rest_address = findViewById(R.id.tv_rest_address);
        et_search = findViewById(R.id.et_search);
        tvRating = findViewById(R.id.tvRating);
        tvReviewCount = findViewById(R.id.tvReviewCount);
        tvEstTime = findViewById(R.id.tvEstTime);
        tvDist = findViewById(R.id.tvDist);
        menuTabLay = findViewById(R.id.menu_tab_layout);
        menuLL = findViewById(R.id.menu_ll);
        revLL = findViewById(R.id.rev_ll);
        revRv = findViewById(R.id.rev_rv);
        tvOpenStat = findViewById(R.id.tvOpenStat);
        tvPreTime = findViewById(R.id.tvPreTime);
        //menuRv = findViewById(R.id.menu_rv);
        couponsRv = findViewById(R.id.couponsRv);
        catFilterRv = findViewById(R.id.catFilterRv);
        //menuRv = findViewById(R.id.menu_rv);
        //catRv = findViewById(R.id.catRv);
        catViewPager = findViewById(R.id.catViewPager);
        tvNoPast = findViewById(R.id.tvNoPast);
        viewCartRL = findViewById(R.id.viewCartRL);
        tvCartPrice = findViewById(R.id.tvCartPrice);
        tvCartQty = findViewById(R.id.tvCartQty);
        tvViewCart = findViewById(R.id.tvViewCart);
        tvHeaderTxt = findViewById(R.id.tvHeaderTxt);
        ivCart = findViewById(R.id.ivCart);
        ivBack = findViewById(R.id.iv_back);
        ivFav = findViewById(R.id.ivFav);
        iv_rest_img = findViewById(R.id.iv_rest_img);
        tvNoData = findViewById(R.id.tvNoData);
        featuredItemRv = findViewById(R.id.featuredItemRv);
        tvCloseTime = findViewById(R.id.tvCloseTime);
        tvAll = findViewById(R.id.tvAll);
        tvVeg = findViewById(R.id.tvVeg);
        tvNonveg = findViewById(R.id.tvNonveg);
        ivShare = findViewById(R.id.ivShare);
        //nestedScrollingView = findViewById(R.id.nestedScrollingView);
        tvPricingFees = findViewById(R.id.tvPricingFees);
        past_order_rest_rv = findViewById(R.id.past_order_rest_rv);
        imgSlider = findViewById(R.id.imgSlider);
        ivRestLogo = findViewById(R.id.ivRestLogo);
        //menuViewPager = findViewById(R.id.menu_view_pager);

        //collapsing_toolbar.setTitle("Test Title");
        /*collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);*/

        if (uri != null) {


            List<String> parameters = uri.getPathSegments();


            String param = parameters.get(parameters.size()-1);


            //Toast.makeText(this, param, Toast.LENGTH_SHORT).show();
            //messageTV.setText(param);
            restID = param;

        }

        RestaurantDetailsRequestModel restaurantDetailsRequestModel = new RestaurantDetailsRequestModel(
                prefs.getData(Constants.USER_ID),
                restID
        );

        getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "");

        String imgView = getIntent().getStringExtra(Constants.REST_IMG);
        ViewCompat.setTransitionName(iv_rest_img, "iv");
        Glide.with(this).load(imgView).into(iv_rest_img);


        prefs.saveData(Constants.REST_ID, restID);

        allDataRL.setVisibility(View.GONE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            lat = locationGPS.getLatitude();
            longi = locationGPS.getLongitude();
            Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);

        } else {
            Location locationGPS1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (locationGPS1 != null) {
                lat = locationGPS1.getLatitude();
                longi = locationGPS1.getLongitude();
                Log.d("LAT1>>", "lat: " + lat + "lng: " + longi);

            } else {
                if (Constants.addressListModel.address.equals("")) {
                    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                    Log.d("not selected>>", "not selected");
                } else {
                    lat = Double.parseDouble(Constants.addressListModel.lat);
                    longi = Double.parseDouble(Constants.addressListModel.lng);
                    Log.d("selected>>", "selected");
                    Log.d("LAT1>>", "lat: " + Constants.addressListModel.lat + "lng: " + Constants.addressListModel.lng);
                }

                Log.d("LAT>>", "lat: " + lat + "lng: " + longi);
            }

        }
        // if (Constants.isCartEmpty){
        Constants.TAG = 0;
        Constants.cartPrice = 0.0;

        if (Constants.isCartEmpty) {
            viewCartRL.setVisibility(View.GONE);
            Constants.isCartEmpty = false;
        }


        //getMenuItems();
        //getReviews();

        ivGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantDetails.this, RestaurantAddressActivity.class)
                        .putExtra("rest_adr", storeAddress)
                        .putExtra("rest_name", storeName)
                        .putExtra("store_open", storeOpen)
                        .putExtra("store_close", storeClose)
                        .putExtra("store_lat", storeLat)
                        .putExtra("store_lng", storeLng));
            }
        });

        tvPricingFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PricingFeesBottomSheet pricingFeesBottomSheet = new PricingFeesBottomSheet();
                pricingFeesBottomSheet.show(RestaurantDetails.this.getSupportFragmentManager(), "callWalletAdd");
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Connect with " + restaurant_name + " and enjoy their variety of dishes ...\n Use this link to get there: " + "https://www.foodexpressonline.com/restaurant/" + restID ;
                String sub = "https://www.foodexpressonline.com/restaurant/" + Constants.REST_ID;
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav.equals("0")) {
                    AddFavRequestModel addFavRequestModel = new AddFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            restID

                    );
                    addAsFav(addFavRequestModel);

                } else {
                    RemoveFavRequestModel removeFavRequestModel = new RemoveFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            restID

                    );
                    removeAsFav(removeFavRequestModel);
                }

            }
        });

        tvRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsBottomSheet.reviewList = reviewList;
                ReviewsBottomSheet reviewBottomSheet = new ReviewsBottomSheet();
                reviewBottomSheet.show((RestaurantDetails.this).getSupportFragmentManager(), "callRatings");
            }
        });

        tvReviewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewsBottomSheet.reviewList = reviewList;
                ReviewsBottomSheet reviewBottomSheet = new ReviewsBottomSheet();
                reviewBottomSheet.show((RestaurantDetails.this).getSupportFragmentManager(), "callRatings");
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vegLL.setBackground(getResources().getDrawable(R.drawable.left_side_round_off_white_bg));
                tvVeg.setTextColor(getResources().getColor(R.color.black));
                tvAll.setBackground(getResources().getDrawable(R.drawable.black_rounded_corner));
                tvAll.setTextColor(getResources().getColor(R.color.white));
                nonvegLL.setBackground(getResources().getDrawable(R.drawable.right_side_round_off_white_bg));
                tvNonveg.setTextColor(getResources().getColor(R.color.black));

                getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "");

            }
        });

        vegLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvAll.setBackground(getResources().getDrawable(R.drawable.left_side_round_off_white_bg));
                tvAll.setTextColor(getResources().getColor(R.color.black));
                vegLL.setBackground(getResources().getDrawable(R.drawable.black_rounded_corner));
                tvVeg.setTextColor(getResources().getColor(R.color.white));
                nonvegLL.setBackground(getResources().getDrawable(R.drawable.right_side_round_off_white_bg));
                tvNonveg.setTextColor(getResources().getColor(R.color.black));

                getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "1");

            }
        });

        nonvegLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvAll.setBackground(getResources().getDrawable(R.drawable.left_side_round_off_white_bg));
                tvAll.setTextColor(getResources().getColor(R.color.black));
                vegLL.setBackgroundResource(R.color.offWhiteBg);
                tvVeg.setTextColor(getResources().getColor(R.color.black));
                nonvegLL.setBackground(getResources().getDrawable(R.drawable.black_rounded_corner));
                tvNonveg.setTextColor(getResources().getColor(R.color.white));

                getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "0");
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(RestaurantDetails.this, SearchActivity.class));
                SearchItemActivity.restID = restID;
                startActivity(new Intent(RestaurantDetails.this, SearchItemActivity.class));
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantDetails.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                finish();
            }
        });

        viewCartRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.isTakingOrder == false) {
                    Toast.makeText(RestaurantDetails.this, "This restaurant is not taking orders right now!", Toast.LENGTH_SHORT).show();
                } else {
                    CartActivity.isForm = "";
                    startActivity(new Intent(RestaurantDetails.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                    finish();
                }
            }
        });


       /* catFilterRv.addOnItemTouchListener(new RecyclerItemClickListener(RestaurantDetails.this, catFilterRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final float y = catRv.getY() + catRv.getChildAt(position + 1).getY();

                nestedScrollingView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollingView.fling(0);
                        nestedScrollingView.smoothScrollTo(0, (int) y);
                    }
                });
                //catRv.getLayoutManager().scrollToPosition(position);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/

        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                    if (Constants.isCartEmpty){
                        getRestDetails(Constants.REST_ID);
                        Constants.isCartEmpty = false;
                        viewCartRL.setVisibility(View.GONE);
                    }
                handler.postDelayed(this, 10000);
            }
        };
        runnable.run();*/

        tvViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.restaurantModel.is_closed.equals("1")) {

                    Toast.makeText(RestaurantDetails.this, "This restaurant is closed right now!", Toast.LENGTH_SHORT).show();

                } else {
                    if (Constants.isTakingOrder == false) {
                        Toast.makeText(RestaurantDetails.this, "This restaurant is not taking orders right now!", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(RestaurantDetails.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                        finish();
                    }

                }
            }
        });

        Handler handler1 = new Handler();
        Runnable runnable1 = new Runnable() {
            public void run() {
                // Log.d("TAG>>", Constants.TAG + "");

                // if (Constants.TAG > 0) {
                getCartListForData(prefs.getData(Constants.USER_ID));
                   /* if (!Constants.isRevTabSelected) {
                        viewCartRL.setVisibility(View.VISIBLE);
                        tvCartQty.setText(Constants.TAG + " Items");
                        tvCartPrice.setText("\u20B9 " + Constants.cartPrice);
                    } else {
                        viewCartRL.setVisibility(View.GONE);
                    }
                } else {
                    viewCartRL.setVisibility(View.GONE);
                }*/
                handler1.postDelayed(this, 500);
            }
        };
        runnable1.run();

        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 2) {
                    Constants.isRevTabSelected = true;
                    viewPager.setCurrentItem(tab.getPosition());
                    menuLL.setVisibility(View.GONE);
                    revLL.setVisibility(View.VISIBLE);
                    viewCartRL.setVisibility(View.GONE);
                    //Log.d("rev>>", reviewsList.size()+"");

                } else {
                    Constants.isRevTabSelected = false;
                    viewPager.setCurrentItem(tab.getPosition());
                    menuLL.setVisibility(View.VISIBLE);
                    revLL.setVisibility(View.GONE);
                    if (Constants.TAG > 0) {
                        viewCartRL.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        couponsRv.addOnItemTouchListener(new RecyclerItemClickListener(RestaurantDetails.this, couponsRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(RestaurantDetails.this, AllCouponsActivity.class));
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    private void getCartListForData(String data) {

        //dialogView.showCustomSpinProgress(RestaurantDetails.this);
        manager.service.getCartList(data).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    CartItemsResponseModel cirm = response.body();
                    if (cirm.error != true) {
                        newCartList.clear();
                        newCartList = cirm.carts;
                        int qty = 0;
                        //int totQty = 0;
                        if (newCartList.size() > 0) {
                            for (int y = 0; y < newCartList.size(); y++) {
                                if (Integer.parseInt(newCartList.get(y).quantity) > 0) {
                                    qty = qty + Integer.parseInt(newCartList.get(y).quantity);
                                }
                            }

                            Log.d("r1>>", newCartList.get(0).restaurant_id);
                            Log.d("r2>>", restID);
                            if (newCartList.get(0).restaurant_id.equals(restID)) {
                                viewCartRL.setVisibility(View.VISIBLE);
                                tvCartQty.setText(qty + " Items");
                                tvCartPrice.setText("\u20B9 " + cirm.cart_amount);
                            } else {
                                viewCartRL.setVisibility(View.GONE);
                            }

                        } else {
                            viewCartRL.setVisibility(View.GONE);
                            //tvCartQty.setText(Constants.TAG + " Items");
                            //tvCartPrice.setText("\u20B9 " + Constants.cartPrice);
                        }

                        //loadItems();
                        //ciAdapter.notifyDataSetChanged();

                    } else {

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

    private void removeAsFav(RemoveFavRequestModel removeFavRequestModel) {
        dialogView.showCustomSpinProgress(RestaurantDetails.this);
        manager.service.removeFav(removeFavRequestModel).enqueue(new Callback<RemoveFavResponseModel>() {
            @Override
            public void onResponse(Call<RemoveFavResponseModel> call, Response<RemoveFavResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RemoveFavResponseModel rfrm = response.body();
                    if (!rfrm.error) {
                        //ivFav.setImageResource(R.drawable.ic_fav_fill);
                        isFav = "0";
                        ivFav.setImageResource(R.drawable.ic_fav_outline);
                    } else {

                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RemoveFavResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }


    private void addAsFav(AddFavRequestModel addFavRequestModel) {
        dialogView.showCustomSpinProgress(RestaurantDetails.this);
        manager.service.addFav(addFavRequestModel).enqueue(new Callback<AddFavResponseModel>() {
            @Override
            public void onResponse(Call<AddFavResponseModel> call, Response<AddFavResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AddFavResponseModel afrm = response.body();
                    if (!afrm.error) {
                        isFav = "1";
                        ivFav.setImageResource(R.drawable.ic_fav_fill);
                    } else {

                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddFavResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void getRestDetails(RestaurantDetailsRequestModel restID, String lat, String lng, String isVeg) {
        dialogView.showCustomSpinProgress(RestaurantDetails.this);
        Log.d("TOKEN", Constants.token);
        Log.d("start>>", (new Gson().toJson(restID)) + " | " + lat + " | " + lng);
        manager.service.getRestaurantDetails(restID, lat, lng, isVeg).enqueue(new Callback<RestaurantDetailsModel>() {
            @Override
            public void onResponse(Call<RestaurantDetailsModel> call, Response<RestaurantDetailsModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    RestaurantDetailsModel rdm = response.body();

                    if (!rdm.error) {
                        specialItemList.clear();
                        menuTabLay.removeAllTabs();
                        categoryList = new ArrayList<>();
                        reviewList.clear();
                        restWiseCouponList.clear();

                        ResponseRestDetailsModel rrdm = rdm.restaurantData;
                        RestaurantModel restM = rrdm.restaurant;
                        Constants.restaurantModel = rrdm.restaurant;
                        restaurant_name = rrdm.restaurant.name;
                        specialItemList = restM.special_items;
                        categoryList = rrdm.categories;
                        reviewList = rrdm.reviews;
                        restWiseCouponList = rrdm.coupons;
                        Constants.restWiseCoupons = restWiseCouponList;
                        Log.d("REVIEW>>", reviewList.size() + " Units");

                        if (reviewList.size()<=1){
                            tvReviewCount.setText(reviewList.size() + " review");
                        }else{
                            tvReviewCount.setText(reviewList.size() + " reviews");
                        }


                        //Glide.with(RestaurantDetails.this).load(restM.logo).into(ivRestLogo);
                        Glide.with(getApplicationContext()).load(restM.logo).into(ivRestLogo);

                        isFav = restM.is_favorite;

                        if (isFav.equals("1")) {
                            ivFav.setImageResource(R.drawable.ic_fav_fill);
                        } else {
                            ivFav.setImageResource(R.drawable.ic_fav_outline);
                        }

                        if (restM.is_pre_order_only.equals("1")) {
                            double preTime = Double.parseDouble(restM.pre_order_duration) / 60;
                            if (preTime < 24.0) {
                                tvPreTime.setVisibility(View.VISIBLE);
                                tvPreTime.setText("It may take upto " + String.valueOf(preTime) + " hrs");
                                showPreOrderPopup(String.valueOf(preTime), "hr(s)");
                            } else {
                                //if (preTime % 24 == 0) {
                                    int days = (int) (preTime / 24);
                                    tvPreTime.setVisibility(View.VISIBLE);
                                    tvPreTime.setText("It may take upto " + String.valueOf(days) + " days");
                                    showPreOrderPopup(String.valueOf(days), "day(s)");
                              //  }else {
                                   /* double days = preTime / 24;
                                    double hrs = pre
                                    tvPreTime.setVisibility(View.VISIBLE);
                                    tvPreTime.setText("It may take upto " + String.valueOf(days) + " days");
                                    showPreOrderPopup(String.valueOf(days));
                                }*/
                            }


                        } else {
                            tvPreTime.setVisibility(View.GONE);
                        }

                        /*if (restM.is_not_taking_orders.equals("1")){
                            showPopup();
                        }*/

                        if (restM.is_closed.equals("1")) {
                            Constants.isTakingOrder = false;
                            tvOpenStat.setText("Closed now");
                            tvOpenStat.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            if (restM.is_not_taking_orders.equals("0")) {
                                Constants.isTakingOrder = true;
                                tvOpenStat.setText("Open now");
                                tvOpenStat.setTextColor(getResources().getColor(R.color.green3));

                            } else {
                                Constants.isTakingOrder = false;
                                tvOpenStat.setText("Not taking orders");
                                tvOpenStat.setTextColor(getResources().getColor(R.color.colorAccent));
                            }

                        }

                        //TOTAL_PAGES = categoryList.size();
                        allDataRL.setVisibility(View.VISIBLE);

                        tvHeaderTxt.setText(restM.name);
                        tv_rest_name.setText(restM.name);
                        //tv_rest_address.setText(restM.special_items_string);
                        storeName = restM.name;
                        storeAddress = restM.address;
                        storeOpen = restM.start_time;
                        storeClose = restM.close_time;
                        storeLat = restM.lat;
                        storeLng = restM.lng;
                        if (categoryList.size() > 0) {
                            tv_rest_address.setText(categoryList.get(0).title + "....");
                        } else if (categoryList.size() > 1) {
                            tv_rest_address.setText(categoryList.get(0).title + ", " + categoryList.get(1).title + "....");
                        }
                        Log.d("Distance>>", restM.distance);
                        tvDist.setText(restM.distance);
                        tvEstTime.setText(restM.estimated_delivery_time + " mins");
                        DecimalFormat formatter = new DecimalFormat("#,##,###.0");
                        String formatted = "0";
                        if (!restM.rating.equals("0")) {
                            formatted = formatter.format(Double.parseDouble(restM.rating));
                        } else {
                            formatted = "0";
                        }

                        tvRating.setText(formatted);

                        if (restM.is_closed.equals("1")) {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            try {
                                final Date dateObj = sdf.parse(restM.start_time);
                                String time = new SimpleDateFormat("K:mm a").format(dateObj);
                                tvCloseTime.setText("Opens at " + time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            try {
                                final Date dateObj = sdf.parse(restM.close_time);
                                String time = new SimpleDateFormat("K:mm a").format(dateObj);
                                tvCloseTime.setText("Closes at " + time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        Constants.restName = restM.name;
                        Constants.restRating = restM.rating;
                        Constants.revList = reviewList;
                        Constants.restTotalReviews = String.valueOf(reviewList.size());
                        Glide.with(RestaurantDetails.this).load(restM.image).into(iv_rest_img);

                        couponList = restWiseCouponList;

                        getCartList(prefs.getData(Constants.USER_ID));

                        if (specialItemList.size() > 0) {
                            fiAdapter = new FeaturedItemAdapter(RestaurantDetails.this, specialItemList, customCartDataList);
                            featuredItemRv.setLayoutManager(new LinearLayoutManager(
                                    RestaurantDetails.this,
                                    LinearLayoutManager.HORIZONTAL,
                                    false));
                            featuredItemRv.setAdapter(fiAdapter);
                        }

                        if (couponList.size() > 0) {
                            //couponsRv.setVisibility(View.VISIBLE);
                            imgSlider.setVisibility(View.VISIBLE);
                            noCouponLL.setVisibility(View.GONE);
                            sliderAdapter = new SliderAdapter(RestaurantDetails.this, couponList);
                            imgSlider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                            //slider.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                            imgSlider.setSliderAdapter(sliderAdapter);
                            imgSlider.setScrollTimeInSec(3);
                            imgSlider.setAutoCycle(true);
                            imgSlider.startAutoCycle();
                            /*restWiseCouponAdapter = new RestWiseCouponAdapter(RestaurantDetails.this, couponList);
                            couponsRv.setLayoutManager(new LinearLayoutManager(RestaurantDetails.this, LinearLayoutManager.HORIZONTAL, false));
                            couponsRv.setAdapter(restWiseCouponAdapter);*/

                        } else {
                            //couponsRv.setVisibility(View.GONE);
                            imgSlider.setVisibility(View.GONE);
                            noCouponLL.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < categoryList.size(); i++) {
                            itemList = new ArrayList<>();

                            itemList.addAll(categoryList.get(i).items);
                        }

                        for (int x = 0; x < categoryList.size(); x++) {
                            for (int y = 0; y < categoryList.get(x).items.size(); y++) {
                                Constants.TAG = Constants.TAG + Integer.parseInt(categoryList.get(x).items.get(y).quantity);
                                Constants.cartPrice = Constants.cartPrice +
                                        (Integer.parseInt(categoryList.get(x).items.get(y).quantity)
                                                * Double.parseDouble(categoryList.get(x).items.get(y).price));
                            }
                        }

                       /* tabLayout.addTab(tabLayout.newTab().setText("Menu"));
                        tabLayout.addTab(tabLayout.newTab().setText("Delivery"));
                        tabLayout.addTab(tabLayout.newTab().setText("Review"));
                        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                        TabLayoutAdapter adapter = new TabLayoutAdapter(RestaurantDetails.this, getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(adapter);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/

                        orderHistList = rdm.orders;
                        //Collections.reverse(orderHistList);
                        /*for(int i = 0; i < orderHistList.size(); i++){

                        }*/

                        Constants.restaurant_payment_method = restM.payment_mode;
                        Log.d("payment_method>>", Constants.restaurant_payment_method);

                        if (restM.delivery_mode.equals("2")) {
                            Constants.order_delivery_type = "2";
                            CartActivity.is_both = 0;
                        } else if (restM.delivery_mode.equals("1")) {
                            Constants.order_delivery_type = "1";
                            CartActivity.is_both = 0;
                        } else if (restM.delivery_mode.equals("3")) {
                            Constants.order_delivery_type = "1";
                            CartActivity.is_both = 1;
                        } else {
                            Constants.order_delivery_type = "1";
                            CartActivity.is_both = 0;
                        }

                        if (orderHistList.size() > 0) {
                            tvNoPast.setVisibility(View.GONE);
                            past_order_rest_rv.setVisibility(View.VISIBLE);

                            //ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList, orderItemList);
                            ohAdapter = new RestaurantOrderHistAdapter(RestaurantDetails.this, orderHistList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(RestaurantDetails.this, LinearLayoutManager.HORIZONTAL, false);
                            past_order_rest_rv.setLayoutManager(layoutManager);
                            past_order_rest_rv.setAdapter(ohAdapter);

                            loadItems();
                        } else {
                            tvNoPast.setVisibility(View.VISIBLE);
                            past_order_rest_rv.setVisibility(View.GONE);

                            loadItems();
                        }

                        Log.d("stop>>", "stop data loading");
                    } else {
                        if (rdm.message.equals("Token_expired")) {
                            showTokenAlert();
                        }
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();

                }
            }

            @Override
            public void onFailure(Call<RestaurantDetailsModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void loadItems() {
        if (categoryList.size() > 0) {
            Constants.cateList = categoryList;
            for (int x = 0; x < categoryList.size(); x++) {
                if (categoryList.get(x).items.size() > 0) {
                    catViewPager.setAdapter(createCatAdapter());
                    //int finalX = x;
                    new TabLayoutMediator(menuTabLay, catViewPager,
                            new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                    tab.setText(categoryList.get(position).title);
                                }
                            }).attach();
                    //menuTabLay.addTab(menuTabLay.newTab().setText(categoryList.get(x).title));
                }
            }
            catViewPager.setUserInputEnabled(true);
            //menuTabLay.getTabAt().select();
            //menuTabLay.smoothScrollTo(menuTabLay.getSelectedTabPosition(), 0);
            menuTabLay.setTabGravity(TabLayout.GRAVITY_FILL);

            /*ciAdapter = new CategoryItemAdapter(RestaurantDetails.this, categoryList, customCartDataList);
            layoutManager = new LinearLayoutManager(RestaurantDetails.this, LinearLayoutManager.VERTICAL, false);
            catRv.setLayoutManager(layoutManager);
            catRv.setRecycledViewPool(sharedPool);
            catRv.setAdapter(ciAdapter);*/

            /*menuTabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    Log.d("menu_pos>>",String.valueOf(menuTabLay.getSelectedTabPosition()));
                    int pos = menuTabLay.getSelectedTabPosition();
                    // y = (int) (catRv.getY() + catRv.getChildAt(tab.getPosition()).getY());
                    //catRv.getLayoutManager().scrollToPosition(y);
                    //menuTabLay.smoothScrollTo(menuTabLay.getSelectedTabPosition(), 0);
                    *//*catRv.post(() -> {
                        float y =  catRv.getY() + catRv.getChildAt(tab.getPosition()).getY();
                        //nestedScrollingView.smoothScrollTo(0, (int) y);
                        catRv.getLayoutManager().scrollToPosition((int)y);
                    });*//*
                    //int h = catRv.getMeasuredHeight()/2;
                   *//* float y = pos;
                    //nestedScrollingView.smoothScrollTo(0, (int) y);
                    catRv.getLayoutManager().scrollToPosition((int)y);*//*

             *//*TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(menuTabLay, catRv,
                            new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                                }
                            });
                    tabLayoutMediator.attach();*//*

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });*/

//            catRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
//                int scrollDy = 0;
//                @Override
//                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    Log.d("state>>",String.valueOf(newState));
//                }
//
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    scrollDy += dy;
//                    //float y = catRv.getY() + catRv.getChildAt(tab.getPosition()).getY();
//                    Log.d("cat_pos>>",String.valueOf(scrollDy));
//                }
//            });

            /*catRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    int itemPosition= layoutManager.findFirstCompletelyVisibleItemPosition();
                    Log.d("itemPosition>>",String.valueOf(itemPosition));

                    if(itemPosition==0){ //  item position of uses
                        TabLayout.Tab tab = menuTabLay.getTabAt(0);
                        tab.select();
                    }else if(itemPosition==1){//  item position of side effects
                        TabLayout.Tab tab = menuTabLay.getTabAt(1);
                        tab.select();
                    }else if(itemPosition==2){//  item position of how it works
                        TabLayout.Tab tab = menuTabLay.getTabAt(2);
                        tab.select();
                    }else if(itemPosition==3){//  item position of precaution
                        TabLayout.Tab tab = menuTabLay.getTabAt(3);
                        tab.select();
                    }
                }
            });*/


        }
    }

    private ViewPager2Adapter createCatAdapter() {

        ViewPager2Adapter adapter = new ViewPager2Adapter(this, categoryList);
        return adapter;
    }

    private void showPreOrderPopup(String time, String mode) {
        Dialog dialog = new Dialog(RestaurantDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pre_order_popup_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("It may take upto " + time + " " + mode);
        //Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        /*btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");*/
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showPopup() {
        LayoutInflater layoutInflater = LayoutInflater.from(RestaurantDetails.this);
        View promptView = layoutInflater.inflate(R.layout.not_taking_orders_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(RestaurantDetails.this).create();
        TextView tvHeader = (TextView) promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);

        //btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestaurantDetails.this, DashboardHome.class));
                finish();

                //deleteFromCart(id, pos);


            }
        });


        alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);
        alertD.show();
    }

    /*private void loadFirstPage() {

        progressbar.setVisibility(View.GONE);

        ciAdapter = new CategoryItemAdapter(RestaurantDetails.this, categorySubList, customCartDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RestaurantDetails.this, LinearLayoutManager.VERTICAL, false);
        catRv.setLayoutManager(layoutManager);
        catRv.setAdapter(ciAdapter);

        ciAdapter.addAll(categorySubList2);

        if (currentPage <= TOTAL_PAGES) ciAdapter.addLoadingFooter();
        else isLastPage = true;

        catFilterRv.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void loadNextPage() {
        ciAdapter.removeLoadingFooter();
        isLoading = false;
        ciAdapter.addAll(categorySubList2);

        if (currentPage != TOTAL_PAGES) ciAdapter.addLoadingFooter();
        else isLastPage = true;
    }*/

    private void showTokenAlert() {
        Dialog dialog = new Dialog(RestaurantDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
       /* LayoutInflater layoutInflater = LayoutInflater.from(RestaurantDetails.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;
        final AlertDialog alertD = new AlertDialog.Builder(RestaurantDetails.this).create();*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Session Expired! Please Login Again!");
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.isDialogOn = 0;
                startActivity(new Intent(RestaurantDetails.this, EmailLogin.class));
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isDialogOn = 0;
                dialog.dismiss();
            }
        });

       /* alertD.setView(promptView);
        try {*/
        dialog.show();
       /* } catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/

    }

    private void getCartList(String userID) {

        //dialogView.showCustomSpinProgress(RestaurantDetails.this);
        manager.service.getCartList(userID).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    CartItemsResponseModel cirm = response.body();
                    if (cirm.error != true) {
                        cartList.clear();
                        cartList = cirm.carts;
                        //int totQty = 0;
                        if (cartList.size() > 0) {
                            for (int y = 0; y < cartList.size(); y++) {
                                CustomCartModel ccm = new CustomCartModel();
                                ccm.cart_id = cartList.get(y).id;
                                ccm.item_id = cartList.get(y).product_id;
                                customCartDataList.add(ccm);
                            }

                            Constants.custCartList = customCartDataList;

                            Gson gson = new Gson();
                            String xyz = gson.toJson(customCartDataList);
                            prefs.saveData(Constants.CUSTOM_CART_DATA, xyz);
                        }

                        //loadItems();
                        //ciAdapter.notifyDataSetChanged();

                    } else {

                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<CartItemsResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* startActivity(new Intent(RestaurantDetails.this, DashboardHome.class));
        finish();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}