package com.fdxUser.app.Activity.RestaurantScreens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.MenuItemAdapter;
import com.fdxUser.app.Models.CartModels.CartItemsResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemsModel;
import com.fdxUser.app.Models.RestaurantModels.SearchItemResponseModel;
import com.fdxUser.app.Models.RestaurantModels.SearchItemsRequestModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchItemActivity extends AppCompatActivity implements TextWatcher {

    EditText et_search;
    RecyclerView item_rv;
    RelativeLayout viewCartRL;
    TextView tvCartQty, tvCartPrice;
    ImageView iv_back;

    DialogView dialogView;
    Prefs prefs;
    String tag = "";
    ApiManager manager = new ApiManager();
    ApiManagerWithAuth manager1 = new ApiManagerWithAuth();

    List<ItemsModel> itemsList = new ArrayList<>();
    List<CustomCartModel> ccmList = new ArrayList<>();
    List<CartsModel> cartList = new ArrayList<>();
    MenuItemAdapter menuItemAdapter;

    Timer timer;
    public static String restID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(SearchItemActivity.this);
        dialogView = new DialogView();

        et_search = findViewById(R.id.et_search);

        item_rv = findViewById(R.id.item_rv);

        viewCartRL = findViewById(R.id.viewCartRL);
        iv_back = findViewById(R.id.iv_back);

        tvCartPrice = findViewById(R.id.tvCartPrice);
        tvCartQty = findViewById(R.id.tvCartQty);

        et_search.addTextChangedListener(this);

        //getCartList(prefs.getData(Constants.USER_ID));

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

        viewCartRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchItemActivity.this, CartActivity.class).putExtra(Constants.REST_ID, restID));
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getCartList(String userID) {
       // dialogView.showCustomSpinProgress(SearchItemActivity.this);
        manager1.service.getCartList(userID).enqueue(new Callback<CartItemsResponseModel>() {
            @Override
            public void onResponse(Call<CartItemsResponseModel> call, Response<CartItemsResponseModel> response) {
                if (response.isSuccessful()) {
                   // dialogView.dismissCustomSpinProgress();
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
                                ccmList.add(ccm);
                            }
                            Gson gson = new Gson();
                            String xyz = gson.toJson(ccmList);
                            prefs.saveData(Constants.CUSTOM_CART_DATA, xyz);
                        }

                        //Log.d("cart_count>>",String.valueOf(ccmList.size()));

                    } else {

                    }

                    SearchItemsRequestModel searchItemsRequestModel = new SearchItemsRequestModel(
                            prefs.getData(Constants.REST_ID),
                            prefs.getData(Constants.USER_ID),
                            et_search.getText().toString()
                    );

                    searchItems(searchItemsRequestModel);

                } else {

                }
            }

            @Override
            public void onFailure(Call<CartItemsResponseModel> call, Throwable t) {

            }
        });
    }

    private void searchItems(SearchItemsRequestModel searchItemsRequestModel) {
        //dialogView.showCustomSpinProgress(SearchItemActivity.this);
        manager.service.searchItem(searchItemsRequestModel).enqueue(new Callback<SearchItemResponseModel>() {
            @Override
            public void onResponse(Call<SearchItemResponseModel> call, Response<SearchItemResponseModel> response) {
                if (response.isSuccessful()){
                    //dialogView.dismissCustomSpinProgress();
                    SearchItemResponseModel searchResponseModel = response.body();
                    if (searchResponseModel.error != true){

                        itemsList = searchResponseModel.items;

                        if (itemsList.size() > 0){

                            menuItemAdapter = new MenuItemAdapter(itemsList,ccmList,SearchItemActivity.this);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchItemActivity.this, LinearLayoutManager.VERTICAL, false);
                            item_rv.setLayoutManager(layoutManager);
                            item_rv.setAdapter(menuItemAdapter);
                        }

                    }else{

                    }

                }else{
                    //dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<SearchItemResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (et_search.getText().toString().length()>=2){
                            getCartList(prefs.getData(Constants.USER_ID));
                            Log.d("thread>>","calling");
                        }
                    }
                });
                if (et_search.getText().toString().length()>=2){
                    getCartList(prefs.getData(Constants.USER_ID));
                }

            }
        }, 500);
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