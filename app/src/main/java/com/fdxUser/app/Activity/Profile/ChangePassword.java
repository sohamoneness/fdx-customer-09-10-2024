package com.fdxUser.app.Activity.Profile;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

public class ChangePassword extends AppCompatActivity {

    ImageView iv_back;
    EditText etOldPass, etNewPass, etConfirmPass;
    Button btnSubmit;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;
    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        dialogView = new DialogView();
        prefs = new Prefs(ChangePassword.this);

        iv_back = findViewById(R.id.iv_back);
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirmPass = findViewById(R.id.etConfirmPass);
        btnSubmit = findViewById(R.id.btnSubmit);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (blankValidate()){
                   // Toast.makeText(ChangePassword.this, "CALL CHANGE PASSWORD API!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean blankValidate() {
        if (etOldPass.getText().toString().equals("")) {
            dialogView.errorButtonDialog(ChangePassword.this, getResources().getString(R.string.app_name), "Please enter your old password!");
            return false;
        }else if (etNewPass.getText().toString().equals("")) {
            dialogView.errorButtonDialog(ChangePassword.this, getResources().getString(R.string.app_name), "Please enter a new password!");
            return false;
        }else if (etConfirmPass.getText().toString().equals("")) {
            dialogView.errorButtonDialog(ChangePassword.this, getResources().getString(R.string.app_name), "Please enter new password to confirm!");
            return false;
        }else {
            return true;
        }

    }
}