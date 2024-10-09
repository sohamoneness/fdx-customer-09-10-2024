package com.fdxUser.app.Activity.RestaurantScreens;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.OtherScreens.CouponActivity;
import com.fdxUser.app.Activity.OtherScreens.PaymentActivity;
import com.fdxUser.app.Activity.PolicyScreens.Support;
import com.fdxUser.app.Adapters.CheckoutOrderAdapter;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.AddressModels.AddressResponseModel;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeRequestModel;
import com.fdxUser.app.Models.DeliveryChargeModels.DelChargeResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    RecyclerView orderRv;
    public static List<CartsModel> cartItemList = new ArrayList<>();
    CheckoutOrderAdapter coAdapter;
    CheckBox cbCutlery;
    Button btnProceed;
    TextView tvTip1, tvTip2, tvTip3, tvTip4, tvAddCustomTip, tvTipRemove, tvAddItem, tvTip;
    LinearLayout otherTipLL, adrLL;
    TextView tvTipVal, tvTotItem, tvDelFee, tvTax, tvTotItemPrice, tvTotalPrice, tv_contact_help;
    public static CartItemsResponseModel cartItemsResponseModel;
    int totQty = 0;
    int delTip = 0;
    double totalAmount = 0.0, disAmount = 0.0;
    double tips = 0.0;
    double deliveryCharge = 0.0;
    double tax = 0.0;
    String restId = "", discountCode = "";
    TextView tvTag, tvAddress, tvGotoSelect, tvApplyPromo, tvDiscountAmount;
    LinearLayout li_discount, tipLL;
    RelativeLayout tipTxtRL;

    TextView tv_discount_txt, tvOfferRate;
    TextView tvScheduleDate, tvScheduleTime;

    RelativeLayout selectAddressRL, deliveryFeeRl, deliveryTipRl;
    List<AddressListModel> addressList = new ArrayList<>();

    EditText etCustomTip, etPromoCode, etNotes;
    Prefs prefs;
    ImageView ivBack;
    double totalItemPrice = 0.0;
    String userID = "";

    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;
    String delLat = "", delLon = "";
    double tot = 0.0;
    String couponCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(CheckOutActivity.this);
        dialogView = new DialogView();


        orderRv = findViewById(R.id.orderRv);
        btnProceed = findViewById(R.id.btnProceed);
        cbCutlery = findViewById(R.id.cbCutlery);
        tvTip1 = findViewById(R.id.tvTip1);
        tvTip2 = findViewById(R.id.tvTip2);
        tvTip3 = findViewById(R.id.tvTip3);
        tvTip4 = findViewById(R.id.tvTip4);
        otherTipLL = findViewById(R.id.otherTipLL);
        tvTipVal = findViewById(R.id.tvTipVal);
        tvTotItem = findViewById(R.id.tvTotItem);
        tvDelFee = findViewById(R.id.tvDelFee);
        tvTax = findViewById(R.id.tvTax);
        etPromoCode = findViewById(R.id.etPromoCode);
        etNotes = findViewById(R.id.etNotes);
        tvTotItemPrice = findViewById(R.id.tvTotItemPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        etCustomTip = findViewById(R.id.etCustomTip);
        tvAddCustomTip = findViewById(R.id.tvAddCustomTip);
        tvTipRemove = findViewById(R.id.tvTipRemove);
        tvAddItem = findViewById(R.id.tvAddItem);
        selectAddressRL = findViewById(R.id.selectAddressRL);
        tvTag = findViewById(R.id.tvTag);
        tvAddress = findViewById(R.id.tvAddress);
        adrLL = findViewById(R.id.adrLL);
        tvGotoSelect = findViewById(R.id.tvGotoSelect);
        ivBack = findViewById(R.id.iv_back);
        tvApplyPromo = findViewById(R.id.tvApplyPromo);
        tvDiscountAmount = findViewById(R.id.tvDiscountAmount);
        tv_contact_help = findViewById(R.id.tv_contact_help);
        tvTip = findViewById(R.id.tvTip);
        li_discount = findViewById(R.id.li_discount);
        tv_discount_txt = findViewById(R.id.tv_discount_txt);
        tipLL = findViewById(R.id.tipLL);
        tipTxtRL = findViewById(R.id.tipTxtRL);
        tvOfferRate = findViewById(R.id.tvOfferRate);
        tvScheduleDate = findViewById(R.id.tvScheduleDate);
        tvScheduleTime = findViewById(R.id.tvScheduleTime);

        deliveryFeeRl = findViewById(R.id.deliveryFeeRl);
        deliveryTipRl = findViewById(R.id.deliveryTipRl);

        restId = getIntent().getStringExtra(Constants.REST_ID);

        userID = prefs.getData(Constants.USER_ID);

        if (Constants.order_delivery_type.equals("1")) {
            deliveryFeeRl.setVisibility(View.VISIBLE);
            deliveryTipRl.setVisibility(View.VISIBLE);
        } else {
            deliveryFeeRl.setVisibility(View.GONE);
            deliveryTipRl.setVisibility(View.GONE);
        }

        /*if (tvGotoSelect.getVisibility() == View.VISIBLE){
            final float scale = getResources().getDisplayMetrics().density;
            int px = (int) (40 * scale + 0.5f);
            selectAddressRL.getLayoutParams().height = px;
        }else {
            final float scale = getResources().getDisplayMetrics().density;
            int px = (int) (100 * scale + 0.5f);
            selectAddressRL.getLayoutParams().height = px;
            //selectAddressRL.setMinimumHeight(100);
        }*/
        //Log.d("RESTID4", restId);

        //getCartItems();

        if (Constants.order_delivery_type.equals("1")) {
            getAddressListData(userID);
            selectAddressRL.setVisibility(View.VISIBLE);
            tipTxtRL.setVisibility(View.VISIBLE);
            tipLL.setVisibility(View.VISIBLE);
        } else {
            selectAddressRL.setVisibility(View.GONE);
            tipTxtRL.setVisibility(View.GONE);
            tipLL.setVisibility(View.GONE);
        }
        if (Constants.couponCode.equals("")) {
            tvApplyPromo.setText("Apply");
        }


        for (int y = 0; y < cartItemList.size(); y++) {
            int qty = Integer.parseInt(cartItemList.get(y).quantity);

            totQty = totQty + qty;
        }

        //****************************** Code by Soham for selected data**********************************//
        if (Constants.addressSelected == true) {
            adrLL.setVisibility(View.VISIBLE);
            tvGotoSelect.setVisibility(View.GONE);
            tvTag.setText("Delivery to " + Constants.addressListModel.tag);
            tvAddress.setText(Constants.addressListModel.address);

                    /*delLat = Constants.addressListModel.lat;
                    delLon = Constants.addressListModel.lng;*/

            DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                    Constants.addressListModel.lat,
                    Constants.addressListModel.lng,
                    restId,
                    prefs.getData(Constants.USER_ID)
            );

            Log.d("DEL_CHARGE>>", new Gson().toJson(delChargeRequestModel));

            tvDelFee.setText("\u20B9 " + Constants.delivery_charge);

            getDelCharge(delChargeRequestModel);

        }

        tips = Double.parseDouble(Constants.tip);
        DecimalFormat formatterx = new DecimalFormat("#,##,###.00");
        String formattedx = formatterx.format(Double.parseDouble(Constants.tip));
        if (formattedx.equals(".00")) {
            formattedx = "0.00";
        }
        //tvDiscountAmount.setText("- \u20B9 " + formatted);
        tvTip.setText("\u20B9 " + formattedx);
        etNotes.setText(Constants.notes);

