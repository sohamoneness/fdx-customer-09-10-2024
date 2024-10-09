package com.fdxUser.app.Activity.OtherScreens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fdxUser.app.Activity.OrderHistory.OrderHistory;
import com.fdxUser.app.Activity.OrderHistory.OrderSummeryActivity;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Models.CancelOrderModels.CancelOrderRequestModel;
import com.fdxUser.app.Models.CancelOrderModels.CancelOrderResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {
    ImageView ivBack;
    public static String orderId = "";
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;
    String orderStatus = "";
    TextView tvTimer;

    String restId = "", orderRefId = "", restImg = "";
    public static String from = "";

    TextView tvRestName, tvRestAddress, tvDelName, tvDelAddress;
    ImageView ivRequestOrder, ivOrderPrepare, ivDelBoyArrive, ivOrderPick, ivOrderDelivered,ivDelBoyAssigned;
    TextView tvOrderStatus, tvOrderTime, tvOrderPrepare, tvOrderPrepareTime,tvDelboy, tvDelboyTime;
    Button btnFeedback, btnCancel;
    TextView tvOrderPick, tvOrderPickTime, tvOrderDelivered, tvOrderDelTime, tvDelboyAssign, tvDelboyAssignTime;
    ImageView imgCall;
    ShapeableImageView ivRest;
    RelativeLayout rl_delivery_by,delivery_boy_arrived_rl,order_picked_rl;

    private final int CALL_REQUEST = 100;
    OrderDetailsModel orderDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(OrderStatusActivity.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAddress = findViewById(R.id.tvRestAddress);
        tvDelName = findViewById(R.id.tvDelName);
        tvDelAddress = findViewById(R.id.tvDelAddress);
        ivRest = (ShapeableImageView) findViewById(R.id.ivRest);
        btnFeedback = findViewById(R.id.btnFeedback);
        tvTimer = findViewById(R.id.tvTimer);
        btnCancel = findViewById(R.id.btnOrderCancel);

        ivRequestOrder = findViewById(R.id.ivRequestOrder);
        ivOrderPrepare = findViewById(R.id.ivOrderPrepare);
        ivDelBoyArrive = findViewById(R.id.ivDelBoyArrive);
        ivOrderPick = findViewById(R.id.ivOrderPick);
        ivOrderDelivered = findViewById(R.id.ivOrderDelivered);
        ivDelBoyAssigned = findViewById(R.id.ivDelBoyAssigned);

        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderPrepare = findViewById(R.id.tvOrderPrepare);
        tvOrderPrepareTime = findViewById(R.id.tvOrderPrepareTime);
        tvDelboy = findViewById(R.id.tvDelboy);
        tvDelboyTime = findViewById(R.id.tvDelboyTime);
        tvOrderPick = findViewById(R.id.tvOrderPick);
        tvOrderPickTime = findViewById(R.id.tvOrderPickTime);
        tvOrderDelivered = findViewById(R.id.tvOrderDelivered);
        tvOrderDelTime = findViewById(R.id.tvOrderDelTime);
        tvDelboyAssign = findViewById(R.id.tvDelboyAssign);
        tvDelboyAssignTime = findViewById(R.id.tvDelboyAssignTime);

        rl_delivery_by = findViewById(R.id.rl_delivery_by);
        delivery_boy_arrived_rl = findViewById(R.id.delivery_boy_arrived_rl);
        order_picked_rl = findViewById(R.id.order_picked_rl);

        if (from.equals("Order Summery")){
            btnCancel.setVisibility(View.GONE);
        }else{
            btnCancel.setVisibility(View.VISIBLE);
        }

        //if (prefs.getData(Constants.isCancelable).equals("1")){


        //}else{
        //    btnCancel.setVisibility(View.GONE);
       // }



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FeedbackActivity.orderDetailsModel = orderDetailsModel;

                startActivity(new Intent(OrderStatusActivity.this, FeedbackActivity.class)
                        .putExtra(Constants.REST_ID, restId)
                        .putExtra(Constants.ORDER_REF_ID, orderRefId)
                        .putExtra(Constants.REST_NAME, tvRestName.getText().toString())
                        .putExtra(Constants.REST_IMG, restImg));
                        //.putExtra(Constants.DELBOY_NAME,)

            }
        });

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                try {
                    getOrderStatus(orderId);

                } catch (Exception e) {

                }


                handler.postDelayed(this, 5000);
            }
        };
        runnable.run();


        imgCall = findViewById(R.id.imgCall);

        //Glide.with(OrderStatusActivity.this).load(restImg).into(ivRest);

    }

    private void showPopup() {
        final Dialog popupdialog = new Dialog(OrderStatusActivity.this);
        //popupdialog.setContentView(R.layout.ownerpopup);
        popupdialog.setContentView(R.layout.cancel_order_popup);

        popupdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //final EditText visitnote =(EditText)popupdialog.findViewById(R.id.visitnote);
        //final Spinner visit_note_cat_sp =(Spinner)popupdialog.findViewById(R.id.visit_note_cat_sp);
        final EditText etReason = (EditText) popupdialog.findViewById(R.id.etReason);

        Button btnCancel = (Button) popupdialog.findViewById(R.id.btnNo);
        Button btnOk = (Button) popupdialog.findViewById(R.id.btnYes);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etReason.getText().toString().equals("")){
                    Toast.makeText(OrderStatusActivity.this, "Please enter your reason of cancellation!", Toast.LENGTH_SHORT).show();
                }else {
                    CancelOrderRequestModel cancelOrderRequestModel = new CancelOrderRequestModel(
                            orderId,
                            etReason.getText().toString()
                    );
                    cancelOrder(cancelOrderRequestModel, popupdialog);
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupdialog.dismiss();
            }
        });

        //popupImg.setImageResource(iconId);
        //txt.setText(msg);
        popupdialog.setCanceledOnTouchOutside(true);
        popupdialog.setCancelable(true);

        popupdialog.show();
    }

    private void cancelOrder(CancelOrderRequestModel cancelOrderRequestModel, Dialog popupdialog) {
        dialogView.showCustomSpinProgress(OrderStatusActivity.this);
        manager.service.cancelOrder(cancelOrderRequestModel).enqueue(new Callback<CancelOrderResponseModel>() {
            @Override
            public void onResponse(Call<CancelOrderResponseModel> call, Response<CancelOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CancelOrderResponseModel corm = response.body();
                    if (!corm.error){
                        popupdialog.dismiss();
                        Toast.makeText(OrderStatusActivity.this, "Order cancelled!", Toast.LENGTH_SHORT).show();
                        showOrderCancelPopup();
                        //tvTimer.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);
                    }else {

                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }
            @Override
            public void onFailure(Call<CancelOrderResponseModel> call, Throwable t) {
                dialogView.dismissProgress();
            }
        });
    }


    private void getOrderStatus(String orderId) {
        //dialogView.showCustomSpinProgress(OrderStatusActivity.this);

        manager.service.getOrderDetails(orderId).enqueue(new Callback<OrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<OrderDetailsResponseModel> call, Response<OrderDetailsResponseModel> response) {
                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();
                    OrderDetailsResponseModel orderDetailsResponseModel = response.body();
                    if(orderDetailsResponseModel.error != true){


                        orderDetailsModel = orderDetailsResponseModel.orderData;

                        if (orderDetailsModel.order_type.equals("2")){
                            rl_delivery_by.setVisibility(View.GONE);
                            delivery_boy_arrived_rl.setVisibility(View.GONE);
                            order_picked_rl.setVisibility(View.GONE);
                        }else{
                            rl_delivery_by.setVisibility(View.VISIBLE);
                            delivery_boy_arrived_rl.setVisibility(View.VISIBLE);
                            order_picked_rl.setVisibility(View.VISIBLE);
                        }

                        orderStatus = orderDetailsModel.status;
                        Log.d("ORD_STAT>>", orderStatus);

                        tvRestName.setText(orderDetailsModel.restaurant.name);
                        tvRestAddress.setText(orderDetailsModel.restaurant.address);
                        restImg = orderDetailsModel.restaurant.image;

                        if(!orderDetailsModel.delivery_boy_id.equals("0")){
                            tvDelName.setText(orderDetailsModel.boy.name);
                            if (orderStatus.equals("7") || orderStatus.equals("8")){
                                imgCall.setVisibility(View.VISIBLE);
                            }else {
                                imgCall.setVisibility(View.GONE);
                            }

                        }else{
                            tvDelName.setText("Delivery Agent Is Not Assigned");
                            imgCall.setVisibility(View.GONE);
                        }

                        imgCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!orderDetailsModel.delivery_boy_id.equals("0")){
                                    callPhoneNumber(orderDetailsModel.boy.mobile);
                                }else{
                                    tvDelName.setText("Delivery Agent Is Not Assigned");
                                }
                            }
                        });

                        if (orderDetailsModel.is_cancellable.equals("1")){
                            btnCancel.setVisibility(View.VISIBLE);
                            //Toast.makeText(OrderStatusActivity.this, orderDetailsModel.timer, Toast.LENGTH_SHORT).show();
                            Log.d("TIME_SPAN", orderDetailsModel.timer);
                            int interval = Integer.parseInt(orderDetailsModel.timer) * 1000;

                            new CountDownTimer(interval, 1000) {

                                public void onTick(long millisUntilFinished) {

                                    // Used for formatting digit to be in 2 digits only

                                    NumberFormat f = new DecimalFormat("00");

                                    // long hour = (millisUntilFinished / 3600000) % 24;

                                    long min = (millisUntilFinished / 60000) % 60;

                                    long sec = (millisUntilFinished / 1000) % 60;

                                    btnCancel.setText("(" + f.format(min) + ":" + f.format(sec) + ") Cancel Order");

                                }

                                // When the task is over it will print 00:00:00 there

                                public void onFinish() {

                                    btnCancel.setText("(00:00) Cancel Order");
                                    //tvTimer.setVisibility(View.GONE);
                                    btnCancel.setVisibility(View.GONE);
                                    //prefs.saveData(Constants.isCancelable, "0");

                                }

                            }.start();
                        }else {
                            btnCancel.setVisibility(View.GONE);
                        }



                        tvDelAddress.setText(orderDetailsModel.delivery_address);

                        orderRefId = orderDetailsModel.unique_id;
                        restId = orderDetailsModel.restaurant_id;

                        if (orderStatus.equals("1")){
                            tvOrderStatus.setText("Order request pending");
                            tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order preparing by restaurant");
                           // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_request));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }

                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.pick_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            /*if(prefs.getData(Constants.isCancelable).equals("0")){
                                btnCancel.setVisibility(View.GONE);
                            }else {
                                btnCancel.setVisibility(View.VISIBLE);
                            }*/



                        }else if(orderStatus.equals("2")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Assigning delivery boy");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order preparing by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arriving));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.pick_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);


                        }else if(orderStatus.equals("3")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Preparing your order");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.pick_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                        }else if(orderStatus.equals("4")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order prepared by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy started towards restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            //if (orderStatus)
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.pick_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                        }else if(orderStatus.equals("5")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order prepared by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arriving at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arriving));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.pick_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                        } else if(orderStatus.equals("6")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order prepared by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Delivery boy picking order from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered to the customer");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.picking_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivery));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                        }else if(orderStatus.equals("7")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                           // tvOrderPrepare.setText("Order prepared by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Delivery boy is arriving at your door step soon");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.picked_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.delivering_order));
                            btnFeedback.setVisibility(View.GONE);
                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                        }else if(orderStatus.equals("8")){

                            tvOrderStatus.setText("Order accepted by restaurant");
                            //tvOrderTime.setText(orderDetailsModel.updated_at);
                            tvDelboyAssign.setText("Delivery boy assigned");
                            //tvDelboyAssignTime.setText("");
                            //tvOrderPrepare.setText("Order prepared by restaurant");
                            // tvOrderPrepareTime.setText(orderDetailsModel.updated_at);
                            tvDelboy.setText("Delivery boy arrived at restaurant");
                            //tvDelboyTime.setText("");
                            tvOrderPick.setText("Order picked from restaurant");
                            //tvOrderPickTime.setText("");
                            tvOrderDelivered.setText("Order delivered");
                            //tvOrderDelTime.setText("");
                            ivRequestOrder.setImageDrawable(getResources().getDrawable(R.drawable.order_accepted));
                            ivDelBoyAssigned.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            if (orderDetailsModel.is_ready.equals("1")){
                                ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_prepared));
                                tvOrderPrepare.setText("Order is prepared by restaurant");
                            }else{
                                if (!orderDetailsModel.preparation_time.equals("0")){
                                    if (!orderDetailsModel.extra_preparation_time.equals("0")) {
                                        tvOrderPrepare.setText("It's taking longer than usual");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.taking_longer_time));
                                    }else{
                                        tvOrderPrepare.setText("Your food is being prepared");
                                        ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.preparing_order));
                                    }
                                }else{
                                    tvOrderPrepare.setText("Restaurant hasn't accept your order yet");
                                    ivOrderPrepare.setImageDrawable(getResources().getDrawable(R.drawable.order_preparation));
                                }

                            }
                            ivDelBoyArrive.setImageDrawable(getResources().getDrawable(R.drawable.del_boy_arrived));
                            ivOrderPick.setImageDrawable(getResources().getDrawable(R.drawable.picked_order));
                            ivOrderDelivered.setImageDrawable(getResources().getDrawable(R.drawable.order_delivered));

                            btnCancel.setVisibility(View.GONE);
                            tvTimer.setVisibility(View.GONE);

                            btnFeedback.setVisibility(View.VISIBLE);


                        }

                    }else{
                        /*btnCancel.setVisibility(View.GONE);
                        tvOrderStatus.setText("Order Cancelled");*/
                    }


                }else{
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // OrderSummeryActivity.ordId = orderId;
        /*if (Constants.isFromHomeToOrderStat){*/
        if(from.equals("Pay")){
            startActivity(new Intent(OrderStatusActivity.this, DashboardHome.class));
            finish();
        }else if (from.equals("Home")){
            startActivity(new Intent(OrderStatusActivity.this, DashboardHome.class));
            finish();
        }else {
            OrderSummeryActivity.ordId = orderId;
            startActivity(new Intent(OrderStatusActivity.this, OrderSummeryActivity.class));
            finish();
        }

       /* }else {
            startActivity(new Intent(OrderStatusActivity.this, OrderHistory.class));
            finish();
        }*/

    }

    private void showOrderCancelPopup() {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderStatusActivity.this);
        View promptView = layoutInflater.inflate(R.layout.order_cancel_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(OrderStatusActivity.this).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);


        Button btnCancel = (Button) promptView.findViewById(R.id.btnMyOrder);
        //btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnHome);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderStatusActivity.this, DashboardHome.class));
                finishAffinity();

                //deleteFromCart(id, pos);
                alertD.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OrderStatusActivity.orderId = id;
                OrderStatusActivity.from = "";
                startActivity(new Intent(OrderStatusActivity.this, OrderHistory.class));
                finish();
                alertD.dismiss();

            }
        });

        alertD.setView(promptView);
        alertD.show();
    }

    /**
     * This method is for calling phone no
     * @param phone
     */
    public void callPhoneNumber(String phone) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(OrderStatusActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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