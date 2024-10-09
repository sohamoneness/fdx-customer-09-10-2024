package com.fdxUser.app.Activity.OtherScreens;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Activity.RestaurantScreens.CheckOutActivity;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Models.PlaceOrderModels.PlaceOrderRequest;
import com.fdxUser.app.Models.PlaceOrderModels.PlaceOrderResponseModel;
import com.fdxUser.app.Models.PlaceOrderModels.PlacedOrderDetailsModel;
import com.fdxUser.app.Models.WalletModels.WalletListResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    
    RelativeLayout payCodRl, payOnlineRl, payWalletRl;
    TextView tv_extra_payment;
    Prefs prefs;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;
    String totAmount = "", totItemPrice = "", discount = "", discountCode = "";
    String restID = "";
    ImageView ivBack;
    TextView tv_wallet_balance,last_order_outstanding;
    TextView tvOnline, tvWallet, tvCod;
    Button btnConfirm;
    String transactionId = "";
    String wallet_balance = "0";
    String payment_method = "1";
    String paid_online = "0";
    String paid_by_wallet = "0";
    int order_completed = 0;
    String cancel_order_charge = "0";

    public String schedule_date = "";
    public String schedule_time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        dialogView = new DialogView();
        prefs = new Prefs(PaymentActivity.this);

        getUserWallets(prefs.getData(Constants.USER_ID));

        restID = getIntent().getStringExtra(Constants.REST_ID);


        totAmount = getIntent().getStringExtra(Constants.TOTAL_PRICE);
        totItemPrice = getIntent().getStringExtra(Constants.TOTAL_ITEM_PRICE);
        discount = getIntent().getStringExtra(Constants.DISCOUNT);
        discountCode = getIntent().getStringExtra(Constants.DISCOUNT_CODE);

        payCodRl = findViewById(R.id.payCodRl);
        payOnlineRl = findViewById(R.id.payOnlineRl);
        payWalletRl = findViewById(R.id.payWalletRl);
        ivBack = findViewById(R.id.ivBack);
        btnConfirm = findViewById(R.id.btnConfirm);

        tv_wallet_balance = findViewById(R.id.tv_wallet_balance);

        tvOnline = findViewById(R.id.tvOnline);
        tvWallet = findViewById(R.id.tvWallet);
        tvCod = findViewById(R.id.tvCod);
        tv_extra_payment = findViewById(R.id.tv_extra_payment);
        last_order_outstanding = findViewById(R.id.last_order_outstanding);

        cancel_order_charge = prefs.getData(Constants.USER_CANCEL_AMOUNT);

        schedule_date = Constants.schedule_date;
        schedule_time = Constants.schedule_time;

        //CToast.show(PaymentActivity.this,schedule_date);
        //CToast.show(PaymentActivity.this,schedule_time);

        Log.d("schedule_date>>",schedule_date);
        Log.d("schedule_time>>",schedule_time);

        if (!cancel_order_charge.equals("0")){
            last_order_outstanding.setVisibility(View.VISIBLE);
            last_order_outstanding.setText("You have a outstanding amount of ₹ "+cancel_order_charge+" from last order");
        }else {
            last_order_outstanding.setVisibility(View.GONE);
            cancel_order_charge = "0";
        }

        totAmount = String.valueOf(Double.parseDouble(totAmount)+Double.parseDouble(cancel_order_charge));
        Log.d("total>>",totAmount);

        if (Constants.restaurant_payment_method.equals("1")){
            payOnlineRl.setVisibility(View.VISIBLE);
            payCodRl.setVisibility(View.GONE);
        }else if (Constants.restaurant_payment_method.equals("2")){
            payOnlineRl.setVisibility(View.GONE);
            payCodRl.setVisibility(View.VISIBLE);
        }else if (Constants.restaurant_payment_method.equals("3")){
            payOnlineRl.setVisibility(View.VISIBLE);
            payCodRl.setVisibility(View.VISIBLE);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_method.equals("1")){
                    Toast.makeText(PaymentActivity.this,"Onine",Toast.LENGTH_SHORT).show();

                    paid_online = totAmount;
                    paid_by_wallet = "0";

                    String samount = totAmount;

                    // rounding off the amount.
                    int amount = Math.round(Float.parseFloat(samount) * 100);

                    // initialize Razorpay account.
                    Checkout checkout = new Checkout();

                    // set your id as below
                    checkout.setKeyID(getResources().getString(R.string.razorpay_key));

                    // set image
                    checkout.setImage(R.drawable.fdxlogo);

                    // initialize json object
                    JSONObject object = new JSONObject();
                    try {
                        // to put name
                        object.put("name", "FOOD EXPRESS ONLINE");

                        // put description
                        object.put("description", "Test payment");

                        // to set theme color
                        object.put("theme.color", "");

                        // put the currency
                        object.put("currency", "INR");

                        // put amount
                        object.put("amount", amount);

                        // put mobile number
                        object.put("prefill.contact", prefs.getData(Constants.USER_MOBILE));

                        // put email
                        object.put("prefill.email", prefs.getData(Constants.USER_EMAIL));

                        // open razorpay to checkout activity
                        checkout.open(PaymentActivity.this, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (payment_method.equals("2")){
                    Toast.makeText(PaymentActivity.this,"COD",Toast.LENGTH_SHORT).show();
                    if (restID != null){
                        Log.d("RESTID3", restID);
                    }else{
                        Log.d("RESTID3", "1234");
                    }

                    PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(

                            restID,
                            prefs.getData(Constants.USER_ID),
                            prefs.getData(Constants.USER_NAME),
                            prefs.getData(Constants.USER_MOBILE),
                            prefs.getData(Constants.USER_EMAIL),
                            Constants.addressListModel.address,
                            Constants.addressListModel.location,
                            Constants.addressListModel.country,
                            Constants.addressListModel.city,
                            Constants.addressListModel.pin,
                            Constants.addressListModel.lat,
                            Constants.addressListModel.lng,
                            totItemPrice,
                            discountCode,
                            discount,
                            "test-cod",
                            Constants.tip,
                            Constants.notes,
                            "0",
                            "0",
                            Constants.order_delivery_type,
                            cancel_order_charge,
                            Constants.offerId,
                            schedule_date,
                            schedule_time
                    );

                    Gson gson = new Gson();
                    String param = gson.toJson(placeOrderRequest);
                    Log.d("PARAMS_PlaceOrder", param);
                    placeOrder(placeOrderRequest);
                }else if (payment_method.equals("3")){
                    Toast.makeText(PaymentActivity.this,"Wallet",Toast.LENGTH_SHORT).show();

                    double d1 = Double.parseDouble(totAmount);
                    double d2 = Double.parseDouble(wallet_balance);

                    if (d1<=d2){

                        paid_online = "0";
                        paid_by_wallet = String.valueOf(d1);
                        Log.d("wallet>>","Success");

                        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(

                                restID,
                                prefs.getData(Constants.USER_ID),
                                prefs.getData(Constants.USER_NAME),
                                prefs.getData(Constants.USER_MOBILE),
                                prefs.getData(Constants.USER_EMAIL),
                                Constants.addressListModel.address,
                                Constants.addressListModel.location,
                                Constants.addressListModel.country,
                                Constants.addressListModel.city,
                                Constants.addressListModel.pin,
                                Constants.addressListModel.lat,
                                Constants.addressListModel.lng,
                                totItemPrice,
                                discountCode,
                                discount,
                                "test-wallet",
                                Constants.tip,
                                Constants.notes,
                                paid_online,
                                paid_by_wallet,
                                Constants.order_delivery_type,
                                cancel_order_charge,
                                Constants.offerId,
                                schedule_date,
                                schedule_time
                        );

                        Gson gson = new Gson();
                        String param = gson.toJson(placeOrderRequest);
                        Log.d("PARAMS_PlaceOrder", param);
                        placeOrder(placeOrderRequest);
                        //Toast.makeText(PaymentActivity.this,"Success",Toast.LENGTH_SHORT);
                    }else if (d1>d2 && d2>0){
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        double d3 = d1 - d2;

                        paid_online = formatter1.format(d3);
                        paid_by_wallet = formatter1.format(d2);

                        String samount = String.valueOf(d3);

                        // rounding off the amount.
                        int amount = Math.round(Float.parseFloat(samount) * 100);

                        // initialize Razorpay account.
                        Checkout checkout = new Checkout();

                        // set your id as below
                        checkout.setKeyID(getResources().getString(R.string.razorpay_key));

                        // set image
                        checkout.setImage(R.drawable.fdxlogo);

                        // initialize json object
                        JSONObject object = new JSONObject();
                        try {
                            // to put name
                            object.put("name", "FOOD EXPRESS ONLINE");

                            // put description
                            object.put("description", "Test payment");

                            // to set theme color
                            object.put("theme.color", "");

                            // put the currency
                            object.put("currency", "INR");

                            // put amount
                            object.put("amount", amount);

                            // put mobile number
                            object.put("prefill.contact", prefs.getData(Constants.USER_MOBILE));

                            // put email
                            object.put("prefill.email", prefs.getData(Constants.USER_EMAIL));

                            // open razorpay to checkout activity
                            checkout.open(PaymentActivity.this, object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("wallet>>","Failed");
                        dialogView.errorButtonDialog(PaymentActivity.this, getResources().getString(R.string.app_name), "You do not have sufficient balance in your wallet");
                    }
                }
            }
        });

        payWalletRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("item price>>",totAmount);
                payment_method = "3";
                payOnlineRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                payWalletRl.setBackground(getResources().getDrawable(R.drawable.payment_selected));
                payCodRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                tvWallet.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOnline.setTextColor(getResources().getColor(R.color.black));
                tvCod.setTextColor(getResources().getColor(R.color.black));

                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                double d1 = Double.parseDouble(totAmount);
                double d2 = Double.parseDouble(wallet_balance);

                if (d1<=d2){
                    tv_extra_payment.setVisibility(View.GONE);
                }else if (d1>d2 && d2>0) {
                    double d3 = d1 - d2;
                    tv_extra_payment.setVisibility(View.VISIBLE);
                    tv_extra_payment.setText("You need to pay ₹ "+formatter1.format(d3)+" via online payment");
                }else {
                    tv_extra_payment.setVisibility(View.GONE);
                }
//                double d1 = Double.parseDouble(totAmount);
//                double d2 = Double.parseDouble(wallet_balance);
//
//                if (d1<=d2){
//                    Log.d("wallet>>","Success");
//
//                    PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(
//
//                            restID,
//                            prefs.getData(Constants.USER_ID),
//                            prefs.getData(Constants.USER_NAME),
//                            prefs.getData(Constants.USER_MOBILE),
//                            prefs.getData(Constants.USER_EMAIL),
//                            Constants.addressListModel.address,
//                            Constants.addressListModel.location,
//                            Constants.addressListModel.country,
//                            Constants.addressListModel.city,
//                            Constants.addressListModel.pin,
//                            Constants.addressListModel.lat,
//                            Constants.addressListModel.lng,
//                            totItemPrice,
//                            discountCode,
//                            discount,
//                            "test-wallet",
//                            "0",
//                            Constants.notes
//                    );
//
//                    Gson gson = new Gson();
//                    String param = gson.toJson(placeOrderRequest);
//                    Log.d("PARAMS_PlaceOrder", param);
//                    placeOrder(placeOrderRequest);
//                    //Toast.makeText(PaymentActivity.this,"Success",Toast.LENGTH_SHORT);
//                }else{
//                    Log.d("wallet>>","Failed");
//                    dialogView.errorButtonDialog(PaymentActivity.this, getResources().getString(R.string.app_name), "You do not have sufficient balance in your wallet");
//                }
            }
        });

        payCodRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_extra_payment.setVisibility(View.GONE);
                payment_method = "2";
                payOnlineRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                payWalletRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                payCodRl.setBackground(getResources().getDrawable(R.drawable.payment_selected));
                tvWallet.setTextColor(getResources().getColor(R.color.black));
                tvOnline.setTextColor(getResources().getColor(R.color.black));
                tvCod.setTextColor(getResources().getColor(R.color.colorAccent));