//        if (Constants.isCouponSelected == 1){
//            li_discount.setVisibility(View.VISIBLE);
//            etPromoCode.setText(Constants.couponListModel.code);
//            discountCode = Constants.couponListModel.code;
//            tvApplyPromo.setText("Discard");
//            if (Constants.couponListModel.type.equals("1")){
//                float percent = Float.parseFloat(Constants.couponListModel.rate);
//                double discountPrice = (totalItemPrice * percent)/100;
//                double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate);
//                if(discountPrice >= maxDiscountPrice){
//                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
//                    String formatted = formatter1.format(maxDiscountPrice+ Double.parseDouble(Constants.offer_discount));
//                    tvDiscountAmount.setText("- \u20B9 " + formatted);
//                    disAmount = maxDiscountPrice+ Double.parseDouble(Constants.offer_discount);
//                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
//                    String formatted3 = formatter1.format(totalAmount);
//
//                    tvTotalPrice.setText("\u20B9 " + formatted3);
//                    String formatted4 = formatter1.format(totalAmount);
//                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
//                    Constants.isCouponSelected = 1;
//
//                    tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
//                }else {
//                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
//                    String formatted = formatter1.format(discountPrice+ Double.parseDouble(Constants.offer_discount));
//                    tvDiscountAmount.setText("- \u20B9 " + formatted);
//                    disAmount = discountPrice + Double.parseDouble(Constants.offer_discount);
//                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
//                    String formatted3 = formatter1.format(totalAmount);
//
//                    tvTotalPrice.setText("\u20B9 " + formatted3);
//                    String formatted4 = formatter1.format(totalAmount);
//                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
//                    Constants.isCouponSelected = 1;
//
//                    tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
//                }
//            }else {
//                double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate)
//                        + Double.parseDouble(Constants.offer_discount);
//                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
//                String formatted = formatter1.format(maxDiscountPrice);
//                tvDiscountAmount.setText("- \u20B9 " + formatted);
//                disAmount = maxDiscountPrice ;
//                Log.d("item total>>",String.valueOf(totalItemPrice));
//                Log.d("discount>>",String.valueOf(deliveryCharge));
//                Log.d("total>>",String.valueOf(totalAmount));
//                Log.d("rest_discount>>",String.valueOf(disAmount));
//                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
//                String formatted3 = formatter1.format(totalAmount);
//
//                tvTotalPrice.setText("\u20B9 " + formatted3);
//                String formatted4 = formatter1.format(totalAmount);
//                btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
//                Constants.isCouponSelected = 1;
//
//                tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
//            }
//
//        }else{
//            li_discount.setVisibility(View.GONE);
//
//            if (Double.parseDouble(Constants.offer_discount)>0){
//                double maxDiscountPrice = Double.parseDouble(Constants.offer_discount);
//                disAmount = maxDiscountPrice;
//                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
//                String formatted = formatter1.format(maxDiscountPrice);
//                tvDiscountAmount.setText("- \u20B9 " + formatted);
//
//                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - maxDiscountPrice;
//                String formatted3 = formatter1.format(totalAmount);
//
//                Log.d("discounted_price>>",formatted3);
//
//                tvTotalPrice.setText("\u20B9 " + formatted3);
//            }else{
//                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
//                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - 0;
//                String formatted3 = formatter1.format(totalAmount);
//
//                Log.d("discounted_price>>",formatted3);
//
//                tvTotalPrice.setText("\u20B9 " + formatted3);
//            }
//        }

        //******************************************End Code***************************************//

        /*Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {

                if (Constants.addressSelected == true) {
                    adrLL.setVisibility(View.VISIBLE);
                    tvGotoSelect.setVisibility(View.GONE);
                    tvTag.setText("Delivery to " + Constants.addressListModel.tag);
                    tvAddress.setText(Constants.addressListModel.location);

                    DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                            Constants.addressListModel.lat,
                            Constants.addressListModel.lng,
                            restId
                    );

                    getDelCharge(delChargeRequestModel);

                }
                if (Constants.isCouponSelected == 1){
                    etPromoCode.setText(Constants.couponListModel.code);
                    discountCode = Constants.couponListModel.code;
                    tvApplyPromo.setText("Discard");
                    if (Constants.couponListModel.type.equals("1")){
                        float percent = Float.parseFloat(Constants.couponListModel.rate);
                        double discountPrice = (totalItemPrice * percent)/100;
                        double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate);
                        if(discountPrice >= maxDiscountPrice){
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(maxDiscountPrice);
                            tvDiscountAmount.setText("- \u20B9 " + formatted);
                            disAmount = maxDiscountPrice;
                            totalAmount = (totalItemPrice + deliveryCharge + tax+tips) - disAmount;
                            String formatted3 = formatter1.format(totalAmount);

                            tvTotalPrice.setText("\u20B9 " + formatted3);
                            String formatted4 = formatter1.format(totalAmount);
                            btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                            Constants.isCouponSelected = 0;
                        }else {
                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted = formatter1.format(discountPrice);
                            tvDiscountAmount.setText("- \u20B9 " + formatted);
                            disAmount = discountPrice;
                            totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                            String formatted3 = formatter1.format(totalAmount);

                            tvTotalPrice.setText("\u20B9 " + formatted3);
                            String formatted4 = formatter1.format(totalAmount);
                            btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                            Constants.isCouponSelected = 0;
                        }
                    }else {
                        double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(maxDiscountPrice);
                        tvDiscountAmount.setText("- \u20B9 " + formatted);
                        disAmount = maxDiscountPrice;
                        Log.d("item total>>",String.valueOf(totalItemPrice));
                        Log.d("discount>>",String.valueOf(deliveryCharge));
                        Log.d("total>>",String.valueOf(totalAmount));
                        totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                        String formatted3 = formatter1.format(totalAmount);

                        tvTotalPrice.setText("\u20B9 " + formatted3);
                        String formatted4 = formatter1.format(totalAmount);
                        btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                        Constants.isCouponSelected = 0;
                    }

                }


                handler.postDelayed(this, 100);
            }
        };
        runnable.run();*/


        tvTotItem.setText("Total Items Price (" + totQty + ")");

        tvApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvApplyPromo.getText().toString().equals("Discard") || tvApplyPromo.getText().toString().equals("DISCARD")) {
                    tvApplyPromo.setText("Apply");
                    etPromoCode.setText("");
                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                    totalAmount = totalAmount + disAmount;
                    Constants.couponCode = "";
                    String formatted3 = formatter1.format(totalAmount);
                    tvTotalPrice.setText("\u20B9 " + formatted3);
                    String formatted4 = formatter1.format(totalAmount);
                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                    disAmount = 0.0;
                    tvDiscountAmount.setText("- \u20B9 " + "0.00");
                    discountCode = "";
                    li_discount.setVisibility(View.GONE);
                    Constants.isCouponSelected = 0;
                } else {
                    Constants.cartItemsResponseModel = cartItemsResponseModel;
                    CouponActivity.cartItemList = cartItemList;
                    startActivity(new Intent(CheckOutActivity.this, CouponActivity.class)
                            .putExtra(Constants.REST_ID, restId));
                    finish();
                }

            }
        });

        tv_contact_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckOutActivity.this, Support.class));
            }
        });


        totalAmount = Double.parseDouble(cartItemsResponseModel.total_amount);
        //deliveryCharge = Double.parseDouble(cartItemsResponseModel.delivery_charge);
        tax = Double.parseDouble(cartItemsResponseModel.tax_amount);
        totalItemPrice = Double.parseDouble(cartItemsResponseModel.cart_amount);

        if (cartItemsResponseModel.discounted_amount.equals("0.0") ||
                cartItemsResponseModel.discounted_amount.equals("0")) {
            tvDiscountAmount.setText("- \u20B9 " + "0.00");
        }

        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(totalItemPrice);

        tvTotItemPrice.setText("\u20B9 " + formatted);

        String formatted1 = "0";
        if (!Constants.total_order_count.equals("0")) {
            formatted1 = formatter1.format(Double.parseDouble(cartItemsResponseModel.delivery_charge));
        } else {
            formatted1 = "0.00";
        }

        tvDelFee.setText("\u20B9 " + formatted1);
        String formatted2 = formatter1.format(Double.parseDouble(cartItemsResponseModel.tax_amount));
        if (formatted2.equals(".00")) {
            formatted2 = "0.00";
        }
        tvTax.setText("\u20B9 " + formatted2);
        String formatted3 = formatter1.format(Double.parseDouble(cartItemsResponseModel.total_amount));


        //tvTotalPrice.setText("\u20B9 " + formatted3);
        String formatted4 = formatter1.format(totalAmount);
        btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);


        coAdapter = new CheckoutOrderAdapter(CheckOutActivity.this, cartItemList);
        orderRv.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, LinearLayoutManager.VERTICAL, false));
        orderRv.setAdapter(coAdapter);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.order_delivery_type.equals("1")) {
                    if (Constants.addressSelected == true || Constants.isDefault == true) {
                        String totItemAmount = String.valueOf(totalItemPrice);

                        totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                        //String totAmount = String.valueOf(tot);
                        String totAmount = String.valueOf(totalAmount);
                        String notes = etNotes.getText().toString();
                        Constants.notes = notes;

                        Constants.schedule_date = tvScheduleDate.getText().toString();
                        Constants.schedule_time = tvScheduleTime.getText().toString();

                        startActivity(new Intent(CheckOutActivity.this, PaymentActivity.class)
                                .putExtra(Constants.TOTAL_PRICE, totAmount)
                                .putExtra(Constants.REST_ID, restId)
                                .putExtra(Constants.TOTAL_ITEM_PRICE, totItemAmount)
                                .putExtra(Constants.DISCOUNT, String.valueOf(disAmount))
                                .putExtra(Constants.DISCOUNT_CODE, discountCode)
                        );
                        //Constants.isCouponSelected = 0;
                        finish();
                    } else {
                        //Toast.makeText(CheckOutActivity.this, "Please select delivery address to proceed!", Toast.LENGTH_SHORT).show();
                        dialogView.errorButtonDialog(CheckOutActivity.this, getResources().getString(R.string.app_name), "Please select your delivery address. If you haven't add any delivery address yet, please add one to continue");
                    }
                } else {
                    deliveryCharge = 0;
                    String totItemAmount = String.valueOf(totalItemPrice);

                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                    //String totAmount = String.valueOf(tot);
                    String totAmount = String.valueOf(totalAmount);
                    String notes = etNotes.getText().toString();
                    Constants.notes = notes;

                    startActivity(new Intent(CheckOutActivity.this, PaymentActivity.class)
                            .putExtra(Constants.TOTAL_PRICE, totAmount)
                            .putExtra(Constants.REST_ID, restId)
                            .putExtra(Constants.TOTAL_ITEM_PRICE, totItemAmount)
                            .putExtra(Constants.DISCOUNT, String.valueOf(disAmount))
                            .putExtra(Constants.DISCOUNT_CODE, discountCode)
                    );
                }

            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CheckOutActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, prefs.getData(Constants.REST_ID)));
                finish();
            }
        });

        selectAddressRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isFor = "Select";
                Constants.cartItemsResponseModel = cartItemsResponseModel;
                AddressList.cartItemList = cartItemList;
                startActivity(new Intent(CheckOutActivity.this, AddressList.class).putExtra(Constants.REST_ID, restId));
                finish();
            }
        });


        tvTip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip1.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));

                tvTipRemove.setVisibility(View.VISIBLE);

                //delTip = 20;
                //totalAmount = (int)(totalAmount) + delTip;
                tips = 20.0;
                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;

                Constants.tip = String.valueOf((int) tips);
                tvTip.setText("\u20B9 " + Constants.tip + ".00");
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(totalAmount);

                tvTotalPrice.setText("\u20B9 " + formatted);
                btnProceed.setText("Proceed to Pay \u20B9 " + formatted);


            }
        });

        tvTip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip2.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));
                tips = 30.0;
                Constants.tip = String.valueOf((int) tips);
                tvTip.setText("\u20B9 " + Constants.tip + ".00");
                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;

                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(totalAmount);

                tvTotalPrice.setText("\u20B9 " + formatted);
                btnProceed.setText("Proceed to Pay \u20B9 " + formatted);

                tvTipRemove.setVisibility(View.VISIBLE);
            }
        });

        tvTip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTipVal.setText(tvTip3.getText().toString());
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
                tvTipVal.setTextColor(getResources().getColor(R.color.black));
                tips = 50.0;
                Constants.tip = String.valueOf((int) tips);
                tvTip.setText("\u20B9 " + Constants.tip + ".00");
                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;

                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(totalAmount);

                tvTotalPrice.setText("\u20B9 " + formatted);
                btnProceed.setText("Proceed to Pay \u20B9 " + formatted);
            }
        });

        tvTip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherTipLL.setVisibility(View.VISIBLE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.orange_border_et_bg));
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.colorAccent));


            }
        });

        tvAddCustomTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST2>>", etCustomTip.getText().toString());
                //String custTip = etCustomTip.getText().toString();
                if (!etCustomTip.getText().toString().equals("")) {

                    tvTipVal.setText("\u20B9 " + etCustomTip.getText().toString());
                    tvTipVal.setTextColor(getResources().getColor(R.color.black));
                    otherTipLL.setVisibility(View.GONE);
                    tvTipRemove.setVisibility(View.VISIBLE);

                    // delTip = Integer.parseInt(etCustomTip.getText().toString());

                    tips = Double.parseDouble(etCustomTip.getText().toString());
                    Constants.tip = String.valueOf((int) tips);
                    tvTip.setText("\u20B9 " + Constants.tip + ".00");
                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;

                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                    String formatted = formatter1.format(totalAmount);

                    tvTotalPrice.setText("\u20B9 " + formatted);
                    btnProceed.setText("Proceed to Pay \u20B9 " + formatted);
                } else {
                    tvTipRemove.setVisibility(View.GONE);
                }

            }
        });

        tvTipRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTipRemove.setVisibility(View.GONE);
                tvTipVal.setText("Add tip");
                tvTipVal.setTextColor(getResources().getColor(R.color.colorAccent));
                totalAmount = totalAmount - delTip;
                tvTotalPrice.setText("\u20B9 " + String.valueOf(totalAmount));
                btnProceed.setText("Proceed to Pay \u20B9 " + String.valueOf(totalAmount));

                otherTipLL.setVisibility(View.GONE);
                tvTip1.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip2.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip3.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip4.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvTip1.setTextColor(getResources().getColor(R.color.black));
                tvTip2.setTextColor(getResources().getColor(R.color.black));
                tvTip3.setTextColor(getResources().getColor(R.color.black));
                tvTip4.setTextColor(getResources().getColor(R.color.black));
            }
        });

        tvScheduleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        CheckOutActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                tvScheduleDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

        tvScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CheckOutActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                tvScheduleTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });
    }

    private void getDelCharge(DelChargeRequestModel delChargeRequestModel) {
        //dialogView.showCustomSpinProgress(CheckOutActivity.this);
        manager.service.getDeliveryCharge(Constants.couponCode, delChargeRequestModel).enqueue(new Callback<DelChargeResponseModel>() {
            @Override
            public void onResponse(Call<DelChargeResponseModel> call, Response<DelChargeResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    DelChargeResponseModel dcrm = response.body();
                    if (!dcrm.error) {
                        Log.d("delivery_resp>>",dcrm.toString());
                        double prevDeliveryCharge = Double.parseDouble(cartItemsResponseModel.delivery_charge);
                        double newDelCharge = Double.parseDouble(dcrm.delivery_charge);
                        tot = Double.parseDouble(cartItemsResponseModel.total_amount);
                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        deliveryCharge = newDelCharge;

                        if (Constants.order_delivery_type.equals("1")) {
                           /* if (Constants.total_order_count.equals("0")) {
                                deliveryCharge = 0.00;
                                Constants.delivery_charge = "0";
                            } else {*/
                                if (newDelCharge > prevDeliveryCharge) {
                                    double extraCharge = newDelCharge - prevDeliveryCharge;
                                    //double discount = Double.parseDouble(Constants.offer_discount);
                                    double discount = disAmount;
                                    tot = (tot + extraCharge) - discount + tips;
                                    String formatted1 = formatter1.format(Double.parseDouble(dcrm.delivery_charge));

                                    Constants.delivery_charge = formatted1;
                                    tvDelFee.setText("\u20B9 " + formatted1);
                                    String formatted3 = formatter1.format(tot);

                                    String formatted4 = formatter1.format(tot);
                                    tvTotalPrice.setText("\u20B9 " + formatted4);
                                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);

                                    if (dcrm.delivery_offer_rate != null) {
                                        if (!dcrm.delivery_offer_rate.equals("0") || !dcrm.delivery_offer_rate.equals("0.0")) {
                                            tvOfferRate.setVisibility(View.VISIBLE);
                                            String formatted10 = formatter1.format(Double.parseDouble(dcrm.delivery_offer_rate));
                                            tvOfferRate.setText("( You saved " + "\u20B9" + formatted10 + " on delivery charge )");
                                        } else {
                                            tvOfferRate.setVisibility(View.GONE);
                                        }
                                    } else {
                                        tvOfferRate.setVisibility(View.GONE);
                                    }


                                } else if (newDelCharge < prevDeliveryCharge) {
                                    double extraCharge = newDelCharge - prevDeliveryCharge;
                                    double discount = Double.parseDouble(Constants.offer_discount);
                                    tot = (tot - extraCharge) - discount + tips;
                                    String formatted1 = formatter1.format(Double.parseDouble(dcrm.delivery_charge));

                                    Constants.delivery_charge = formatted1;
                                    tvDelFee.setText("\u20B9 " + formatted1);
                                    String formatted3 = formatter1.format(tot);

                                    // tvTotalPrice.setText("\u20B9 " + formatted3);
                                    String formatted4 = formatter1.format(tot);
                                    tvTotalPrice.setText("\u20B9 " + formatted4);
                                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                                }
                           // }
                        } else {
                            Constants.addressSelected = false;
                            double extraCharge = 0.00;
                            deliveryCharge = 0;
                            //double discount = Double.parseDouble(Constants.offer_discount);
                            double discount = disAmount;
                            tot = (tot + extraCharge) - discount + tips;
                            String formatted1 = formatter1.format(Double.parseDouble("0.00"));

                            Constants.delivery_charge = formatted1;
                            tvDelFee.setText("\u20B9 " + formatted1);
                            String formatted3 = formatter1.format(tot);

                            String formatted4 = formatter1.format(tot);
                            tvTotalPrice.setText("\u20B9 " + formatted4);
                            btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                        }


                    } else {
                        //dialogView.dismissCustomSpinProgress();
                    }
                } else {
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DelChargeResponseModel> call, Throwable t) {
                //dialogView.dismissCustomSpinProgress();
            }
        });
    }


    private void getAddressListData(String uId) {

        //dialogView.showCustomSpinProgress(this);

        manager.service.getAddressList(uId).enqueue(new Callback<AddressResponseModel>() {
            @Override
            public void onResponse(Call<AddressResponseModel> call, Response<AddressResponseModel> response) {
                if (response.isSuccessful()) {
                    //dialogView.dismissCustomSpinProgress();
                    AddressResponseModel addressResponseModel = response.body();
                    if (addressResponseModel.error != true) {
                        addressList = addressResponseModel.addresses;

                        if (addressList.size() > 0) {
                            if (!Constants.addressSelected) {
                                Constants.isDefault = true;
                                //Constants.addressSelected = false;
                                Constants.addressListModel = addressList.get(0);
                                adrLL.setVisibility(View.VISIBLE);
                                tvGotoSelect.setVisibility(View.GONE);
                                tvTag.setText("Delivery to " + addressList.get(0).tag);
                                tvAddress.setText(addressList.get(0).address);
                                /*final float scale = getResources().getDisplayMetrics().density;
                                int px = (int) (100 * scale + 0.5f);
                                selectAddressRL.getLayoutParams().height = px;*/
                                DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                                        Constants.addressListModel.lat,
                                        Constants.addressListModel.lng,
                                        restId,
                                        prefs.getData(Constants.USER_ID)
                                );

                                getDelCharge(delChargeRequestModel);
                            }
                        }

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
        /*startActivity(new Intent(CheckOutActivity.this, CartActivity.class));
        finish();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume>>", "we are in resume");

        tips = Double.parseDouble(Constants.tip);
        DecimalFormat formatterx = new DecimalFormat("#,##,###.00");
        String formattedx = formatterx.format(Double.parseDouble(Constants.tip));
        if (formattedx.equals(".00")) {
            formattedx = "0.00";
        }
        //tvDiscountAmount.setText("- \u20B9 " + formatted);
        tvTip.setText("\u20B9 " + formattedx);

        if (Constants.couponCode.equals("")) {
            tvApplyPromo.setText("Apply");
        }

        if (Constants.addressSelected == true) {
            adrLL.setVisibility(View.VISIBLE);
            tvGotoSelect.setVisibility(View.GONE);
            tvTag.setText("Delivery to " + Constants.addressListModel.tag);
            tvAddress.setText(Constants.addressListModel.address);

                    /*delLat = Constants.addressListModel.lat;
                    delLon = Constants.addressListModel.lng;*/

            DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                    Constants.addressListModel.lat,
                    Constants.addressListModel.lng,
                    restId,
                    prefs.getData(Constants.USER_ID)
            );

            //getDelCharge(delChargeRequestModel);

        }

        if (!Constants.delivery_charge.equals("0")) {
            tvDelFee.setText("\u20B9 " + Constants.delivery_charge);
            deliveryCharge = Double.parseDouble(Constants.delivery_charge);
        }
        tips = Double.parseDouble(Constants.tip);

        if (Constants.isCouponSelected == 1) {
            //CToast.show(CheckOutActivity.this,Constants.couponCode);
            //CToast.show(CheckOutActivity.this,Constants.couponAmount);
            li_discount.setVisibility(View.VISIBLE);
            etPromoCode.setText(Constants.couponCode);
            discountCode = Constants.couponCode;
            tvApplyPromo.setText("Discard");
            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
            String formatted = formatter1.format(Double.parseDouble(Constants.couponAmount));
            tvDiscountAmount.setText("- \u20B9 " + formatted);
            tv_discount_txt.setText("Saving ₹ " + formatted + " with this order");

            disAmount = Double.parseDouble(Constants.couponAmount);

            totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
            String formatted3 = formatter1.format(totalAmount);

            tvTotalPrice.setText("\u20B9 " + formatted3);
            String formatted4 = formatter1.format(totalAmount);
            btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
            DelChargeRequestModel delChargeRequestModel = new DelChargeRequestModel(
                    Constants.addressListModel.lat,
                    Constants.addressListModel.lng,
                    restId,
                    prefs.getData(Constants.USER_ID)
            );

            Log.d("DEL_CHARGE>>", new Gson().toJson(delChargeRequestModel));

            getDelCharge(delChargeRequestModel);
            /*if (Constants.couponListModel.type.equals("1")){
                float percent = Float.parseFloat(Constants.couponListModel.rate);
                double discountPrice = (totalItemPrice * percent)/100;
                double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate);
                if(discountPrice >= maxDiscountPrice){
                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                    //String formatted = formatter1.format(maxDiscountPrice+Double.parseDouble(Constants.offer_discount));
                    String formatted = formatter1.format(Double.parseDouble(Constants.couponAmount));
                    tvDiscountAmount.setText("- \u20B9 " + formatted);
                    //disAmount = maxDiscountPrice + Double.parseDouble(Constants.offer_discount);
                    disAmount = Double.parseDouble(Constants.couponAmount);
                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                    String formatted3 = formatter1.format(totalAmount);

                    tvTotalPrice.setText("\u20B9 " + formatted3);
                    String formatted4 = formatter1.format(totalAmount);
                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                    Constants.isCouponSelected = 1;
                    tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
                }else {
                    DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                    //String formatted = formatter1.format(discountPrice+ Double.parseDouble(Constants.offer_discount));
                    String formatted = formatter1.format(Double.parseDouble(Constants.couponAmount));
                    tvDiscountAmount.setText("- \u20B9 " + formatted);
                    //disAmount = discountPrice + Double.parseDouble(Constants.offer_discount);
                    disAmount = Double.parseDouble(Constants.couponAmount);
                    totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                    String formatted3 = formatter1.format(totalAmount);

                    tvTotalPrice.setText("\u20B9 " + formatted3);
                    String formatted4 = formatter1.format(totalAmount);
                    btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                    Constants.isCouponSelected = 1;

                    tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
                }
            }else {
                //double maxDiscountPrice = Double.parseDouble(Constants.couponListModel.maximum_offer_rate) + Double.parseDouble(Constants.offer_discount);
                double maxDiscountPrice = Double.parseDouble(Constants.couponAmount);
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(maxDiscountPrice);
                tvDiscountAmount.setText("- \u20B9 " + formatted);
                disAmount = maxDiscountPrice ;
                Log.d("item total>>",String.valueOf(totalItemPrice));
                Log.d("discount>>",String.valueOf(deliveryCharge));
                Log.d("total>>",String.valueOf(totalAmount));
                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - disAmount;
                String formatted3 = formatter1.format(totalAmount);

                tvTotalPrice.setText("\u20B9 " + formatted3);
                String formatted4 = formatter1.format(totalAmount);
                btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted4);
                Constants.isCouponSelected = 1;

                tv_discount_txt.setText("Saving ₹ "+formatted+" with this order");
            }*/

        } else {

            li_discount.setVisibility(View.GONE);
            if (Double.parseDouble(Constants.offer_discount) > 0) {
                double maxDiscountPrice = Double.parseDouble(Constants.offer_discount);
                //double maxDiscountPrice = Double.parseDouble(Constants.couponAmount);
                disAmount = maxDiscountPrice;
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                String formatted = formatter1.format(maxDiscountPrice);
                tvDiscountAmount.setText("- \u20B9 " + formatted);

                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - maxDiscountPrice;
                String formatted3 = formatter1.format(totalAmount);

                Log.d("discounted_price>>", formatted3);
                tvTotalPrice.setText("\u20B9 " + formatted3);
                btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted3);
            } else {
                DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                totalAmount = (totalItemPrice + deliveryCharge + tax + tips) - 0;
                String formatted3 = formatter1.format(totalAmount);

                Log.d("discounted_price>>", formatted3);

                tvTotalPrice.setText("\u20B9 " + formatted3);
                btnProceed.setText("Proceed to Pay  " + "\u20B9 " + formatted3);
            }
        }
    }
}