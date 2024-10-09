package com.fdxUser.app.Activity.RestaurantScreens;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.CartItemAdapter;
import com.fdxUser.app.Adapters.CompleteMealAdapter;
import com.fdxUser.app.CustomFonts.ManropeExtraBoldTextView;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.AddressModels.AddressResponseModel;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.CartModels.UpsellItemModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeRequestModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView orderRv, completeMealRv;
    TextView tvItems, tvItemPrice, tvDelFee, tvTax, tvTotPrice;
    TextView tvTotItemPrice, tvAddItem, tv_sur_charge;
    ManropeExtraBoldTextView completeHeaderTv;
    ImageView ivBack, ivInfo;
    //List<CartItemModel> cartItemList = new ArrayList<>();
    List<CartsModel> cartList = new ArrayList<>();
    List<UpsellItemModel> upsellItemList = new ArrayList<>();
    //List<CompleteMealModel> completeMealList = new ArrayList<>();
    CartItemAdapter ciAdapter;
    CompleteMealAdapter completeMealAdapter;
    Button btnChkOut;

    DialogView dialogView;
    Prefs prefs;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    String userID = "", restID = "";

    CartItemsResponseModel cartItemsResponseModel;

    LinearLayout mainLay, emptyLay, li_discount, li_get_discount, delivery_type_li, li_sur_charge;
    Button btnAddItem;
    TextView tv_discount_txt, tv_get_discount_txt, tvDiscount, delivery_txt, takeaway_txt;

    public static int is_both = 0;
    String taxRate = "";
    TextView tvClear;
    public static String isForm = "";
    String offerId = "";

    List<AddressListModel> addressList = new ArrayList<>();
    double total = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
