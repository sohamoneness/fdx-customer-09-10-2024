package com.fdxUser.app.Activity.EntryPoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.R;

public class Login extends AppCompatActivity {

    Button btnContinue;
    TextView tvEmailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnContinue = findViewById(R.id.btn_continue);
        tvEmailLogin = findViewById(R.id.tvEmailLogin);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, OTP.class));
                finish();
            }
        });

        tvEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, EmailLogin.class));
            }
        });

    }
}