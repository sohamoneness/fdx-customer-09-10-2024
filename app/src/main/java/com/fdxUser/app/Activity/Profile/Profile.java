package com.fdxUser.app.Activity.Profile;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.EntryPoint.EmailLogin;
import com.fdxUser.app.Activity.OrderHistory.OrderHistory;
import com.fdxUser.app.Activity.OtherScreens.FavouriteActivity;
import com.fdxUser.app.Activity.PolicyScreens.About;
import com.fdxUser.app.Activity.PolicyScreens.PrivacyPolicy;
import com.fdxUser.app.Activity.PolicyScreens.RefundPolicy;
import com.fdxUser.app.Activity.PolicyScreens.Support;
import com.fdxUser.app.Activity.PolicyScreens.TermsAndConditions;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.WalletModule.WalletActivity;
import com.fdxUser.app.Models.UserDetailsModel;
import com.fdxUser.app.Models.UserModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.Prefs;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    TextView tvEdit, tvPhn;
    RelativeLayout orderHistRL, addressRL, walletRL, shareRL, aboutRL, policyRL, termRL, refundRL, logoutRL, aboutRL2;
    RelativeLayout changePassRL, copyRl, shareRl, fav_rl, supportRL;
    ImageView ivBack;
    Prefs prefs;
    TextView tv_user_name, tv_user_email, tvDob, tvGender, tv_your_ref_code;
    CircleImageView iv_user;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    String referal_code = "";
    RelativeLayout homeRl, histRl, subcriptionRl, profileRl;
    ImageView ivHome, ivCart, ivCrown, ivPrf;
    TextView tvCrown, tvCart, tvHome, tvPrf, tv_ref_txt;

    LinearLayout li_coming_soon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
