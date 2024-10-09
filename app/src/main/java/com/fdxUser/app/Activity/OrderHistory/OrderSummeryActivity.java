package com.fdxUser.app.Activity.OrderHistory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OtherScreens.OrderStatusActivity;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Adapters.OrderItemAdapter;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderInvoiceResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderItemModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderRequestModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderResponseModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ResponseRestDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsRequestModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummeryActivity extends AppCompatActivity {

    RecyclerView pastOrderItemRv;
    List<OrderItemModel> orderItemList = new ArrayList<>();
    OrderItemAdapter orderItemAdapter;
    public static String ordId = "";

    TextView tvTotPrice, tvOrderNumber, tvPayment, tvDate, tvPhn, tvDelTo, tvDelBy, tvTips,tvDownloadSummary;
    TextView tvDelFee, tvPackingChrg, tvTax, tvPayType;
    TextView tvRestName, tvRestAdr, tvTransId;
    TextView tvDiscountAmount, tvReason;
    LinearLayout li_status_bg;
    TextView orderStatus,itemCount,tvSubtotal,tvDelToTxt;
    RelativeLayout rl_complete, rl_delivery_fee,rl_tip;

    ImageView iv_back;
    Button btnOrderStat,btnOrderRepeat;

    Prefs prefs;
    DialogView dialogView;
    ShapeableImageView ivRest;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    ApiManager manager1 = new ApiManager();
    LinearLayout li_cancel;
    String restId = "";
    String dirpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summery);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(OrderSummeryActivity.this);
        dialogView = new DialogView();

        pastOrderItemRv = findViewById(R.id.pastOrderItemRv);
        tvTotPrice = findViewById(R.id.tvTotPrice);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvPayment = findViewById(R.id.tvPayment);
        tvDate = findViewById(R.id.tvDate);
        tvPhn = findViewById(R.id.tvPhn);
        tvDelTo = findViewById(R.id.tvDelTo);
        tvDelBy = findViewById(R.id.tvDelBy);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvPackingChrg = findViewById(R.id.tvPackingChrg);
        tvTax = findViewById(R.id.tvTax);
        tvPayType = findViewById(R.id.tvPayType);
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAdr = findViewById(R.id.tvRestAdr);
        iv_back = findViewById(R.id.iv_back);
        tvTransId = findViewById(R.id.tvTransId);
        btnOrderStat = findViewById(R.id.btnOrderStat);
        ivRest = findViewById(R.id.ivRest);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tvTips = findViewById(R.id.tvTips);
        itemCount = findViewById(R.id.itemCount);
        li_cancel = findViewById(R.id.li_cancel);
        tvReason = findViewById(R.id.tvReason);

        tvSubtotal = findViewById(R.id.tvSubtotal);
        orderStatus = findViewById(R.id.orderStatus);
        li_status_bg = findViewById(R.id.li_status_bg);
        btnOrderRepeat = findViewById(R.id.btnOrderRepeat);
        rl_complete = findViewById(R.id.rl_complete);
        tvDownloadSummary = findViewById(R.id.tvDownloadSummary);

        rl_delivery_fee= findViewById(R.id.rl_delivery_fee);
        rl_tip = findViewById(R.id.rl_tip);
        tvDelToTxt = findViewById(R.id.tvDelToTxt);

       // makeOrderItemList();
        getOrderDetails(ordId);

        tvDownloadSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrderInvoice(ordId);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnOrderStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.isFromHomeToOrderStat = true;
                OrderStatusActivity.orderId = ordId;
                OrderStatusActivity.from = "Order Summery";
                startActivity(new Intent(OrderSummeryActivity.this, OrderStatusActivity.class));
                finish();
            }
        });

        btnOrderRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                if (Constants.restIdFromCart.equals(restId) || Constants.restIdFromCart.equals("")){
                    RepeatOrderRequestModel repeatOrderRequestModel = new RepeatOrderRequestModel(
                            ordId
                    );
                    repeatOrderOrderRequest(repeatOrderRequestModel);
                }else {
                    clearCart(prefs.getData(Constants.USER_ID), ordId);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Constants.isFromHomeToOrderStat){
            Constants.isFromHomeToOrderStat = false;
            startActivity(new Intent(OrderSummeryActivity.this, DashboardHome.class));
            finish();
        }else if (Constants.isFromHomeToSearch){
            Constants.isFromHomeToSearch = false;
            startActivity(new Intent(OrderSummeryActivity.this, SearchActivity.class));
            finish();
        }else if (Constants.isFromRestToHist){
            Constants.isFromRestToHist = false;
            //startActivity(new Intent(OrderSummeryActivity.this, OrderHistory.class));
            finish();
        }else {
            startActivity(new Intent(OrderSummeryActivity.this, OrderHistory.class));
            finish();
        }

    }

    private void clearCart(String data, String id) {
        dialogView.showCustomSpinProgress(OrderSummeryActivity.this);
        manager.service.clearCartData(data).enqueue(new Callback<ClearCartResponseModel>() {
            @Override
            public void onResponse(Call<ClearCartResponseModel> call, Response<ClearCartResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ClearCartResponseModel clearCartResponseModel = response.body();
                    if (!clearCartResponseModel.error) {
                        RepeatOrderRequestModel repeatOrderRequestModel = new RepeatOrderRequestModel(
                                id
                        );
                        repeatOrderOrderRequest(repeatOrderRequestModel);

                    } else {

                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ClearCartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void getOrderDetails(String id) {
        dialogView.showCustomSpinProgress(OrderSummeryActivity.this);

        manager.service.getOrderDetails(id).enqueue(new Callback<OrderDetailsResponseModel>() {
            @Override
            public void onResponse(Call<OrderDetailsResponseModel> call, Response<OrderDetailsResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    OrderDetailsResponseModel orderDetailsResponseModel = response.body();
                    if(orderDetailsResponseModel.error != true){

                        orderItemList = new ArrayList<>();

                        OrderDetailsModel orderDetailsModel = orderDetailsResponseModel.orderData;

                        Log.d("type>>",orderDetailsModel.order_type);

                        if (orderDetailsModel.order_type.equals("2")){
                            rl_delivery_fee.setVisibility(View.GONE);
                            rl_tip.setVisibility(View.GONE);
                            tvDelToTxt.setVisibility(View.GONE);
                            tvDelTo.setVisibility(View.GONE);
                        }else{
                            rl_delivery_fee.setVisibility(View.VISIBLE);
                            rl_tip.setVisibility(View.VISIBLE);
                            tvDelToTxt.setVisibility(View.VISIBLE);
                            tvDelTo.setVisibility(View.VISIBLE);
                        }

                        orderItemList = orderDetailsModel.items;

                        tvOrderNumber.setText(orderDetailsModel.unique_id);
                        tvDate.setText(orderDetailsModel.created_at);
                        if (orderDetailsModel.payment_status.equals("1")){
                            Log.d("transaction>>",orderDetailsModel.transaction_id);
                            if (orderDetailsModel.transaction_id.equals("test-cod")){
                                tvPayment.setText("Paid using cash");
                            }else if (orderDetailsModel.transaction_id.equals("test-wallet")){
                                tvPayment.setText("Paid from Wallet");
                            }else {
                                tvPayment.setText("Online Payment through Razorpay");
                            }
                        }else{
                            tvPayment.setText("Unpaid");
                        }

                        restId = orderDetailsModel.restaurant_id;

                        tvDelBy.setText("");
                        tvTransId.setText(orderDetailsModel.transaction_id);
                        tvDelTo.setText(orderDetailsModel.delivery_address);
                        tvPhn.setText(orderDetailsModel.mobile);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(orderDetailsModel.delivery_charge));
                        tvDelFee.setText("₹ " + formatted);

                        String formatted2 = "0.00";
                        if (!orderDetailsModel.packing_price.equals("0")){
                            formatted2 = formatter1.format(Double.parseDouble(orderDetailsModel.packing_price));
                        }else{
                            formatted2 = "0.00";
                        }
                        tvPackingChrg.setText("₹ " + formatted2);

                        String formatted3 = "0.00";
                        if (!orderDetailsModel.tax_amount.equals("0")) {
                            formatted3 = formatter1.format(Double.parseDouble(orderDetailsModel.tax_amount));
                        }else{
                            formatted3 = "0.00";
                        }
                        tvTax.setText("₹ " + formatted3);
                        String formatted4 = formatter1.format(Double.parseDouble(orderDetailsModel.amount));
                        tvSubtotal.setText("₹ " + formatted4);

                        String formatted1 = formatter1.format(Double.parseDouble(orderDetailsModel.total_amount));
                        tvTotPrice.setText("₹ " + formatted1);

                        String formatted6 = "0.00";
                        if (!orderDetailsModel.discounted_amount.equals("0")) {
                            formatted6 = formatter1.format(Double.parseDouble(orderDetailsModel.discounted_amount));
                        }else{
                            formatted6 = "0.00";
                        }
                        tvDiscountAmount.setText("₹ " + formatted6);

                        if (orderDetailsModel.transaction_id.equals("")){
                            tvPayType.setText("Cash Payment");
                        }else {
                            tvPayType.setText("Online Payment");
                        }

                        tvRestName.setText(orderDetailsModel.restaurant.name);
                        tvRestAdr.setText(orderDetailsModel.restaurant.address);

                        String formatted7 = "0.00";
                        if (!orderDetailsModel.tips.equals("0")) {
                            formatted7 = formatter1.format(Double.parseDouble(orderDetailsModel.tips));
                        }else{
                            formatted7 = "0.00";
                        }
                        tvTips.setText("₹ " + formatted7);

                        if (orderDetailsModel.restaurant.image != null){
                            Glide.with(OrderSummeryActivity.this).load(orderDetailsModel.restaurant.image).into(ivRest);
                        }else {
                            Glide.with(OrderSummeryActivity.this).load(R.drawable.ic_no_image).into(ivRest);
                        }

                        if (orderDetailsModel.status.equals("1")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("New");
                        }else if (orderDetailsModel.status.equals("2")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("3")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("4")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("5")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("6")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("7")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status1));
                            orderStatus.setTextColor(getResources().getColor(R.color.black));
                            orderStatus.setText("Ongoing");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }else if (orderDetailsModel.status.equals("10")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status2));
                            orderStatus.setTextColor(getResources().getColor(R.color.red3));
                            orderStatus.setText("Cancelled");
                            li_cancel.setVisibility(View.VISIBLE);
                            if (orderDetailsModel.cancellation_reason.equals("")){
                                tvReason.setText("Note : " + orderDetailsModel.cancel_order_reason);
                            }else {
                                tvReason.setText("Note : " + orderDetailsModel.cancellation_reason);
                            }

                            btnOrderStat.setVisibility(View.GONE);
                        }else if (orderDetailsModel.status.equals("9") || orderDetailsModel.status.equals("8")){
                            li_status_bg.setBackground(getResources().getDrawable(R.drawable.rounded_corner_status));
                            orderStatus.setText("Delivered");
                            li_cancel.setVisibility(View.GONE);
                            btnOrderStat.setVisibility(View.VISIBLE);
                        }

                        itemCount.setText(String.valueOf(orderItemList.size())+" items in this order");
                        if (orderItemList.size() > 0){
                            orderItemAdapter = new OrderItemAdapter(OrderSummeryActivity.this, orderItemList);
                            pastOrderItemRv.setLayoutManager(new LinearLayoutManager(OrderSummeryActivity.this, LinearLayoutManager.VERTICAL, false));
                            pastOrderItemRv.setAdapter(orderItemAdapter);
                        }

                    }else{

                    }


                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponseModel> call, Throwable t) {

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

    private void repeatOrderOrderRequest(RepeatOrderRequestModel repeatOrderRequestModel) {

        dialogView.showCustomSpinProgress(OrderSummeryActivity.this);

        manager1.service.repeatOrder(repeatOrderRequestModel).enqueue(new Callback<RepeatOrderResponseModel>() {
            @Override
            public void onResponse(Call<RepeatOrderResponseModel> call, Response<RepeatOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RepeatOrderResponseModel repeatOrderResponseModel = response.body();

                    if (!repeatOrderResponseModel.error) {
                        //Toast.makeText(context, "Order Ready!", Toast.LENGTH_SHORT).show();
                        //((Activity)context).startActivity(new Intent((Activity)context, CartActivity.class));
                        //((Activity)context).finish();

                        RestaurantDetailsRequestModel restaurantDetailsRequestModel = new RestaurantDetailsRequestModel(
                                prefs.getData(Constants.USER_ID),
                                repeatOrderResponseModel.restaurant_id
                        );

                        getRestDetails(restaurantDetailsRequestModel, repeatOrderResponseModel.delivery_lat, repeatOrderResponseModel.delivery_lng, "");
                    } else {

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RepeatOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getOrderInvoice(String orderId) {

        dialogView.showCustomSpinProgress(OrderSummeryActivity.this);

        manager1.service.getOrderInvoice(orderId).enqueue(new Callback<OrderInvoiceResponseModel>() {
            @Override
            public void onResponse(Call<OrderInvoiceResponseModel> call, Response<OrderInvoiceResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    OrderInvoiceResponseModel orderInvoiceResponseModel = response.body();

                    if (!orderInvoiceResponseModel.error) {
                        Log.d("pdf>>",orderInvoiceResponseModel.file_link);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(orderInvoiceResponseModel.file_link));
                        startActivity(intent);
                    } else {

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderInvoiceResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getRestDetails(RestaurantDetailsRequestModel restID, String lat, String lng, String isVeg) {
        dialogView.showCustomSpinProgress(OrderSummeryActivity.this);
        manager.service.getRestaurantDetails(restID, lat, lng, isVeg).enqueue(new Callback<RestaurantDetailsModel>() {
            @Override
            public void onResponse(Call<RestaurantDetailsModel> call, Response<RestaurantDetailsModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    RestaurantDetailsModel rdm = response.body();

                    if (!rdm.error) {
                        ResponseRestDetailsModel rrdm = rdm.restaurantData;
                        RestaurantModel restM = rrdm.restaurant;
                        Constants.restaurantModel = rrdm.restaurant;

                        Constants.restName = restM.name;
                        Constants.restRating = restM.rating;
                        Constants.REST_ID = restM.id;

                        startActivity(new Intent(OrderSummeryActivity.this, CartActivity.class));
                        finish();
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

    public void layoutToImage(View view) {
        // convert view group to bitmap
        rl_complete.setDrawingCacheEnabled(true);
        rl_complete.buildDrawingCache();
        Bitmap bm = rl_complete.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document();
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/NewPDF.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }*/
}