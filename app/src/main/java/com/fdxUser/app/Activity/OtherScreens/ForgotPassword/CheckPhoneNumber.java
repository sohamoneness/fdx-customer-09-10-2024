package com.fdxUser.app.Activity.OtherScreens.ForgotPassword;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Models.ForgotPassModels.CheckPhoneRequestModel;
import com.fdxUser.app.Models.ForgotPassModels.CheckPhoneResponseModel;
import com.fdxUser.app.Models.UserModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckPhoneNumber extends AppCompatActivity {

    EditText etPhn;
    Button btnSubmit;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    UserModel userModel = new UserModel();
    String userId = "";
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_number);

        dialogView = new DialogView();

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        etPhn = findViewById(R.id.etPhn);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivBack = findViewById(R.id.ivBack);

       /* String pWeight = "12.56 kg";
        String mWeight = pWeight.substring(2, pWeight.length());
        String[] split = pWeight.split("\\s");
        // mWeight = split[0].toString();
        mWeight = split[0].toString();
        Log.d("String>>", mWeight);*/

        String weight = "wegfgu 12.50 kg";

        String finalWeight = "";
        finalWeight = weight.substring(2, weight.length());
        String[] ss = weight.split("\\s");
        finalWeight = ss[1].toString() + ss[2].toString();
        Log.d("String>>", finalWeight);
        //tvWeight.setText(finalWeight);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhn.getText().toString().equals("")){
                    Toast.makeText(CheckPhoneNumber.this, "Please enter your registered phone number!", Toast.LENGTH_SHORT).show();
                }else {
                    CheckPhoneRequestModel checkPhoneRequestModel = new CheckPhoneRequestModel(
                            "+91"+etPhn.getText().toString()
                    );
                    checkPhoneNumber(checkPhoneRequestModel);
                }
            }
        });

    }

    private void checkPhoneNumber(CheckPhoneRequestModel checkPhoneRequestModel) {
        dialogView.showCustomSpinProgress(CheckPhoneNumber.this);
        manager.service.checkNumber(checkPhoneRequestModel).enqueue(new Callback<CheckPhoneResponseModel>() {
            @Override
            public void onResponse(Call<CheckPhoneResponseModel> call, Response<CheckPhoneResponseModel> response) {
                if (response.isSuccessful()){
                    CheckPhoneResponseModel cprm = response.body();
                    if (!cprm.error){
                        dialogView.dismissCustomSpinProgress();
                        userId = cprm.data.id;
                        startActivity(new Intent(CheckPhoneNumber.this, ForgetPassword.class).putExtra(Constants.USER_ID, userId));
                        //finish();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(CheckPhoneNumber.this, cprm.message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CheckPhoneResponseModel> call, Throwable t) {
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