//                if (restID != null){
//                    Log.d("RESTID3", restID);
//                }else{
//                    Log.d("RESTID3", "1234");
//                }
//
//                PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(
//
//                        restID,
//                        prefs.getData(Constants.USER_ID),
//                        prefs.getData(Constants.USER_NAME),
//                        prefs.getData(Constants.USER_MOBILE),
//                        prefs.getData(Constants.USER_EMAIL),
//                        Constants.addressListModel.address,
//                        Constants.addressListModel.location,
//                        Constants.addressListModel.country,
//                        Constants.addressListModel.city,
//                        Constants.addressListModel.pin,
//                        Constants.addressListModel.lat,
//                        Constants.addressListModel.lng,
//                        totItemPrice,
//                        discountCode,
//                        discount,
//                        "test-cod",
//                        "0",
//                        Constants.notes
//                );
//
//                Gson gson = new Gson();
//                String param = gson.toJson(placeOrderRequest);
//                Log.d("PARAMS_PlaceOrder", param);
//                placeOrder(placeOrderRequest);
            }
        });
        
        payOnlineRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_extra_payment.setVisibility(View.GONE);
                payment_method = "1";
                payOnlineRl.setBackground(getResources().getDrawable(R.drawable.payment_selected));
                payWalletRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                payCodRl.setBackground(getResources().getDrawable(R.drawable.payment_no_selected));
                tvWallet.setTextColor(getResources().getColor(R.color.black));
                tvOnline.setTextColor(getResources().getColor(R.color.colorAccent));
                tvCod.setTextColor(getResources().getColor(R.color.black));
