package com.fdxUser.app.Activity.WalletModule;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.TransactionHistAdapter;
import com.fdxUser.app.Fragments.BottomSheets.AddMoneyToWalletBottomSheet;
import com.fdxUser.app.Models.WalletModels.WalletCreditRequestModel;
import com.fdxUser.app.Models.WalletModels.WalletCreditResponseModel;
import com.fdxUser.app.Models.WalletModels.WalletDataModel;
import com.fdxUser.app.Models.WalletModels.WalletListResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity implements PaymentResultListener {
    RecyclerView transHistRv;
    RelativeLayout addMoneyToWalletRL;
    TextView tv_wallet_balance;
    Button btn_add_money;

    List<WalletDataModel> transHistList = new ArrayList<>();
    TransactionHistAdapter transactionHistAdapter;

    Prefs prefs;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;

    String wallet_amount = "0";

    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        dialogView = new DialogView();
        prefs = new Prefs(WalletActivity.this);

        transHistRv = findViewById(R.id.transHistRv);
        addMoneyToWalletRL = findViewById(R.id.addMoneyToWalletRL);
        iv_back = findViewById(R.id.iv_back);
        btn_add_money = findViewById(R.id.btn_add_money);

        tv_wallet_balance = (TextView) findViewById(R.id.tv_wallet_balance);

        //transactionHistList();

        getUserWallets(prefs.getData(Constants.USER_ID));

        btn_add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMoneyToWalletBottomSheet addMoneyToWalletBottomSheet = new AddMoneyToWalletBottomSheet();
                addMoneyToWalletBottomSheet.show(WalletActivity.this.getSupportFragmentManager(), "callWalletAdd");
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    public void openRazorPay(String amount1){
        String samount = amount1;
        wallet_amount = amount1;

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
            object.put("description", "Wallet Credit");

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
            checkout.open(WalletActivity.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
       // Toast.makeText(WalletActivity.this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();

        WalletCreditRequestModel walletCreditRequestModel = new WalletCreditRequestModel(
                prefs.getData(Constants.USER_ID),
                wallet_amount,
                s,
                "Wallet Credit"

        );
        // transactionId = s;
        creditToWalletTask(walletCreditRequestModel);
    }


    @Override
    public void onPaymentError(int i, String s) {

        //Toast.makeText(WalletActivity.this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
        Toast.makeText(WalletActivity.this, "Payment Failed!", Toast.LENGTH_SHORT).show();

    }

    private void creditToWalletTask(WalletCreditRequestModel walletCreditRequestModel) {

        dialogView.showCustomSpinProgress(WalletActivity.this);

        manager.service.walletCredit(walletCreditRequestModel).enqueue(new Callback<WalletCreditResponseModel>() {
            @Override
            public void onResponse(Call<WalletCreditResponseModel> call, Response<WalletCreditResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    WalletCreditResponseModel pom = response.body();
                    Log.d("ORDER_RESP>>", response.body().toString());
                    if (pom.error != true){

                        dialogView.showSingleButtonDialog(WalletActivity.this,"Credit Added!","Amount has been successfully credited to your wallet");
                        getUserWallets(prefs.getData(Constants.USER_ID));

                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<WalletCreditResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getUserWallets(String id){
        dialogView.showCustomSpinProgress(WalletActivity.this);
        manager.service.userWallets(id).enqueue(new Callback<WalletListResponseModel>() {
            @Override
            public void onResponse(Call<WalletListResponseModel> call, Response<WalletListResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    WalletListResponseModel nrm = response.body();
                    if (!nrm.error){

                        transHistList = nrm.wallets;

                        if (transHistList.size()>0){

                            String wallet_balance = nrm.wallet_balance;

                            DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                            String formatted1 = formatter1.format(Double.parseDouble(wallet_balance));

                            if (formatted1.equals(".00")){
                                formatted1 = "0.00";
                            }

                            tv_wallet_balance.setText("₹ " + formatted1);
                            transactionHistAdapter = new TransactionHistAdapter(WalletActivity.this, transHistList);
                            transHistRv.setLayoutManager(new LinearLayoutManager(WalletActivity.this, LinearLayoutManager.VERTICAL, false));
                            transHistRv.setAdapter(transactionHistAdapter);

                        }

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

}