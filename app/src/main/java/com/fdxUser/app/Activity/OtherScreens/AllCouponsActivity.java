package com.fdxUser.app.Activity.OtherScreens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Adapters.AllCouponListAdapter;
import com.fdxUser.app.Models.CouponModels.CouponListModel;
import com.fdxUser.app.Models.CouponModels.CouponResponseModel;
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

public class AllCouponsActivity extends AppCompatActivity {

    RecyclerView couponRv;
    ImageView ivBack;
    String restID = "";
    List<CouponListModel> couponList = new ArrayList<>();
    DialogView dialogView;
    Prefs prefs;
    ApiManager manager = new ApiManager();

    AllCouponListAdapter couponListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_coupons);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(AllCouponsActivity.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        couponRv = findViewById(R.id.couponRv);

        restID = prefs.getData(Constants.REST_ID);
        Constants.isCouponSelected = 0;

        getCouponList();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        couponRv.addOnItemTouchListener(new RecyclerItemClickListener(AllCouponsActivity.this, couponRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void getCouponList() {
        dialogView.showCustomSpinProgress(AllCouponsActivity.this);
        manager.service.getAllCoupons(restID,prefs.getData(Constants.USER_ID)).enqueue(new Callback<CouponResponseModel>() {
            @Override
            public void onResponse(Call<CouponResponseModel> call, Response<CouponResponseModel> response) {
                if (response.isSuccessful()){
                    CouponResponseModel crm = response.body();
                    if (!crm.error){
                        dialogView.dismissCustomSpinProgress();
                        couponList = crm.coupons;
                        if (couponList.size() > 0){
                            couponListAdapter = new AllCouponListAdapter(AllCouponsActivity.this, couponList);
                            couponRv.setLayoutManager(new LinearLayoutManager(AllCouponsActivity.this,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                            couponRv.setAdapter(couponListAdapter);
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
        startActivity(new Intent(AllCouponsActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, restID));
        Constants.TAG = 0;
        Constants.cartPrice = 0.0;
        finish();
    }
}