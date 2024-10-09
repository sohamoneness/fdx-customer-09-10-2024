package com.fdxUser.app.Activity.OtherScreens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OrderHistory.OrderHistory;
import com.fdxUser.app.Models.FeedbackModels.FeedbackRequestModel;
import com.fdxUser.app.Models.FeedbackModels.FeedbackResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderDetailsModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    ImageView ivBack;
    CircleImageView ivUser;
    CircleImageView ivRestImg;
    TextView tvUserName, tvRestName;
    EditText etFeedback;
    Button btnDone;
    RatingBar rating;
    TextView tvRestAddress, tvRestName1, tvRestName2;

    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;
    String restID = "", ordRefId = "", restName = "", restImg = "";
    public static OrderDetailsModel orderDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        ivUser = findViewById(R.id.ivUser);
        tvUserName = findViewById(R.id.tvUserName);
        etFeedback = findViewById(R.id.etFeedback);
        btnDone = findViewById(R.id.btnDone);
        rating = findViewById(R.id.rating);
        ivRestImg = findViewById(R.id.ivRestImg);
        tvRestName = findViewById(R.id.tvRestName);
        tvRestAddress = findViewById(R.id.tvRestAddress);
        tvRestName1 = findViewById(R.id.tvRestName1);
        tvRestName2 = findViewById(R.id.tvRestName2);

        ordRefId = getIntent().getStringExtra(Constants.ORDER_REF_ID);
        restID = getIntent().getStringExtra(Constants.REST_ID);
        restName = getIntent().getStringExtra(Constants.REST_NAME);
        restImg = getIntent().getStringExtra(Constants.REST_IMG);

        if (restImg != null){
            Glide.with(FeedbackActivity.this).load(restImg).into(ivRestImg);
        }else{
            Glide.with(FeedbackActivity.this).load(R.drawable.no_image).into(ivRestImg);
        }

        tvRestName.setText(orderDetailsModel.restaurant.name);
        tvRestAddress.setText(orderDetailsModel.restaurant.address);
        tvRestName1.setText("Rate "+orderDetailsModel.restaurant.name);
        tvRestName2.setText("Rate "+orderDetailsModel.boy.name);
        //tvRestName.setText(restName);

        tvUserName.setText(prefs.getData(Constants.USER_NAME));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etFeedback.getText().toString().equals("")){
                    dialogView.errorButtonDialog(FeedbackActivity.this, getResources().getString(R.string.app_name), "Please enter your feedback");
                }else {
                    float userRating = rating.getRating();

                    FeedbackRequestModel feedbackRequestModel = new FeedbackRequestModel(
                            prefs.getData(Constants.USER_ID),
                            restID,
                            ordRefId,
                            String.valueOf(userRating),
                            etFeedback.getText().toString().trim()
                    );

                    submitUserFeedback(feedbackRequestModel);
                }

            }
        });

    }

    private void submitUserFeedback(FeedbackRequestModel feedbackRequestModel) {
        dialogView.showCustomSpinProgress(FeedbackActivity.this);
        manager.service.getFeedbackFromUser(feedbackRequestModel).enqueue(new Callback<FeedbackResponseModel>() {
            @Override
            public void onResponse(Call<FeedbackResponseModel> call, Response<FeedbackResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    FeedbackResponseModel feedbackResponseModel = response.body();
                    if (!feedbackResponseModel.error){
                        Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(FeedbackActivity.this, OrderHistory.class));
                        finishAffinity();
                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*startActivity(new Intent(FeedbackActivity.this, Dashboard.class));
        finishAffinity();*/
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