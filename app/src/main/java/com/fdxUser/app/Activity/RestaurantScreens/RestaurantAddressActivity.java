package com.fdxUser.app.Activity.RestaurantScreens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestaurantAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView ivBack;
    TextView tv_head_text, tvAddress, tvOpen, tvClose;
    TextView tv_mon_time, tv_tues_time, tv_wed_time, tv_thurs_time, tv_fri_time, tv_sat_time, tv_sun_time;
    String name= "", address= "", open = "", close="";
    MapView mapView;
    GoogleMap mMap;
    double lati = 0.0, longi = 0.0;
    String lat = "", lng = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_address);
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        mapView = findViewById(R.id.mapView);

       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        //mapFragment.getMapAsync(RestaurantAddressActivity.this);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        ivBack = findViewById(R.id.iv_back);
        tvAddress = findViewById(R.id.tvAddress);
        tvOpen = findViewById(R.id.tvOpen);
        tvClose = findViewById(R.id.tvClose);
        tv_head_text = findViewById(R.id.tv_head_text);


        name = getIntent().getStringExtra("rest_name");
        address = getIntent().getStringExtra("rest_adr");
        open = getIntent().getStringExtra("store_open");
        close = getIntent().getStringExtra("store_close");
        lat = getIntent().getStringExtra("store_lat");
        lng = getIntent().getStringExtra("store_lng");

        lati = Double.parseDouble(lat);
        longi = Double.parseDouble(lng);

        tv_head_text.setText(name);
        tvAddress.setText(address);

        tv_mon_time = findViewById(R.id.tv_mon_time);
        tv_tues_time = findViewById(R.id.tv_tues_time);
        tv_wed_time = findViewById(R.id.tv_wed_time);
        tv_thurs_time = findViewById(R.id.tv_thurs_time);
        tv_fri_time = findViewById(R.id.tv_fri_time);
        tv_sat_time = findViewById(R.id.tv_sat_time);
        tv_sun_time = findViewById(R.id.tv_sun_time);

        final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        try {
            final Date dateObj = sdf.parse(open);
            final Date dateObj2 = sdf.parse(close);
            String time1 = new SimpleDateFormat("hh:mm a").format(dateObj);
            String time2 = new SimpleDateFormat("K:mm a").format(dateObj2);
            tvOpen.setText("Opens at: " + time1);
            tvClose.setText("Accepting orders untill  " + time2);

            tv_mon_time.setText(time1+" - "+time2);
            tv_tues_time.setText(time1+" - "+time2);
            tv_wed_time.setText(time1+" - "+time2);
            tv_thurs_time.setText(time1+" - "+time2);
            tv_fri_time.setText(time1+" - "+time2);
            tv_sat_time.setText(time1+" - "+time2);
            tv_sun_time.setText(time1+" - "+time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        String title = name;
        LatLng latLng = new LatLng(lati, longi);
        mMap.addMarker(new MarkerOptions().position(latLng).title(title).draggable(false));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 18.0f));
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