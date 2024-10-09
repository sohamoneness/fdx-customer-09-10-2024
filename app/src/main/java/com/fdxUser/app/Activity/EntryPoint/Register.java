package com.fdxUser.app.Activity.EntryPoint;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Activity.PolicyScreens.PrivacyPolicy;
import com.fdxUser.app.Activity.PolicyScreens.TermsAndConditions;
import com.fdxUser.app.Models.RegisterModels.RegisterModel;
import com.fdxUser.app.Models.RegisterModels.RegisterRequest;
import com.fdxUser.app.Models.UserModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    Button btnSignUp;
    EditText etName, etEmail, etPhone, etPassword, etPassConfirm, etReferralCode;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Prefs prefs;
    private String TAG="Register";
    ImageView iv_back;
    TextView tvLoginNow, tvTc, tvPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialogView = new DialogView();
        prefs = new Prefs(Register.this);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        btnSignUp = findViewById(R.id.btn_sign_up);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPass);
        etPassConfirm = findViewById(R.id.etPassConfirm);
        iv_back = findViewById(R.id.iv_back);
        tvLoginNow = findViewById(R.id.tvLoginNow);
        tvTc = findViewById(R.id.tvTc);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        etReferralCode = findViewById(R.id.etReferralCode);

        tvLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, EmailLogin.class));
                finish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlankValidate()){
                    RegisterRequest registerRequest = new RegisterRequest(
                            etName.getText().toString(),
                            etEmail.getText().toString(),
                            "+91"+etPhone.getText().toString(),
                            etPassword.getText().toString(),
                            etPassConfirm.getText().toString(),
                            etReferralCode.getText().toString()
                    );

                    registerUser(registerRequest);
                }
               // startActivity(new Intent(Register.this, DashboardHome.class));
                //finish();
            }
        });

        tvTc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, TermsAndConditions.class));
            }
        });

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, PrivacyPolicy.class));
            }
        });

    }

    private void registerUser(RegisterRequest registerRequest) {
        dialogView.showCustomSpinProgress(Register.this);

        manager.service.registerNewUser(registerRequest).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RegisterModel rm = response.body();

                    if (rm.error==false){
                        UserModel um = rm.user;

//                    prefs.saveData(Constants.USER_ID, um.id);
//                    prefs.saveData(Constants.USER_NAME, um.name);
//                    prefs.saveData(Constants.LOGIN_TOKEN, rm.token);

                        //Constants.token = rm.token;

                        startActivity(new Intent(Register.this, EmailLogin.class));
                        Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), rm.message);
                    }
                }else{

                    dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Registration Failed!");

                    dialogView.dismissCustomSpinProgress();

                }
            }
            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {

                Toast.makeText(Register.this, "Something went wrong! please try again!", Toast.LENGTH_SHORT).show();
                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private boolean isBlankValidate() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (etName.getText().toString().equals("")){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter your name!");
            return false;
        }else if (etPhone.getText().toString().equals("")){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter your mobile no!");
            return false;
        }else if (etPhone.getText().toString().length()<10) {
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter valid 10 digit mobile no");
            return false;
        }else if (etEmail.getText().toString().equals("")){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter email id!");
            return false;
        }else if (!etEmail.getText().toString().matches(emailPattern)){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter valid email id!");
            return false;
        }else if (etPassword.getText().toString().equals("")){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please enter your password!");
            return false;
        }else if (etPassword.getText().toString().length()<6){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Your password should contain at least 6 characters!");
            return false;
        }else if (etPassConfirm.getText().toString().equals("")){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Please re-enter your password!");
            return false;
        }else if (!etPassConfirm.getText().toString().equals(etPassword.getText().toString())){
            dialogView.errorButtonDialog(Register.this, getResources().getString(R.string.app_name), "Password does not match!");
            return false;
        } else {
            return true;
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