//                String samount = totAmount;
//
//                // rounding off the amount.
//                int amount = Math.round(Float.parseFloat(samount) * 100);
//
//                // initialize Razorpay account.
//                Checkout checkout = new Checkout();
//
//                // set your id as below
//                checkout.setKeyID(getResources().getString(R.string.razorpay_key));
//
//                // set image
//                checkout.setImage(R.drawable.fdxlogo);
//
//                // initialize json object
//                JSONObject object = new JSONObject();
//                try {
//                    // to put name
//                    object.put("name", "FOOD EXPRESS ONLINE");
//
//                    // put description
//                    object.put("description", "Test payment");
//
//                    // to set theme color
//                    object.put("theme.color", "");
//
//                    // put the currency
//                    object.put("currency", "INR");
//
//                    // put amount
//                    object.put("amount", amount);
//
//                    // put mobile number
//                    object.put("prefill.contact", prefs.getData(Constants.USER_MOBILE));
//
//                    // put email
//                    object.put("prefill.email", prefs.getData(Constants.USER_EMAIL));
//
//                    // open razorpay to checkout activity
//                    checkout.open(PaymentActivity.this, object);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
        
    }

    private void placeOrder(PlaceOrderRequest placeOrderRequest) {

        dialogView.showCustomSpinProgress(PaymentActivity.this);

        manager.service.placeOrder(placeOrderRequest).enqueue(new Callback<PlaceOrderResponseModel>() {
            @Override
            public void onResponse(Call<PlaceOrderResponseModel> call, Response<PlaceOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    PlaceOrderResponseModel pom = response.body();
                    Log.d("ORDER_RESP>>", response.body().toString());
                    if (pom.error != true){
                        PlacedOrderDetailsModel placedOrderDetailsModel = new PlacedOrderDetailsModel();
                        placedOrderDetailsModel = pom.order;
                        order_completed = 1;
                        Constants.notes = "";

                        showOrderPlacedPopup(placedOrderDetailsModel.id);
                        //dialogView.showSingleButtonDialog(PaymentActivity.this, getResources().getString(R.string.app_name), "Order Placed successfully!");
                        Constants.addressSelected = false;
                        Constants.TAG = 0;
                        Constants.cartPrice = 0;
                        Constants.tip = "0";
                        Constants.isCouponSelected = 0;
                        prefs.saveData(Constants.isCancelable, "1");
                        prefs.saveData(Constants.RestId_FromCart, "");
                        //if (Constants.isDialogOn == 0){
                           // startActivity(new Intent(PaymentActivity.this, Dashboard.class));
                           // finishAffinity();
                       // }

                        if (!cancel_order_charge.equals("0")){
                            prefs.saveData(Constants.USER_CANCEL_AMOUNT, "0");
                        }

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void showOrderPlacedPopup(String id) {
        Dialog dialog = new Dialog(PaymentActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.place_order_popup_lay);
        dialog.setCancelable(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(PaymentActivity.this);
        View promptView = layoutInflater.inflate(R.layout.place_order_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(PaymentActivity.this).create();*/
        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);


        Button btnCancel = (Button) dialog.findViewById(R.id.btnMyOrder);
        //btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnHome);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentActivity.this, DashboardHome.class));
                finish();

                //deleteFromCart(id, pos);


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderStatusActivity.orderId = id;
                OrderStatusActivity.from = "Pay";
                startActivity(new Intent(PaymentActivity.this, OrderStatusActivity.class));
                finish();
                //alertD.dismiss();

            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //alertD.setView(promptView);
        dialog.show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();

        PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest(

                restID,
                prefs.getData(Constants.USER_ID),
                prefs.getData(Constants.USER_NAME),
                prefs.getData(Constants.USER_MOBILE),
                prefs.getData(Constants.USER_EMAIL),
                Constants.addressListModel.address,
                Constants.addressListModel.location,
                Constants.addressListModel.country,
                Constants.addressListModel.city,
                Constants.addressListModel.pin,
                Constants.addressListModel.lat,
                Constants.addressListModel.lng,
                totItemPrice,
                discountCode,
                discount,
                s,
                Constants.tip,
                Constants.notes,
                paid_online,
                paid_by_wallet,
                Constants.order_delivery_type,
                cancel_order_charge,
                Constants.offerId,
                schedule_date,
                schedule_time
        );
       // transactionId = s;
        placeOrder(placeOrderRequest);
    }


    @Override
    public void onPaymentError(int i, String s) {

        //Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Payment Failed!", Toast.LENGTH_SHORT).show();

    }

    private void getUserWallets(String id){
        dialogView.showCustomSpinProgress(PaymentActivity.this);
        manager.service.userWallets(id).enqueue(new Callback<WalletListResponseModel>() {
            @Override
            public void onResponse(Call<WalletListResponseModel> call, Response<WalletListResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    WalletListResponseModel nrm = response.body();
                    if (!nrm.error){
                        wallet_balance = nrm.wallet_balance;

                        if (nrm.wallet_balance.equals("0")){
                            tv_wallet_balance.setText("₹ 0.00");
                        }else{
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted1 = formatter1.format(Double.parseDouble(wallet_balance));
                            if (formatted1.equals(".00")){
                                formatted1 = "0.00";
                            }
                            tv_wallet_balance.setText("₹ " + formatted1);
                        }

                        //tv_wallet_balance.setText("₹ " + wallet_balance);
                    }else{

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<WalletListResponseModel> call, Throwable t) {

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
        if (order_completed==1){
            startActivity(new Intent(PaymentActivity.this, DashboardHome.class));
            finish();
        }else {
            startActivity(new Intent(PaymentActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
            finish();
        }

    }
}