//        }

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(CartActivity.this);
        dialogView = new DialogView();
        CartItemsResponseModel cirm = new CartItemsResponseModel();


        restID = getIntent().getStringExtra(Constants.REST_ID);
        if (restID != null) {
            Log.d("RESTID", restID);
        } else {
            Log.d("RESTID", "1234");
        }


        orderRv = findViewById(R.id.orderRv);
        tvAddItem = findViewById(R.id.tvAddItem);
        completeHeaderTv = (ManropeExtraBoldTextView) findViewById(R.id.completeHeaderTv);
        completeMealRv = findViewById(R.id.completeMealRv);
        tvClear = findViewById(R.id.tvClear);
        tvItems = findViewById(R.id.tvTotItem);
        tvTotItemPrice = findViewById(R.id.tvTotItemPrice);
        // tvItemPrice = findViewById(R.id.tvPrice);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvTax = findViewById(R.id.tvTax);
        tvTotPrice = findViewById(R.id.tvTotalPrice);
        ivBack = findViewById(R.id.iv_back);
        ivInfo = findViewById(R.id.ivInfo);
        btnChkOut = findViewById(R.id.btnChkOut);

        mainLay = findViewById(R.id.mainLay);
        emptyLay = findViewById(R.id.emptyLay);
        btnAddItem = findViewById(R.id.btnAddItem);

        li_discount = findViewById(R.id.li_discount);
        tv_discount_txt = findViewById(R.id.tv_discount_txt);
        li_get_discount = findViewById(R.id.li_get_discount);
        tv_get_discount_txt = findViewById(R.id.tv_get_discount_txt);
        tvDiscount = findViewById(R.id.tvDiscount);

        li_sur_charge = findViewById(R.id.li_sur_charge);
        tv_sur_charge = findViewById(R.id.tv_sur_charge);

        delivery_type_li = findViewById(R.id.delivery_type_li);
        delivery_txt = findViewById(R.id.delivery_txt);
        takeaway_txt = findViewById(R.id.takeaway_txt);

        userID = prefs.getData(Constants.USER_ID);


        //getCartItems();
        getCartList(userID);
        //getCompleteMeal();
        if (is_both == 1) {
            delivery_type_li.setVisibility(View.VISIBLE);
        } else {
            delivery_type_li.setVisibility(View.GONE);
        }


        /*completeMealAdapter = new CompleteMealAdapter(CartActivity.this, completeMealList);
        completeMealRv.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false));
        completeMealRv.setAdapter(completeMealAdapter);*/

        delivery_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.order_delivery_type = "1";
                delivery_type_li.setBackground(getResources().getDrawable(R.drawable.home_delivery));
                delivery_txt.setTextColor(getResources().getColor(R.color.white));
                takeaway_txt.setTextColor(getResources().getColor(R.color.black));
            }
        });

        takeaway_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.order_delivery_type = "2";
                delivery_type_li.setBackground(getResources().getDrawable(R.drawable.takeaway));
                delivery_txt.setTextColor(getResources().getColor(R.color.black));
                takeaway_txt.setTextColor(getResources().getColor(R.color.white));
            }
        });

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, prefs.getData(Constants.REST_ID)));
                finish();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.showSingleButtonDialog(CartActivity.this, "Restaurant GST @ " + taxRate + "%", "Food Express plays no role in taxes and charges levied by the govt. and restaurant");
            }
        });

        btnChkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CheckOutActivity.cartItemList = cartList;
                    CheckOutActivity.cartItemsResponseModel = cartItemsResponseModel;
                    Log.d("TEST>>", cartItemsResponseModel.total_amount + cartItemsResponseModel.carts.size());
                    Log.d("RESTID2", restID);
                    //Constants.delivery_charge = "0";
                    Constants.offerId = offerId;
                    Constants.couponCode = "";
                    //Constants.offer_discount = "0";
                    Constants.isCouponSelected = 0;
                    startActivity(new Intent(CartActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });


    }

    private void showAlert() {
        Dialog dialog = new Dialog(CartActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        //tvHeader.setText(context.getResources().getString(R.string.app_name));
        tvHeader.setVisibility(View.GONE);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Are you sure to Clear your cart ?");
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("No");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Yes");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart(prefs.getData(Constants.USER_ID));
            }
        });

        dialog.show();
    }

    private void clearCart(String data) {
        dialogView.showCustomSpinProgress(CartActivity.this);
        manager.service.clearCartData(data).enqueue(new Callback<ClearCartResponseModel>() {
            @Override
            public void onResponse(Call<ClearCartResponseModel> call, Response<ClearCartResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ClearCartResponseModel clearCartResponseModel = response.body();
                    if (!clearCartResponseModel.error) {
                        Toast.makeText(CartActivity.this, "Cart cleared successfully!", Toast.LENGTH_SHORT).show();
                        Constants.isCartEmpty = true;
                        Constants.couponCode = "";
                        prefs.saveData(Constants.RestId_FromCart, "");
                        isForm = "";
                        //context
                        startActivity(new Intent(CartActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, restID));
                        finish();

                    } else {
                        Toast.makeText(CartActivity.this, "Cart cleared successfully!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(CartActivity.this, "Server not responding properly! Please try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClearCartResponseModel> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Network Error! Please try again later!", Toast.LENGTH_SHORT).show();
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

    private void getAddressListData(String uId, double total) {

        //dialogView.showCustomSpinProgress(this);

        manager.service.getAddressList(uId).enqueue(new Callback<AddressResponseModel>() {
            @Override
            public void onResponse(Call<AddressResponseModel> call, Response<AddressResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    AddressResponseModel addressResponseModel = response.body();
                    if (addressResponseModel.error != true) {
                        addressList = addressResponseModel.addresses;


                        DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                                addressList.get(0).lat,
                                addressList.get(0).lng,
                                restID,
                                prefs.getData(Constants.USER_ID)
                        );

                        getDelCharge(delChargeRequestModel, total);


                    } else {
                        // dialogView.dismissCustomSpinProgress();
                    }
                } else {
                    // dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddressResponseModel> call, Throwable t) {

                // dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getCartList(String userID) {
        dialogView.showCustomSpinProgress(CartActivity.this);
        manager.service.getCartList(userID).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    CartItemsResponseModel cirm = response.body();
                    if (cirm.error != true) {
                        cartList.clear();
                        cartList = cirm.carts;
                        upsellItemList = cirm.upsell_items;
                        taxRate = cirm.tax_rate;
                        offerId = cirm.offer_id;


                        int totQty = 0;

                        cartItemsResponseModel = new CartItemsResponseModel();
                        cartItemsResponseModel = cirm;



                        if (cartList.size() > 0) {

                            for (int y = 0; y < cartList.size(); y++) {
                                int qty = Integer.parseInt(cartList.get(y).quantity);

                                totQty = totQty + qty;
                            }

                            tvItems.setText("Total Items Price (" + totQty + ")");
                            double totalItemPrice = 0.0;
                            double deliveryCharge = 0.0;

                            double totalAmount = Double.parseDouble(cirm.cart_amount);
                            //if (Constants.restaurantModel.delivery_charge != null) {
                            //deliveryCharge = Double.parseDouble(Constants.restaurantModel.delivery_charge);
                            deliveryCharge = Double.parseDouble("0");
                            //}
                            double tax = Double.parseDouble(cirm.tax_amount);
                            totalItemPrice = totalAmount - (deliveryCharge + tax);

                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(Double.parseDouble(cirm.cart_amount));

                            tvTotItemPrice.setText("\u20B9 " + formatted);
                            /*String formatted1 = formatter1.format(Double.parseDouble(Constants.restaurantModel.delivery_charge));

                            if (!Constants.total_order_count.equals("0")) {
                                tvDelFee.setText("\u20B9 " + formatted1);
                            }else{
                                tvDelFee.setText("\u20B9 0.00" );
                            }*/
                            String formatted2 = formatter1.format(Double.parseDouble(cirm.tax_amount));
                            if (formatted2.equals(".00")) {
                                formatted2 = "0.00";
                            }
                            tvTax.setText("\u20B9 " + formatted2);

                            String formatted3 = "0";
                            if (!Constants.total_order_count.equals("0")) {
//                                formatted3 = formatter1.format((Double.parseDouble(cirm.cart_amount)+Double.parseDouble(Constants.restaurantModel.delivery_charge)+
//                                        Double.parseDouble(cirm.tax_amount) - Double.parseDouble(cirm.offer_discount)));
                                formatted3 = formatter1.format((Double.parseDouble(cirm.cart_amount) +
                                        Double.parseDouble(cirm.tax_amount) - Double.parseDouble(cirm.offer_discount)));
                            } else {
                                formatted3 = formatter1.format((Double.parseDouble(cirm.cart_amount) +
                                        Double.parseDouble(cirm.tax_amount) - Double.parseDouble(cirm.offer_discount)));
                            }

                            tvTotPrice.setText("\u20B9 " + formatted3);
                            total = (Double.parseDouble(cirm.cart_amount) +
                                    Double.parseDouble(cirm.tax_amount) - Double.parseDouble(cirm.offer_discount));

                            if (restID != null) {
                                if (restID.equals("")) {
                                    restID = cartList.get(0).restaurant_id;
                                    Log.d("RESTID", restID);
                                }
                            } else {
                                restID = cartList.get(0).restaurant_id;
                                Log.d("RESTID", restID);
                            }

                            if (cirm.offer_discount.equals("0")) {
                                li_discount.setVisibility(View.GONE);
                                tvDiscount.setText("₹ 0.00");
                                Constants.offer_discount = "0";
                            } else {
                                li_discount.setVisibility(View.VISIBLE);
                                Constants.offer_discount = cirm.offer_discount;
                                tv_discount_txt.setText("Saving ₹ " + cirm.offer_discount + " with this order");

                                DecimalFormat formatter10 = new DecimalFormat("#,##,###.00");
                                String formatted10 = formatter10.format(Double.parseDouble(cirm.offer_discount));

                                tvDiscount.setText("\u20B9 " + formatted10);
                            }

                            Log.d("OFFER_AT>>", cirm.min_discount_to_go);

                            double min_disc = Double.parseDouble(cirm.min_discount_to_go);
                            if (cirm.min_discount_to_go.equals("0") || min_disc < 0) {
                                li_get_discount.setVisibility(View.GONE);
                            } else {
                                li_get_discount.setVisibility(View.VISIBLE);
                                tv_get_discount_txt.setText("Add food worth of ₹ " + cirm.min_discount_to_go + " to get discount");
                            }


                            ciAdapter = new CartItemAdapter(CartActivity.this, cartList);
                            orderRv.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
                            orderRv.setAdapter(ciAdapter);
                            //ciAdapter.notifyDataSetChanged();

                        } else {

                            mainLay.setVisibility(View.GONE);
                            emptyLay.setVisibility(View.VISIBLE);
                            btnChkOut.setVisibility(View.GONE);
                            prefs.saveData(Constants.RestId_FromCart, "");

                        }

                        if (upsellItemList.size() > 0) {
                            completeHeaderTv.setVisibility(View.VISIBLE);
                            completeMealRv.setVisibility(View.VISIBLE);
                            completeMealAdapter = new CompleteMealAdapter(CartActivity.this, upsellItemList);
                            completeMealRv.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            completeMealRv.setAdapter(completeMealAdapter);
                        } else {

                            completeHeaderTv.setVisibility(View.GONE);
                            completeMealRv.setVisibility(View.GONE);

                        }

                        getAddressListData(prefs.getData(Constants.USER_ID), total);
                    } else {

                        dialogView.dismissCustomSpinProgress();

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();

                }
            }

            @Override
            public void onFailure(Call<CartItemsResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void getDelCharge(DelChargeRequestModel delChargeRequestModel, double total) {
        dialogView.showCustomSpinProgress(CartActivity.this);
        manager.service.getDeliveryCharge(Constants.couponCode, delChargeRequestModel).enqueue(new Callback<DelChargeResponseModel>() {
            @Override
            public void onResponse(Call<DelChargeResponseModel> call, Response<DelChargeResponseModel> response) {
                if(response.isSuccessful()){
                    DelChargeResponseModel dcrm = response.body();
                    if (!dcrm.error){

                        dialogView.dismissCustomSpinProgress();
                        double newDelCharge = Double.parseDouble(dcrm.delivery_charge);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted1 = formatter1.format(Double.parseDouble(dcrm.delivery_charge));
                        tvDelFee.setText("\u20B9 " + formatted1);

                        double finalTotal = total + Double.parseDouble(dcrm.delivery_charge);
                        String formatted2 = formatter1.format(finalTotal);
                        tvTotPrice.setText("\u20B9 " + formatted2);
                        Constants.delivery_charge = formatted1;

                        if (dcrm.is_surge.equals("1")){
                            li_sur_charge.setVisibility(View.VISIBLE);
                            tv_sur_charge.setText(dcrm.surge_purpose);
                        }else{
                            li_sur_charge.setVisibility(View.GONE);
                        }
                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DelChargeResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (Constants.isCartEmpty) {*/
        if (isForm.equals("Home")) {
            startActivity(new Intent(CartActivity.this, DashboardHome.class));
            isForm = "";
            //Constants.TAG = 0;
            //Constants.cartPrice = 0.0;
            finish();
        } else {
            startActivity(new Intent(CartActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, restID));
            Constants.TAG = 0;
            Constants.cartPrice = 0.0;
            finish();
        }

        //finishAffinity();
        // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.order_delivery_type.equals("1")) {
            //Constants.order_delivery_type = "1";
            delivery_type_li.setBackground(getResources().getDrawable(R.drawable.home_delivery));
            delivery_txt.setTextColor(getResources().getColor(R.color.white));
            takeaway_txt.setTextColor(getResources().getColor(R.color.black));
        } else {
            //Constants.order_delivery_type = "2";
            delivery_type_li.setBackground(getResources().getDrawable(R.drawable.takeaway));
            delivery_txt.setTextColor(getResources().getColor(R.color.black));
            takeaway_txt.setTextColor(getResources().getColor(R.color.white));
        }
    }
}