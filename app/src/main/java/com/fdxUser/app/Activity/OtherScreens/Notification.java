package com.fdxUser.app.Activity.OtherScreens;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Activity.WalletModule.WalletActivity;
import com.fdxUser.app.Adapters.NotificationAdapter;
import com.fdxUser.app.Models.NotificationModels.NotificationModel;
import com.fdxUser.app.Models.NotificationModels.NotificationResponseModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.Utills.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification extends AppCompatActivity {

    RecyclerView notiRv;
    ImageView ivBack;
    NotificationAdapter nAdapter;

    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(Notification.this);
        dialogView = new DialogView();

        notiRv = findViewById(R.id.notiRv);
        ivBack = findViewById(R.id.ivBack);

        getAllNotifications(prefs.getData(Constants.USER_ID));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        notiRv.addOnItemTouchListener(new RecyclerItemClickListener(Notification.this, notiRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (notificationList.get(position).notification_type.equals("1")){
                    Log.d("case>>","i am in case1");
                    OrderStatusActivity.orderId = notificationList.get(position).ref_id;
                    OrderStatusActivity.from = "Notification";
                    startActivity(new Intent(Notification.this, OrderStatusActivity.class));
                    finish();
                }else if (notificationList.get(position).notification_type.equals("2")){
                    Log.d("case>>","i am in case2");
                    startActivity(new Intent(Notification.this, WalletActivity.class));
                    finish();
                }else if (notificationList.get(position).notification_type.equals("3")){
                    Log.d("case>>","i am in case3");
                    Constants.TAG = 0;
                    Intent intent = new Intent(Notification.this, RestaurantDetails.class);
                    intent.putExtra(Constants.REST_IMG, "");
                    //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(Notification.this, new Pair<View, String>(notificationList.get(position).ref_id,"iv"));
                    intent.putExtra(Constants.REST_ID, notificationList.get(position).ref_id);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void getAllNotifications(String data) {
        dialogView.showCustomSpinProgress(Notification.this);
        manager.service.getNotifications(data).enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    NotificationResponseModel nrm = response.body();
                    if (!nrm.error){

                        notificationList = nrm.notifications;

                        if (notificationList.size()>0){

                            nAdapter = new NotificationAdapter(Notification.this, notificationList);
                            notiRv.setLayoutManager(new LinearLayoutManager(Notification.this,
                                    LinearLayoutManager.VERTICAL, false));
                            notiRv.setAdapter(nAdapter);

                        }

                    }else{

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t) {

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