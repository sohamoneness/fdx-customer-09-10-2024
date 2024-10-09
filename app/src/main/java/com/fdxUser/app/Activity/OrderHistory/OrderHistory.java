package com.fdxUser.app.Activity.OrderHistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.Profile.Profile;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Adapters.OrderHistAdapter;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDateFilterRequestModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderItemModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistory extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView ordHistRv;
    List<OrderHistoryListModel> orderHistList = new ArrayList<>();
    List<OrderHistoryListModel> revOrderHistList = new ArrayList<>();
    List<OrderItemModel> orderItemList = new ArrayList<>();
    OrderHistAdapter ohAdapter;
    RelativeLayout relNoOrder;
    Button btn_try_now;

    Prefs prefs;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    ApiManager manager1 = new ApiManager();
    String userID = "";
    RelativeLayout homeRl, cartRl, subcriptionRl, profileRl;
    ImageView ivCrown, ivCart;
    TextView tvCrown, tvCart;
    LinearLayout li_coming_soon;

    EditText etStartDate, etEndDate;
    RelativeLayout tv_search;
    private String start = "";
    private String end = "";
    private String today = "";
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(OrderHistory.this);
        dialogView = new DialogView();

        userID = prefs.getData(Constants.USER_ID);
        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);

        ivBack = findViewById(R.id.iv_back);
        ordHistRv = findViewById(R.id.ord_hist_rv);
        relNoOrder = findViewById(R.id.relNoOrder);
        btn_try_now = findViewById(R.id.btn_try_now);
        homeRl = findViewById(R.id.homeRl);
        cartRl = findViewById(R.id.cartRl);
        subcriptionRl = findViewById(R.id.subcriptionRl);
        profileRl = findViewById(R.id.profileRl);
        ivCrown = findViewById(R.id.ivCrown);
        tvCrown = findViewById(R.id.tvCrown);
        ivCart = findViewById(R.id.ivCart);
        tvCart = findViewById(R.id.tvCart);
        li_coming_soon = findViewById(R.id.li_coming_soon);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        tv_search = findViewById(R.id.tv_search);

        getOrderHistList(userID);

//        ordHistRv.addOnItemTouchListener(new RecyclerItemClickListener(OrderHistory.this, ordHistRv, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                OrderSummeryActivity.ordId = orderHistList.get(position).id;
//                startActivity(new Intent(OrderHistory.this, OrderSummeryActivity.class));
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        }));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_try_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderHistory.this, DashboardHome.class));
            }
        });

        homeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this, DashboardHome.class));
                finish();
            }
        });

        cartRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCrown.setTextColor(getResources().getColor(R.color.black));
                //ivCrown.setColorFilter(Color.argb(1, 255, 255, 255));
                ivCrown.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.black), PorterDuff.Mode.SRC_IN);
                //ivCrown.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                // homeLL.setBackgroundResource(R.color.white);
                tvCart.setTextColor(getResources().getColor(R.color.colorAccent));
                //ivHome.setColorFilter(Color.argb(1, 0, 0, 0));
                ivCart.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            }
        });

        subcriptionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvCrown.setTextColor(getResources().getColor(R.color.colorAccent));
                ivCrown.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

                tvCart.setTextColor(getResources().getColor(R.color.black));
                ivCart.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.black), PorterDuff.Mode.SRC_IN);

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
                                tvCart.setTextColor(getResources().getColor(R.color.colorAccent));
                                //ivHome.setColorFilter(Color.argb(1, 255, 255, 255));
                                ivCart.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                                //subcriptionLL.setBackgroundResource(R.color.white);
                                tvCrown.setTextColor(getResources().getColor(R.color.black));
                                //ivCrown.setColorFilter(Color.argb(1, 0, 0, 0));
                                ivCrown.setColorFilter(ContextCompat.getColor(OrderHistory.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                        });
                    }
                };
                thread.start(); //start the thread

            }
        });

        profileRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this, Profile.class));
            }
        });

        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);

        //String d = String.valueOf(myCalendar.getTime());
        //Log.d("d>>",d);

        end = dateFormat.format(myCalendar.getTime());
        today = dateFormat.format(myCalendar.getTime());

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        //String d1 = String.valueOf(c.getTime());
        //Log.d("d1>>",d1);

        start = dateFormat.format(c.getTime());

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel1();
            }
        };
        DatePickerDialog.OnDateSetListener date1 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel2();
            }
        };
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrderHistory.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrderHistory.this,date1,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDateFilterRequestModel orderDateFilterRequestModel = new OrderDateFilterRequestModel(
                        userID,
                        start,
                        end
                );

                orderDateFilter(orderDateFilterRequestModel);
            }
        });
        //etStartDate.setText(start);
        //etEndDate.setText(end);
    }

    private void updateLabel1(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        etStartDate.setText(dateFormat.format(myCalendar.getTime()));
        start = dateFormat.format(myCalendar.getTime());
    }

    private void updateLabel2(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        etEndDate.setText(dateFormat.format(myCalendar.getTime()));
        end = dateFormat.format(myCalendar.getTime());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Constants.isFromHomeToHistory){
            Constants.isFromHomeToHistory = false;
            startActivity(new Intent(OrderHistory.this, DashboardHome.class));
            finish();
        }else {
            startActivity(new Intent(OrderHistory.this, Profile.class));
            finish();
        }

    }

    private void getOrderHistList(String userID) {
        dialogView.showCustomSpinProgress(OrderHistory.this);
        manager.service.getOrderList(userID).enqueue(new Callback<OrderHistResponseModel>() {
            @Override
            public void onResponse(Call<OrderHistResponseModel> call, Response<OrderHistResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderHistResponseModel orderHistResponseModel = response.body();
                    if (orderHistResponseModel.error != true){

                        orderHistList.clear();
                        orderHistList = orderHistResponseModel.orders;
                        Collections.reverse(orderHistList);
                        /*for(int i = 0; i < orderHistList.size(); i++){

                        }*/

                        if (orderHistList.size() > 0){
                            ordHistRv.setVisibility(View.VISIBLE);
                            relNoOrder.setVisibility(View.GONE);
                            //ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList, orderItemList);
                            ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false);
                            ordHistRv.setLayoutManager(layoutManager);
                            ordHistRv.setAdapter(ohAdapter);
                        }else{
                            ordHistRv.setVisibility(View.GONE);
                            relNoOrder.setVisibility(View.VISIBLE);
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

    private void orderDateFilter(OrderDateFilterRequestModel orderDateFilterRequestModel) {
        dialogView.showCustomSpinProgress(OrderHistory.this);
        manager.service.orderDateFilter(orderDateFilterRequestModel).enqueue(new Callback<OrderHistResponseModel>() {
            @Override
            public void onResponse(Call<OrderHistResponseModel> call, Response<OrderHistResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderHistResponseModel orderHistResponseModel = response.body();
                    if (orderHistResponseModel.error != true){

                        orderHistList.clear();
                        orderHistList = orderHistResponseModel.orders;
                        Collections.reverse(orderHistList);
                        /*for(int i = 0; i < orderHistList.size(); i++){

                        }*/

                        if (orderHistList.size() > 0){
                            ordHistRv.setVisibility(View.VISIBLE);
                            relNoOrder.setVisibility(View.GONE);
                            //ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList, orderItemList);
                            ohAdapter = new OrderHistAdapter(OrderHistory.this, orderHistList);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false);
                            ordHistRv.setLayoutManager(layoutManager);
                            ordHistRv.setAdapter(ohAdapter);
                        }else{
                            ordHistRv.setVisibility(View.GONE);
                            relNoOrder.setVisibility(View.VISIBLE);
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

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}