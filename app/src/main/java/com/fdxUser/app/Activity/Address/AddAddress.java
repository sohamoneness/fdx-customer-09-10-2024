package com.fdxUser.app.Activity.Address;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Fragments.BottomSheets.AddAddressBottomSheet;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class AddAddress extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    Button saveLocation;
    private static final int REQUEST_CHECK_SETTINGS = 123;

    TextView tvChange, tvAddress, tvBlock;
    public static AddressListModel addressListModel = new AddressListModel();
    private GoogleMap mMap;
    ImageView ivBack;
    String lat = "", lon = "", city = "", pin = "";
    String lati = "", lngi = "";

    //Location mLocation =new Location();
    Marker lastClicked = null;

    ImageView ivPin;
    LatLng centerOfMap;
    LinearLayout li_use_current_location;
    public static String is_coming_from = "1";
    public static String coming_from_login = "0";
    String restId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_address);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        saveLocation = findViewById(R.id.saveLocation);
        tvChange = findViewById(R.id.tvChange);
        ivBack = findViewById(R.id.ivBack);
        tvAddress = findViewById(R.id.tvAddress);
        ivPin = findViewById(R.id.ivPin);
        tvBlock = findViewById(R.id.tvBlock);

        if (Constants.isFor.equals("Select")){
            restId = getIntent().getStringExtra(Constants.REST_ID);
        }
        Log.d("LONGS>>", Constants.lat + ", " + Constants.lng);



        /*if (Constants.isFromGoogle){
            //lat = getIntent().getStringExtra("LAT");
            //lon = getIntent().getStringExtra("LNG");
            lat = Constants.searchLat;
            lon = Constants.searchLon;
            Log.d("LATLNG>>", lat + ", " + lon);
        }*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        li_use_current_location = findViewById(R.id.li_use_current_location);

        if (Constants.isFor.equals("Edit")){
            AddAddressBottomSheet.addressListModel = addressListModel;
            AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet();
            addAddressBottomSheet.show(AddAddress.this.getSupportFragmentManager(), "callAddAddress");
        }

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet();
                //addAddressBottomSheet.show(AddAddress.this.getSupportFragmentManager(), "callAddAddress");
                if (coming_from_login.equals("1")){
                    GoogleMapActivity.coming_from_login = "1";
                }

                if (Constants.isFor.equals("Select")){
                    startActivity(new Intent(AddAddress.this, GoogleMapActivity.class).putExtra(Constants.REST_ID, restId));
                    finish();
                    restId = getIntent().getStringExtra(Constants.REST_ID);
                }else {
                    startActivity(new Intent(AddAddress.this, GoogleMapActivity.class));
                    finish();
                }

               /* startActivity(new Intent(AddAddress.this, GoogleMapActivity.class));
                finish();*/
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        li_use_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                    mMap.clear();

                    // add markers from database to the map
                    Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
                    GPSTracker gpsTracker = new GPSTracker(AddAddress.this);
                    double currentLat = gpsTracker.getLatitude();
                    double currentLang = gpsTracker.getLongitude();

                    LatLng latLng = new LatLng(currentLat, currentLang);

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location!").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_picker)).draggable(true));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 18.0f));
                }
            }
        });

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddAddressBottomSheet.block = tvBlock.getText().toString();
                AddAddressBottomSheet.localAddress = tvAddress.getText().toString();
                AddAddressBottomSheet.lati = lat;
                AddAddressBottomSheet.longi = lon;
                AddAddressBottomSheet.pin = pin;
                AddAddressBottomSheet.city = city;

               // Constants.isFor = "";

                if (coming_from_login.equals("1")){
                    AddAddressBottomSheet.coming_from_login = "1";
                }

                if (Constants.isFor.equals("Select")) {
                    AddAddressBottomSheet.restId = restId;
                }
                AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet();
                addAddressBottomSheet.show(AddAddress.this.getSupportFragmentManager(), "callAddAddress");

            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (lastClicked!=null)
            lastClicked.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        lastClicked = marker;
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.clear();
        mMap = googleMap;
       // mLocation = mMap.getMyLocation();
        double currentLat = 0.0;
        double currentLang = 0.0;


       /* if (Constants.isFromGoogle){
            mMap.clear();
            currentLat = Double.parseDouble(lat);
            currentLang = Double.parseDouble(lon);

        }else {*/
        //setUpMapIfNeeded();

        Log.d("LOG1>>", String.valueOf(Constants.lat) + ", " + String.valueOf(Constants.lng));
        if (Constants.lat == 0.0 && Constants.lng == 0.0){
            if (Constants.fromAddressSearch){
                //setUpMapIfNeeded();
                Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
                GPSTracker gpsTracker = new GPSTracker(AddAddress.this);
                currentLat = gpsTracker.getLatitude();
                currentLang = gpsTracker.getLongitude();
                Log.d("LOG2>>", String.valueOf(currentLat) + ", " + String.valueOf(currentLang));
                LatLng latLng = new LatLng(currentLat, currentLang);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location!").draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 18.0f));
            }else {
                Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
                GPSTracker gpsTracker = new GPSTracker(AddAddress.this);
                currentLat = gpsTracker.getLatitude();
                currentLang = gpsTracker.getLongitude();
                Log.d("LOG2>>", String.valueOf(currentLat) + ", " + String.valueOf(currentLang));
                LatLng latLng = new LatLng(currentLat, currentLang);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location!").draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 18.0f));
            }

        }else {
            //Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
            //GPSTracker gpsTracker = new GPSTracker(AddAddress.this);
            currentLat = Constants.lat;
            currentLang = Constants.lng;
            Constants.lat = 0.0;
            Constants.lng = 0.0;

            Log.d("LOG3>>", String.valueOf(currentLat) + ", " + String.valueOf(currentLang));
            LatLng latLng = new LatLng(currentLat, currentLang);
            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location!").draggable(true));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 18.0f));
        }


       // }







        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange (CameraPosition position) {

                // Get the center of the Map.
                centerOfMap = mMap.getCameraPosition().target;

                //Update your Marker's position to the center of the Map.
                //mMap.setPosition(centerOfMap);
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();
                ivPin.setVisibility(View.VISIBLE);

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                ivPin.setVisibility(View.GONE);
               // val markerOptions = MarkerOptions().position(mMap.cameraPosition.target)
                     //   .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_pin))
                //mMap.addMarker(markerOptions)
                double selectedLatitude=mMap.getCameraPosition().target.latitude;
                double selectedLongitude=mMap.getCameraPosition().target.longitude;
                MarkerOptions marker = new MarkerOptions().position(mMap.getCameraPosition().target).title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_picker));
                        //.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_location_red));
                mMap.addMarker(marker);

                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
                try {
                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                    tvAddress.setText(address.getAddressLine(0));
                    tvBlock.setText(address.getSubLocality());
                    pin = address.getPostalCode();
                    lat = String.valueOf(latLng.latitude);
                    lon = String.valueOf(latLng.longitude);
                    city = address.getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });



        //setOnCameraMoveListener(this@MapStartedActivity)
        //setOnCameraIdleListener(this@MapStartedActivity)

        // Add a marker in Sydney and move the camera

        /*LatLng latLng = new LatLng(22.5709, 88.4326);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in ERGO Tower").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), 12.0f));*/



        /*mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                *//*LatLng newLocation = marker.getPosition();
                mLocation.setLatitude(newLocation.latitude);
                mLocation.setLongitude(newLocation.longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15.0f));*//*


                LatLng latLng = marker.getPosition();
                Geocoder geocoder = new Geocoder(AddAddress.this, Locale.getDefault());
                try {
                    android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
                    tvAddress.setText(address.getSubLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

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

    @Override
    public void onBackPressed() {
        int k = 0;
        //super.onBackPressed();
        if(is_coming_from.equals("1")){
            startActivity(new Intent(AddAddress.this, AddressList.class));
        }else if(is_coming_from.equals("2")){
           /* Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
            startActivity(new Intent(AddAddress.this, DashboardHome.class));
        }else{
            startActivity(new Intent(AddAddress.this, AddressList.class));
        }

        finish();
    }

   /* @Override
    protected void onResume() {
        super.onResume();



    }*/

    private void setUpMapIfNeeded() {
       // if (mMap == null) {

            Log.d("MyMap", "setUpMapIfNeeded");
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(this);
       // }
    }
}