//        }
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

        prefs = new Prefs(Profile.this);

        tvEdit = findViewById(R.id.tv_edit);
        ivBack = findViewById(R.id.iv_back);
        orderHistRL = findViewById(R.id.ord_hist_rl);
        addressRL = findViewById(R.id.address_rl);
        walletRL = findViewById(R.id.wallet_rl);
        shareRL = findViewById(R.id.share_app_rl);
        aboutRL = findViewById(R.id.about_rl);
        policyRL = findViewById(R.id.policy_rl);
        logoutRL = findViewById(R.id.logout_rl);
        changePassRL = findViewById(R.id.changePassRL);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_email = findViewById(R.id.tv_user_email);
        tvDob = findViewById(R.id.tvDob);
        tvGender = findViewById(R.id.tvGender);
        tvPhn = findViewById(R.id.tvPhn);
        copyRl = findViewById(R.id.copyRl);
        shareRl = findViewById(R.id.shareRl);
        fav_rl = findViewById(R.id.fav_rl);
        tv_your_ref_code = findViewById(R.id.tv_your_ref_code);
        iv_user = findViewById(R.id.iv_user);

        homeRl = findViewById(R.id.homeRl);
        histRl = findViewById(R.id.histRl);
        subcriptionRl = findViewById(R.id.subcriptionRl);
        profileRl = findViewById(R.id.profileRl);

        ivHome = findViewById(R.id.ivHome);
        ivCart = findViewById(R.id.ivCart);
        ivCrown = findViewById(R.id.ivCrown);
        ivPrf = findViewById(R.id.ivPrf);

        tvHome = findViewById(R.id.tvHome);
        tvCart = findViewById(R.id.tvCart);
        tvCrown = findViewById(R.id.tvCrown);
        tvPrf = findViewById(R.id.tvPrf);

        tv_ref_txt = findViewById(R.id.tv_ref_txt);

        li_coming_soon = findViewById(R.id.li_coming_soon);

        termRL = findViewById(R.id.tc_rl);
        supportRL = findViewById(R.id.support_rl);
        refundRL = findViewById(R.id.refund_rl);
        aboutRL2 = findViewById(R.id.about_rl2);
        Constants.token = prefs.getData(Constants.LOGIN_TOKEN);

        getUserData();

        //tv_user_name.setText(prefs.getData(Constants.USER_NAME));

        try {
            if (Constants.referralList.size()>0){
                tv_ref_txt.setText(Constants.referralList.get(0).description);
            }else{
                tv_ref_txt.setText("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                //finish();
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Profile.this, EditProfile.class));

            }
        });

        fav_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, FavouriteActivity.class));
            }
        });

        changePassRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, ChangePassword.class));
            }
        });

        shareRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/html");
                String body = "Download Food Express app and use my Referral code: 2I6G9Y to register, get benefit on your first order. \n Use the following link to download the app: https://play.google.com/store/apps/details?id=com.fdxUser.app";
                String sub = "Refer and earn discounts and many more...";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        orderHistRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, OrderHistory.class));
            }
        });

        addressRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isFor = "";
                Constants.isFromHome = false;
                startActivity(new Intent(Profile.this, AddressList.class));
            }
        });

        refundRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, RefundPolicy.class));
            }
        });

        aboutRL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, About.class));
            }
        });

        policyRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, PrivacyPolicy.class));
            }
        });

        termRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, TermsAndConditions.class));

            }
        });

        supportRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Support.class));
            }
        });


        walletRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, WalletActivity.class));
            }
        });

        shareRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Download Food Express app and use my Referral code: "+prefs.getData(Constants.USER_REF_ID)+" to register, get free delivery on your first order. Use the following link to download the app: https://play.google.com/store/apps/details?id=com.fdxUser.app";
                String sub = "Refer and earn discounts and many more...";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
            }
        });

        copyRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tv_your_ref_code.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Profile.this, "Referral Code Copied!", Toast.LENGTH_SHORT).show();
            }
        });

        logoutRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLogoutPopup();
               /* prefs.clearAllData();
                Constants.token = "";
                startActivity(new Intent(Profile.this, EmailLogin.class));
                finishAffinity();*/


            }
        });


        homeRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, DashboardHome.class));
                finish();
            }
        });

        histRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Profile.this, OrderHistory.class));

            }
        });

        profileRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        subcriptionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCrown.setTextColor(getResources().getColor(R.color.colorAccent));
                ivCrown.setColorFilter(ContextCompat.getColor(Profile.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

                tvPrf.setTextColor(getResources().getColor(R.color.black));
                ivPrf.setColorFilter(ContextCompat.getColor(Profile.this, R.color.black), PorterDuff.Mode.SRC_IN);

                li_coming_soon.setVisibility(View.VISIBLE);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Do some stuff
                                li_coming_soon.setVisibility(View.GONE);
                                tvPrf.setTextColor(getResources().getColor(R.color.colorAccent));
                                //ivHome.setColorFilter(Color.argb(1, 255, 255, 255));
                                ivPrf.setColorFilter(ContextCompat.getColor(Profile.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                                //ivHome.setColorFilter(ContextCompat.getColor(DashboardHome.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

                                //subcriptionLL.setBackgroundResource(R.color.white);
                                tvCrown.setTextColor(getResources().getColor(R.color.black));
                                //ivCrown.setColorFilter(Color.argb(1, 0, 0, 0));
                                ivCrown.setColorFilter(ContextCompat.getColor(Profile.this, R.color.black), PorterDuff.Mode.SRC_IN);
                            }
                        });
                    }
                };
                thread.start(); //start the thread
            }
        });




    }

    private void getUserData() {
        manager.service.getUserDetails().enqueue(new Callback<UserDetailsModel>() {
            @Override
            public void onResponse(Call<UserDetailsModel> call, Response<UserDetailsModel> response) {
                if (response.isSuccessful()) {

                    UserDetailsModel udm = response.body();
                    UserModel userModel = udm.user;
                    prefs.saveData(Constants.USER_ID, userModel.id);
                    prefs.saveData(Constants.USER_NAME, userModel.name);
                    prefs.saveData(Constants.USER_MOBILE, userModel.mobile);
                    prefs.saveData(Constants.USER_EMAIL, userModel.email);
                    prefs.saveData(Constants.USER_DOB, userModel.date_of_birth);
                    prefs.saveData(Constants.USER_GENDER, userModel.gender);
                    prefs.saveData(Constants.USER_CITY, userModel.city);
                    prefs.saveData(Constants.USER_ADDRESS, userModel.address);
                    prefs.saveData(Constants.USER_WALLET_ID, userModel.wallet_id);
                    prefs.saveData(Constants.USER_REF_ID, userModel.referal_code);
                    prefs.saveData(Constants.USER_IMG, userModel.image);

                    tv_user_name.setText(prefs.getData(Constants.USER_NAME));
                    //tv_user_email.setText(prefs.getData(Constants.USER_EMAIL));
                    tv_user_email.setText(prefs.getData(Constants.USER_EMAIL));
                    tvPhn.setText(prefs.getData(Constants.USER_MOBILE));
                    tvDob.setText("DOB: " + prefs.getData(Constants.USER_DOB));
                    tvGender.setText("Gender: " + prefs.getData(Constants.USER_GENDER));
                    tv_your_ref_code.setText(prefs.getData(Constants.USER_REF_ID));
                    Log.d("image>>",prefs.getData(Constants.USER_IMG));
                    Glide.with(Profile.this).load(prefs.getData(Constants.USER_IMG)).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(iv_user);

                    //Toast.makeText(Profile.this, "Please wait while fetching your app data!", Toast.LENGTH_SHORT).show();

                }else {
                    //showTokenAlert();
                }
            }

            @Override
            public void onFailure(Call<UserDetailsModel> call, Throwable t) {

            }
        });

    }

    private void showTokenAlert() {
        Dialog dialog = new Dialog(Profile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
       /* LayoutInflater layoutInflater = LayoutInflater.from(RestaurantDetails.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        //Constants.isDialogOn = 1;
        final AlertDialog alertD = new AlertDialog.Builder(RestaurantDetails.this).create();*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Session Expired! Please Login Again!");
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setBackground(getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.isDialogOn = 0;
                startActivity(new Intent(Profile.this, EmailLogin.class));
                finish();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isDialogOn = 0;
                dialog.dismiss();
            }
        });

       /* alertD.setView(promptView);
        try {*/
        dialog.show();
       /* } catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/

    }

    private void showLogoutPopup() {


        Dialog dialog = new Dialog(Profile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.logout_popup_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(Profile.this);
        View promptView = layoutInflater.inflate(R.layout.logout_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(Profile.this).create();
        alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertD.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertD.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
       // alertD.setView(promptView, 16, 16, 16, 16);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            alertD.getWindow().setClipToOutline(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            alertD.getWindow().setClipToOutline(false);
        }*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        tvHeader.setVisibility(View.GONE);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Do you want to logout ?");

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //deleteFromCart(id, pos);
                prefs.clearAllData();
                Constants.token = "";
                startActivity(new Intent(Profile.this, EmailLogin.class));
                finishAffinity();
                dialog.dismiss();

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

       /* alertD.setView(promptView);
        alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);
        alertD.show();*/
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_user_name.setText(prefs.getData(Constants.USER_NAME));
        //tv_user_email.setText(prefs.getData(Constants.USER_EMAIL));
        tv_user_email.setText(prefs.getData(Constants.USER_EMAIL));
        tvPhn.setText(prefs.getData(Constants.USER_MOBILE));
        tvDob.setText("DOB: " + prefs.getData(Constants.USER_DOB));
        tvGender.setText("Gender: " + prefs.getData(Constants.USER_GENDER));
        tv_your_ref_code.setText(prefs.getData(Constants.USER_REF_ID));
        Log.d("image>>",prefs.getData(Constants.USER_IMG));
        Glide.with(Profile.this).load(prefs.getData(Constants.USER_IMG)).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(iv_user);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Profile.this, DashboardHome.class));
        finish();
    }
}