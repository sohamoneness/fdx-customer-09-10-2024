package com.fdxUser.app.Activity.PolicyScreens;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.fdxUser.app.R;

public class Support extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private RelativeLayout rel1, rel2, rel3, rel4;

    private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

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

        rel1 = (RelativeLayout) findViewById(R.id.rel1);
        rel2 = (RelativeLayout) findViewById(R.id.rel2);
        rel3 = (RelativeLayout) findViewById(R.id.rel3);
        rel4 = (RelativeLayout) findViewById(R.id.rel4);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        rel1.setOnClickListener(this);
        rel2.setOnClickListener(this);
        rel3.setOnClickListener(this);
        rel4.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel1:
                callPhoneNumber("7074355488");
                break;
            case R.id.rel2:
                sendEmail();
                break;
            case R.id.rel3:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.foodexpressonline.com/"));
                startActivity(browserIntent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    /**
     * This method is for calling phone no
     * @param phone
     */
    public void callPhoneNumber(String phone) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(Support.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST);

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is for sending email
     */
    private void sendEmail() {
        String to = "info@foodexpressonline.com";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "");

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

}