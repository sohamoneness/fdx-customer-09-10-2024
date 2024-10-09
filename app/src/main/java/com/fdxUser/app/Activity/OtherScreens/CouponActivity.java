package com.fdxUser.app.Activity.OtherScreens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.CheckOutActivity;
import com.fdxUser.app.Adapters.CouponListAdapter;
import com.fdxUser.app.Adapters.DelCouponListAdapter;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CouponModels.CheckCouponRequestModel;
import com.fdxUser.app.Models.CouponModels.CheckCouponResponseModel;
import com.fdxUser.app.Models.CouponModels.CouponListModel;
import com.fdxUser.app.Models.CouponModels.CouponResponseModel;
import com.fdxUser.app.Models.CouponModels.DeliveryCouponsListModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {

    RecyclerView couponRv, delCouponRv;
    ImageView ivBack;
    String restID = "";
    List<CouponListModel> couponList = new ArrayList<>();
    List<DeliveryCouponsListModel> delCouponList = new ArrayList<>();
    DialogView dialogView;
    Prefs prefs;
   // ApiManagerWithAuth manager = new ApiManagerWithAuth();
    ApiManager manager = new ApiManager();
    ApiManager manager1 = new ApiManager();
    EditText etPromoCode;
    TextView tvApplyPromo;
    public static List<CartsModel> cartItemList = new ArrayList<>();

    CouponListAdapter couponListAdapter;
    DelCouponListAdapter delCouponListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(CouponActivity.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        couponRv = findViewById(R.id.couponRv);
        delCouponRv = findViewById(R.id.delCouponRv);

        restID = getIntent().getStringExtra(Constants.REST_ID);
        Constants.isCouponSelected = 0;

        etPromoCode= findViewById(R.id.etPromoCode);
        tvApplyPromo = findViewById(R.id.tvApplyPromo);

        getCouponList();

        tvApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPromoCode.getText().toString().equals("")){
                    CheckCouponRequestModel checkCouponRequestModel = new CheckCouponRequestModel(
                            prefs.getData(Constants.USER_ID),
                            etPromoCode.getText().toString()
                    );

                    checkCoupon(checkCouponRequestModel);
                }else{
                    dialogView.errorButtonDialog(CouponActivity.this, getResources().getString(R.string.app_name), "Please enter valid coupon code");
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        couponRv.addOnItemTouchListener(new RecyclerItemClickListener(CouponActivity.this, couponRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                int left = Integer.parseInt(couponList.get(position).maximum_time_user_can_use) - Integer.parseInt(couponList.get(position).user_used);
                if (left<=0){
                    dialogView.errorButtonDialog(CouponActivity.this, getResources().getString(R.string.app_name), "This coupon has been used for maximum no of time!");
                }else{
                    //Constants.isCouponSelected = 1;
                    Log.d("CODE>>", couponList.get(position).code);
                    CheckCouponRequestModel checkCouponRequestModel = new CheckCouponRequestModel(
                            prefs.getData(Constants.USER_ID),
                            couponList.get(position).code
                    );

                    checkCoupon(checkCouponRequestModel);
                    //Constants.couponListModel = couponList.get(position);
                    //finish();
                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        delCouponRv.addOnItemTouchListener(new RecyclerItemClickListener(CouponActivity.this, delCouponRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                int left = Integer.parseInt(delCouponList.get(position).maximum_time_user_can_use) - Integer.parseInt(delCouponList.get(position).user_used);
                if (left<=0){
                    dialogView.errorButtonDialog(CouponActivity.this, getResources().getString(R.string.app_name), "This coupon has been used for maximum no of time!");
                }else{
                    //Constants.isCouponSelected = 1;
                    //Log.d("CODE>>", couponList.get(position).code);
                    Constants.couponCode = delCouponList.get(position).code;
                    Constants.isCouponSelected = 1;
                    //Constants.couponAmount = delCouponList.get(position).offer_discount;
                    startActivity(new Intent(CouponActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
                    finish();
                    /*CheckCouponRequestModel checkCouponRequestModel = new CheckCouponRequestModel(
                            prefs.getData(Constants.USER_ID),
                            delCouponList.get(position).code
                    );

                    checkCoupon(checkCouponRequestModel);*/
                    //Constants.couponListModel = couponList.get(position);
                    //finish();
                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    private void getCouponList() {
        dialogView.showCustomSpinProgress(CouponActivity.this);
        manager.service.getAllCoupons(restID,prefs.getData(Constants.USER_ID)).enqueue(new Callback<CouponResponseModel>() {
            @Override
            public void onResponse(Call<CouponResponseModel> call, Response<CouponResponseModel> response) {
                if (response.isSuccessful()){
                    CouponResponseModel crm = response.body();
                    if (!crm.error){
                        dialogView.dismissCustomSpinProgress();
                        couponList = crm.coupons;
                        delCouponList = crm.delivery_coupons;
                        if (couponList.size() > 0){
                            couponListAdapter = new CouponListAdapter(CouponActivity.this, couponList);
                            couponRv.setLayoutManager(new LinearLayoutManager(CouponActivity.this,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                            couponRv.setAdapter(couponListAdapter);
                        }

                        if (delCouponList.size()>0){
                            delCouponListAdapter = new DelCouponListAdapter(CouponActivity.this, delCouponList);
                            delCouponRv.setLayoutManager(new LinearLayoutManager(CouponActivity.this,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                            delCouponRv.setAdapter(delCouponListAdapter);
                        }

                    }else{
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CouponResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void checkCoupon(CheckCouponRequestModel checkCouponRequestModel) {
        dialogView.showCustomSpinProgress(CouponActivity.this);
        manager.service.checkActiveCoupon(checkCouponRequestModel).enqueue(new Callback<CheckCouponResponseModel>() {
            @Override
            public void onResponse(Call<CheckCouponResponseModel> call, Response<CheckCouponResponseModel> response) {
                if (response.isSuccessful()){
                    CheckCouponResponseModel cgrm = response.body();
                    //Log.d("RESP>>", String.valueOf(cgrm.error));
                    if (!cgrm.error){
                        dialogView.dismissCustomSpinProgress();
                        //Toast.makeText(getActivity(), cgrm.message, Toast.LENGTH_SHORT).show();
                        Constants.isCouponSelected = 1;
                        Constants.couponAmount = cgrm.offer_discount;
                        Constants.couponCode = cgrm.code;
                        Log.d("RESP2>>", Constants.couponCode);

                       // Constants.couponListModel = cgrm.coupon;
                        startActivity(new Intent(CouponActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
                        finish();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                        Constants.couponAmount = "0";
                        Constants.couponCode = "";
                        dialogView.errorButtonDialog(CouponActivity.this, getResources().getString(R.string.app_name), cgrm.message);
                        //Toast.makeText(CouponActivity.this, cgrm.message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CheckCouponResponseModel> call, Throwable t) {
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
        CheckOutActivity.cartItemList = cartItemList;
        CheckOutActivity.cartItemsResponseModel = Constants.cartItemsResponseModel;
        //startActivity(new Intent(CartActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
        startActivity(new Intent(CouponActivity.this, CheckOutActivity.class).putExtra(Constants.REST_ID, restID));
        finish();
    }
}