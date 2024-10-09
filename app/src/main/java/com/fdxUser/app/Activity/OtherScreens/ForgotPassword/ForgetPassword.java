package com.fdxUser.app.Activity.OtherScreens.ForgotPassword;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Models.ForgotPassModels.ForgetPassRequestModel;
import com.fdxUser.app.Models.ForgotPassModels.ForgetPassResponseModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    Button btnSubmit;
    EditText etPass, etConPass;

    String userID = "";
    ApiManager manager = new ApiManager();
    DialogView dialogView;

    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        dialogView = new DialogView();

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivBack = findViewById(R.id.ivBack);

        userID = getIntent().getStringExtra(Constants.USER_ID);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPass.getText().toString().equals("")){
                    Toast.makeText(ForgetPassword.this, "Please enter a new password!", Toast.LENGTH_SHORT).show();
                }else if (etConPass.getText().toString().equals("")){
                    Toast.makeText(ForgetPassword.this, "Please enter your new password again to confirm!", Toast.LENGTH_SHORT).show();
                }else if (!etConPass.getText().toString().equals(etPass.getText().toString())){
                    Toast.makeText(ForgetPassword.this, "Password didn't match please enter your password correctly!", Toast.LENGTH_SHORT).show();
                }else {
                    ForgetPassRequestModel forgetPassRequestModel = new ForgetPassRequestModel(
                            userID,
                            etPass.getText().toString()
                    );
                    forgotPass(forgetPassRequestModel);
                }
            }
        });


    }

    private void forgotPass(ForgetPassRequestModel forgetPassRequestModel) {
        dialogView.showCustomSpinProgress(ForgetPassword.this);
        manager.service.updateForgotPassword(forgetPassRequestModel).enqueue(new Callback<ForgetPassResponseModel>() {
            @Override
            public void onResponse(Call<ForgetPassResponseModel> call, Response<ForgetPassResponseModel> response) {
                if (response.isSuccessful()){
                    ForgetPassResponseModel fprm = response.body();
                    if (!fprm.error){
                        dialogView.dismissCustomSpinProgress();
                        startActivity(new Intent(ForgetPassword.this, EmailLogin.class));
                        Toast.makeText(ForgetPassword.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ForgetPassResponseModel> call, Throwable t) {
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