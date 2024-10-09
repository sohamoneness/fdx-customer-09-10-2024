package com.fdxUser.app.Activity.RestaurantScreens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Adapters.CategoryItemAdapter;
import com.fdxUser.app.Adapters.FeaturedItemAdapter;
import com.fdxUser.app.Adapters.FilterCatAdapter;
import com.fdxUser.app.Adapters.MenuItemAdapter;
import com.fdxUser.app.Adapters.RestWiseCouponAdapter;
import com.fdxUser.app.Adapters.ReviewRestAdapter;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.Models.DemoModels.MenuItemModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavResponseModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavResponseModel;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRestaurantDetails extends AppCompatActivity implements Animation.AnimationListener {

    TabLayout tabLayout, menuTabLay;
    ViewPager viewPager, menuViewPager;
    LinearLayout menuLL, revLL;
    RecyclerView catRv, revRv;

    MenuItemAdapter miAdapter;
    ReviewRestAdapter riAdapter;
    RelativeLayout viewCartRL;

    List<CartsModel> cartList = new ArrayList<>();
    List<CustomCartModel> customCartDataList = new ArrayList<>();

    //ReviewRestAdapter reviewRestAdapter;
    TextView tvAll, tvVeg, tvNonveg;

    TextView tvCartPrice, tvCartQty, tvViewCart;
    TextView tv_rest_name, tv_rest_address, tvRating, tvEstTime, tvDist;

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
    TextView tvNoData, et_search;
    Toolbar mToolbar;
    AppBarLayout mAppBarLayout;

    LocationManager locationManager;
    double lat = 0.0;
    double longi = 0.0;

    CategoryItemAdapter ciAdapter;
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
    NestedScrollView nestedScrollingView;
    ProgressBar progressbar;
    ImageView ivShare, ivFav;

    LinearLayout vegLL, nonvegLL;
    RelativeLayout allDataRL;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    String isFav = "";
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        uri = getIntent().getData();

        // checking if the uri is null or not.
        if (uri != null) {

            // if the uri is not null then we are getting
            // the path segments and storing it in list.
            List<String> parameters = uri.getPathSegments();

            // after that we are extracting string
            // from that parameters.
            String param = parameters.get(parameters.size() - 1);

            Toast.makeText(this,param,Toast.LENGTH_SHORT).show();
            Log.d("deepl>>",param);
        }else{
            Log.d("deepl>>","no deep link");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant_details);
        getWindow().setExitTransition(new Explode());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeStatusBarColor();


        dialogView = new DialogView();

        prefs = new Prefs(NewRestaurantDetails.this);
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
        nonvegLL = findViewById(R.id.nonvegLL);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        tv_rest_name = findViewById(R.id.tv_rest_name);
        tv_rest_address = findViewById(R.id.tv_rest_address);
        et_search = findViewById(R.id.et_search);
        tvRating = findViewById(R.id.tvRating);
        tvEstTime = findViewById(R.id.tvEstTime);
        tvDist = findViewById(R.id.tvDist);
        menuTabLay = findViewById(R.id.menu_tab_layout);
        menuLL = findViewById(R.id.menu_ll);
        revLL = findViewById(R.id.rev_ll);
        revRv = findViewById(R.id.rev_rv);
        //menuRv = findViewById(R.id.menu_rv);
        couponsRv = findViewById(R.id.couponsRv);
        catFilterRv = findViewById(R.id.catFilterRv);
        //menuRv = findViewById(R.id.menu_rv);
        catRv = findViewById(R.id.catRv);
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
        nestedScrollingView = findViewById(R.id.nestedScrollingView);
        //menuViewPager = findViewById(R.id.menu_view_pager);

        //collapsing_toolbar.setTitle("Test Title");
        /*collapsing_toolbar.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        collapsing_toolbar.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);*/

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
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
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

        RestaurantDetailsRequestModel restaurantDetailsRequestModel = new RestaurantDetailsRequestModel(
                prefs.getData(Constants.USER_ID),
                restID
        );

        getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "");

        //getMenuItems();
        //getReviews();

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Download the app and use my Referral code:FDX-TEST-1234 to register and get 50% off on your first order";
                String sub = "Refer and earn discounts and many more...";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                myIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav.equals("0")){
                    AddFavRequestModel addFavRequestModel = new AddFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            restID

                    );
                    addAsFav(addFavRequestModel);

                }else {
                    RemoveFavRequestModel removeFavRequestModel = new RemoveFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            restID

                    );
                    removeAsFav(removeFavRequestModel);
                }

            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vegLL.setBackground(getResources().getDrawable(R.drawable.left_side_round_off_white_bg));
                tvVeg.setTextColor(getResources().getColor(R.color.black));
                tvAll.setBackground(getResources().getDrawable(R.drawable.all_item_border_bg));
                tvAll.setTextColor(getResources().getColor(R.color.colorAccent));
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
                vegLL.setBackground(getResources().getDrawable(R.drawable.veg_item_border_bg));
                tvVeg.setTextColor(getResources().getColor(R.color.green2));
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
                nonvegLL.setBackground(getResources().getDrawable(R.drawable.nonveg_item_border_bg));
                tvNonveg.setTextColor(getResources().getColor(R.color.red));

                getRestDetails(restaurantDetailsRequestModel, String.valueOf(lat), String.valueOf(longi), "0");
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewRestaurantDetails.this, SearchActivity.class));
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
                startActivity(new Intent(NewRestaurantDetails.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                finish();
            }
        });


       /* catFilterRv.addOnItemTouchListener(new RecyclerItemClickListener(NewRestaurantDetails.this, catFilterRv, new RecyclerItemClickListener.OnItemClickListener() {
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

                startActivity(new Intent(NewRestaurantDetails.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                finish();
            }
        });

        Handler handler1 = new Handler();
        Runnable runnable1 = new Runnable() {
            public void run() {
                Log.d("TAG>>", Constants.TAG + "");

                if (Constants.TAG > 0) {
                    if (!Constants.isRevTabSelected) {
                        viewCartRL.setVisibility(View.VISIBLE);
                        tvCartQty.setText(Constants.TAG + " Items");
                        tvCartPrice.setText("\u20B9 " + Constants.cartPrice);
                    } else {
                        viewCartRL.setVisibility(View.GONE);
                    }
                } else {
                    viewCartRL.setVisibility(View.GONE);
                }
                handler1.postDelayed(this, 100);
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


    }

    private void removeAsFav(RemoveFavRequestModel removeFavRequestModel) {
        dialogView.showCustomSpinProgress(NewRestaurantDetails.this);
        manager.service.removeFav(removeFavRequestModel).enqueue(new Callback<RemoveFavResponseModel>() {
            @Override
            public void onResponse(Call<RemoveFavResponseModel> call, Response<RemoveFavResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RemoveFavResponseModel rfrm = response.body();
                    if (!rfrm.error){
                        //ivFav.setImageResource(R.drawable.ic_fav_fill);
                        ivFav.setImageResource(R.drawable.ic_fav_outline);
                    }else {

                    }
                }else {
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
        dialogView.showCustomSpinProgress(NewRestaurantDetails.this);
        manager.service.addFav(addFavRequestModel).enqueue(new Callback<AddFavResponseModel>() {
            @Override
            public void onResponse(Call<AddFavResponseModel> call, Response<AddFavResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AddFavResponseModel afrm = response.body();
                    if (!afrm.error){
                        ivFav.setImageResource(R.drawable.ic_fav_fill);
                    }else {

                    }
                }else {
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
        dialogView.showCustomSpinProgress(NewRestaurantDetails.this);
        manager.service.getRestaurantDetails(restID, lat, lng, isVeg).enqueue(new Callback<RestaurantDetailsModel>() {
            @Override
            public void onResponse(Call<RestaurantDetailsModel> call, Response<RestaurantDetailsModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    RestaurantDetailsModel rdm = response.body();

                    if (!rdm.error) {
                        specialItemList.clear();
                        categoryList.clear();
                        reviewList.clear();
                        restWiseCouponList.clear();


                        ResponseRestDetailsModel rrdm = rdm.restaurantData;
                        RestaurantModel restM = rrdm.restaurant;
                        Constants.restaurantModel = rrdm.restaurant;
                        specialItemList = restM.special_items;
                        categoryList = rrdm.categories;
                        reviewList = rrdm.reviews;
                        restWiseCouponList = rrdm.coupons;
                        Constants.restWiseCoupons = restWiseCouponList;
                        Log.d("REVIEW>>", reviewList.size() + " Units");

                        isFav = restM.is_favorite;

                        if (isFav.equals("1")){
                            ivFav.setImageResource(R.drawable.ic_fav_fill);
                        }else {
                            ivFav.setImageResource(R.drawable.ic_fav_outline);
                        }

                        //TOTAL_PAGES = categoryList.size();
                        allDataRL.setVisibility(View.VISIBLE);

                        collapsing_toolbar.setTitle(restM.name);

                        tvHeaderTxt.setText(restM.name);
                        tv_rest_name.setText(restM.name);
                        tv_rest_address.setText(restM.address);
                        tvDist.setText(restM.distance);
                        tvEstTime.setText(restM.estimated_delivery_time);
                        tvRating.setText(restM.rating);
                        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                        try {
                            final Date dateObj = sdf.parse(restM.close_time);
                            String time = new SimpleDateFormat("K:mm").format(dateObj);
                            tvCloseTime.setText("Closes at " + time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Constants.restName = restM.name;
                        Constants.restRating = restM.rating;
                        Constants.revList = reviewList;
                        Constants.restTotalReviews = String.valueOf(reviewList.size());
                        Glide.with(NewRestaurantDetails.this).load(restM.image).into(iv_rest_img);

                        couponList = restWiseCouponList;

                        getCartList(prefs.getData(Constants.USER_ID));

                        if (specialItemList.size() > 0) {
                            /*fiAdapter = new FeaturedItemAdapter(NewRestaurantDetails.this, specialItemList, customCartDataList);
                            featuredItemRv.setLayoutManager(new LinearLayoutManager(
                                    NewRestaurantDetails.this,
                                    LinearLayoutManager.HORIZONTAL,
                                    false));
                            featuredItemRv.setAdapter(fiAdapter);*/
                        }

                        if (couponList.size() > 0) {
                            couponsRv.setVisibility(View.VISIBLE);
                            noCouponLL.setVisibility(View.GONE);
                            restWiseCouponAdapter = new RestWiseCouponAdapter(NewRestaurantDetails.this, couponList);
                            couponsRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.HORIZONTAL, false));
                            couponsRv.setAdapter(restWiseCouponAdapter);

                        } else {
                            couponsRv.setVisibility(View.GONE);
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

                        if (categoryList.size()>0){
                            for(int x = 0; x < categoryList.size(); x++){
                                menuTabLay.addTab(menuTabLay.newTab().setText(categoryList.get(x).title));
                            }

                            menuTabLay.setTabGravity(TabLayout.GRAVITY_FILL);

                            ciAdapter = new CategoryItemAdapter(NewRestaurantDetails.this, categoryList, customCartDataList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false);
                            catRv.setLayoutManager(layoutManager);
                            catRv.setAdapter(ciAdapter);
                           /* if (categoryList.get(0).items.size()>0){
                                catRv.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                                miAdapter = new MenuItemAdapter(categoryList.get(0).items, customCartDataList, NewRestaurantDetails.this);
                                catRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                                catRv.setAdapter(miAdapter);
                            }else{
                                catRv.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }*/


                            menuTabLay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    //nestedScrollingView.
                                    catRv.smoothScrollToPosition(tab.getPosition());

                                    //viewPager.setCurrentItem(tab.getPosition());
                                    /*for (int y = 0; y < categoryList.size(); y++) {
                                        if(menuTabLay.getSelectedTabPosition() == y) {
                                            if (categoryList.get(y).items.size() > 0) {
                                                catRv.setVisibility(View.VISIBLE);
                                                tvNoData.setVisibility(View.GONE);
                                                miAdapter = new MenuItemAdapter(categoryList.get(y).items, customCartDataList, NewRestaurantDetails.this);
                                                catRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                                                catRv.setAdapter(miAdapter);
                                                //miAdapter.notifyDataSetChanged();
                                            } else {
                                                catRv.setVisibility(View.GONE);
                                                tvNoData.setVisibility(View.VISIBLE);
                                            }
                                        }

                                    }*/
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {

                                    /*if (categoryList.get(0).items.size() > 0){
                                        catRv.setVisibility(View.VISIBLE);
                                        tvNoData.setVisibility(View.GONE);
                                        miAdapter = new MenuItemAdapter(categoryList.get(0).items, customCartDataList, NewRestaurantDetails.this);
                                        catRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                                        catRv.setAdapter(miAdapter);
                                    }else{
                                        catRv.setVisibility(View.GONE);
                                        tvNoData.setVisibility(View.VISIBLE);
                                    }*/


                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });


                           /* for (int x = 0; x < categoryList.size(); x++) {
                                menuTabLay.addTab(menuTabLay.newTab().setText(categoryList.get(x).title));

                            }*/
                            /*filterCatAdapter = new FilterCatAdapter(NewRestaurantDetails.this, categoryList);
                            catFilterRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.HORIZONTAL, false));
                            catFilterRv.setAdapter(filterCatAdapter);

                            for (int i = 0; i < categoryList.size(); i++){
                                if(i < 4){
                                    categorySubList.add(categoryList.get(i));
                                }else{
                                    categorySubList2.add(categoryList.get(i));
                                }

                            }*/
                            //Log.d("SIZE>>", ""+categoryList.size()/2);
                            /*for (int j = categorySubList.size(); j < categoryList.size(); j++){
                                categorySubList2.add(categoryList.get(j));
                            }*/
                            //loadFirstPage();


                            //menuTabLay.setTabGravity(TabLayout.GRAVITY_FILL);

                           /* if (categoryList.get(0).items.size() > 0) {

                                ciAdapter = new CategoryItemAdapter(NewRestaurantDetails.this, categoryList, customCartDataList);
                                //LinearLayoutManager layoutManager = ;
                                catRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                                catRv.setAdapter(ciAdapter);
                                menuRv.setVisibility(View.VISIBLE);
                                tvNoData.setVisibility(View.GONE);
                                *//*miAdapter = new MenuItemAdapter(categoryList.get(0).items, customCartDataList, NewRestaurantDetails.this);
                                menuRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                                menuRv.setAdapter(miAdapter);*//*
                            } else {
                                //catRv.setVisibility(View.GONE);
                                menuRv.setVisibility(View.GONE);
                                tvNoData.setVisibility(View.VISIBLE);
                            }*/

                            //Toast.makeText(NewRestaurantDetails.this, "CART SIZE: " + cartList.size(), Toast.LENGTH_SHORT).show();

                        /*if (reviewList.size() > 0) {
                            riAdapter = new ReviewRestAdapter(NewRestaurantDetails.this, reviewList);
                            revRv.setLayoutManager(new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false));
                            revRv.setAdapter(riAdapter);
                        } else {
                            //Toast.makeText(NewRestaurantDetails.this, "List empty!", Toast.LENGTH_SHORT).show();
                        }*/



                        }



                       /* tabLayout.addTab(tabLayout.newTab().setText("Menu"));
                        tabLayout.addTab(tabLayout.newTab().setText("Delivery"));
                        tabLayout.addTab(tabLayout.newTab().setText("Review"));
                        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

                        TabLayoutAdapter adapter = new TabLayoutAdapter(NewRestaurantDetails.this, getSupportFragmentManager(), tabLayout.getTabCount());
                        viewPager.setAdapter(adapter);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/

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

    /*private void loadFirstPage() {

        progressbar.setVisibility(View.GONE);

        ciAdapter = new CategoryItemAdapter(NewRestaurantDetails.this, categorySubList, customCartDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(NewRestaurantDetails.this, LinearLayoutManager.VERTICAL, false);
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
        LayoutInflater layoutInflater = LayoutInflater.from(NewRestaurantDetails.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;
        final AlertDialog alertD = new AlertDialog.Builder(NewRestaurantDetails.this).create();
        TextView tvHeader = (TextView) promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg = (TextView) promptView.findViewById(R.id.tvMsg);
        tvMsg.setText("Session Expired! Please Login Again!");
        Button btnCancel = (Button) promptView.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        btnOk.setBackgroundColor(NewRestaurantDetails.this.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.isDialogOn = 0;
                startActivity(new Intent(NewRestaurantDetails.this, EmailLogin.class));
                finish();
                alertD.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isDialogOn = 0;
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

    private void getCartList(String userID) {
        dialogView.showCustomSpinProgress(NewRestaurantDetails.this);
        manager.service.getCartList(userID).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
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
                            Gson gson = new Gson();
                            String xyz = gson.toJson(customCartDataList);
                            prefs.saveData(Constants.CUSTOM_CART_DATA, xyz);
                        }

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
}