package com.fdxUser.app.Activity.RestaurantScreens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.CuisineWiseRestAdapter;
import com.fdxUser.app.Models.RestaurantModels.CuisinesWiseRestResponseModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuisinesWiseRestaurantActivity extends AppCompatActivity {
    
    ImageView ivBack;
    TextView tv_head_text;
    RecyclerView restRv;
    RelativeLayout relNoRestaurant;
    
    Prefs prefs;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    String cuisineId = "", cuisineName="";
    List<RestaurantModel> restList = new ArrayList<>();
    CuisineWiseRestAdapter cwrAdapter;
    //LocationManager locationManager;
    public static double lat = 0.0;
    public static double longi = 0.0;
    public static String loc_Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisines_wise_restaurant);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        
        prefs = new Prefs(this);
        dialogView = new DialogView();
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        tv_head_text = findViewById(R.id.tv_head_text);
        tv_head_text.setSingleLine(true);
        ivBack = findViewById(R.id.ivBack);
        restRv = findViewById(R.id.restRv);
        relNoRestaurant = findViewById(R.id.relNoRestaurant);

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (locationGPS != null) {
            lat = locationGPS.getLatitude();
            longi = locationGPS.getLongitude();
        }*/

        cuisineName = getIntent().getStringExtra(Constants.CUISINE_NAME);
        cuisineId = getIntent().getStringExtra(Constants.CUISINE_ID);

        tv_head_text.setText(cuisineName + " Special");

        getCuisinesWiseRestList(cuisineId, String.valueOf(lat), String.valueOf(longi), loc_Id);
        
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*restRv.addOnItemTouchListener(new RecyclerItemClickListener(CuisinesWiseRestaurantActivity.this, restRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(CuisinesWiseRestaurantActivity.this, RestaurantDetails.class).putExtra(Constants.REST_ID, restList.get(position).id));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));*/

        
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

    private void getCuisinesWiseRestList(String cuisineId, String lat, String lng, String loc_Id) {
        dialogView.showCustomSpinProgress(CuisinesWiseRestaurantActivity.this);
        manager.service.getCuisinesWiseRestaurants(cuisineId, lat, lng, loc_Id).enqueue(new Callback<CuisinesWiseRestResponseModel>() {
            @Override
            public void onResponse(Call<CuisinesWiseRestResponseModel> call, Response<CuisinesWiseRestResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CuisinesWiseRestResponseModel cuisinesWiseRestResponseModel = response.body();
                    if (!cuisinesWiseRestResponseModel.error){

                        restList = cuisinesWiseRestResponseModel.restaurants;

                        if (restList.size()>0) {
                            relNoRestaurant.setVisibility(View.GONE);
                            restRv.setVisibility(View.VISIBLE);
                            cwrAdapter = new CuisineWiseRestAdapter(CuisinesWiseRestaurantActivity.this, restList);
                            restRv.setLayoutManager(new LinearLayoutManager
                                    (CuisinesWiseRestaurantActivity.this,
                                            LinearLayoutManager.VERTICAL, false));

                            restRv.setAdapter(cwrAdapter);
                        }else {
                            relNoRestaurant.setVisibility(View.VISIBLE);
                            restRv.setVisibility(View.GONE);
                        }

                    }else{

                    }


                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<CuisinesWiseRestResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }
}