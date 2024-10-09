package com.fdxUser.app.Activity.Address;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fdxUser.app.Adapters.GoogleAddressAdapter;
import com.fdxUser.app.Fragments.BottomSheets.AddAddressBottomSheet;
import com.fdxUser.app.Models.GoogleAddressBean;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.GPSTracker;
import com.fdxUser.app.Utills.Permission;
import com.fdxUser.app.utilities.async_tasks.AsyncResponse;
import com.fdxUser.app.utilities.async_tasks.AutoCompleteConnection;
import com.fdxUser.app.utilities.async_tasks.RemoteAsync;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback, AsyncResponse, TextWatcher, GoogleMap.OnCameraChangeListener, GoogleMap.OnCameraIdleListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private TextView location_set;
    GPSTracker gpsTracker;
    String title;
    GoogleMap mMap;
    Boolean is_camera_change = true;
    private double lat = 0.0, lng = 0.0;
    boolean fstMapChange = true;
    RemoteAsync remoteAsync;
    private ProgressBar statusCall;
    private boolean tv_pickup_on_focus = false;
    private RemoteAsync taskAddress;

    public static String pickup = "";
    private String address = "";
    String pin="";
    public static String status="";
    private Timer timer;
    private EditText tv_pickup;
    private ArrayList<GoogleAddressBean> listAddress;
    private ListView listView;
    private GoogleAddressBean selectedGoogleAddressBean;
    private Button btn_add_address;
    private ImageView imgError;
    View mapView;
    DialogView dialogView;
    private String TAG="ChangePass";
    RequestQueue mQueue;
    Preferences pref;
    ImageView btnImgBack;
    public static String coming_from_login = "0";
    String restId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        location_set = (TextView) findViewById(R.id.address);

        gpsTracker = new GPSTracker(this);
        location_set.addTextChangedListener(this);
        tv_pickup = (EditText) findViewById(R.id.tv_pickup);
        tv_pickup.addTextChangedListener(this);
        statusCall = (ProgressBar) findViewById(R.id.statusCall);
        listView=(ListView)findViewById(R.id.adrs_List);
        btn_add_address=(Button)findViewById(R.id.btn_add_address);
        imgError=(ImageView)findViewById(R.id.imgError);
        btnImgBack=(ImageView)findViewById(R.id.btnImgBack);


        imgError.setOnClickListener(this);
        btn_add_address.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        btnImgBack.setOnClickListener(this);
        //pref=new Preferences(this);
        dialogView=new DialogView();
        mQueue = Volley.newRequestQueue(this);

//        if (gpsTracker.canGetLocation) {
//            lat = gpsTracker.getLatitude();
//            lon = gpsTracker.getLongitude();
//        }
//        try {
//            Geocoder geocoder;
//            List<Address> addressList;
//
//            geocoder = new Geocoder(this, Locale.getDefault());
//            addressList = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lon), 1);
//            title = addressList.get(0).getAddressLine(0) + ", " + addressList.get(0).getLocality() + ", " + addressList.get(0).getAdminArea() + ", " + addressList.get(0).getPostalCode() + ", " + addressList.get(0).getCountryName();
//        } catch (Exception e) {
//            title = "Adderss not found";
//        }
//
//        location_set.setText(title);

        if (Constants.isFor.equals("Select")){
            restId = getIntent().getStringExtra(Constants.REST_ID);
        }

        try {
            Geocoder geocoder;
            List<Address> addressList;

            geocoder = new Geocoder(this, Locale.getDefault());
            addressList = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1);
            pin =addressList.get(0).getPostalCode();
        } catch (Exception e) {
            //  title = "Pin not found";
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_location);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        mMap = googleMap;
        mMap.setOnCameraChangeListener(this);
        mMap.setOnCameraIdleListener(this);

        PermissionChecking();

        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 80);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (!fstMapChange) {
            if (is_camera_change) {
                LatLng pickupLocation = cameraPosition.target;
                fetchAddrAgainstPoint(pickupLocation);
            } else {
                is_camera_change = true;
            }
        } else {
            fstMapChange = false;
        }
    }

    @Override
    public void onCameraIdle() {

    }

    public void PermissionChecking() {
        if (Permission.selfPermissionGranted(GoogleMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLocation();
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat. requestPermissions(GoogleMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
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


    private void getLocation() {
        if (gpsTracker.canGetLocation()) {

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            setCurrentAddress(latitude,longitude);
            if(lat==0.0 && lng==0.0) {
                lat = latitude;
                lng = longitude;
            }
            LatLng latLng = new LatLng(lat, lng);
            fetchAddrAgainstPoint(latLng);
            if(mMap!=null) {
                mMap.clear();
                is_camera_change=false;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void fetchAddrAgainstPoint(LatLng pickupLocation) {
        //CToast.show(getApplicationContext(),"Fetching address...");
        String fetchAddr = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(pickupLocation.latitude) + ","
                + String.valueOf(pickupLocation.longitude) + "&key=" + AutoCompleteConnection.API_KEY;
        remoteAsync = new RemoteAsync(fetchAddr);
        remoteAsync.type = RemoteAsync.GET_ADDRESS_ON_CAMERA_CHANGE;
        remoteAsync.delegate = (AsyncResponse) this;
        String urlParams = "";
        remoteAsync.execute(urlParams);
    }

    private void setCurrentAddress(double latitude, double longitude) {
        String title = "";
        try {
            Geocoder geocoder;
            List<Address> addressList;


            geocoder = new Geocoder(this, Locale.getDefault());
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            title = addressList.get(0).getAdminArea() + ", "+addressList.get(0).getPostalCode() + ", " +addressList.get(0).getCountryName();
            pin =addressList.get(0).getPostalCode();


        } catch (Exception e) {
            title = "Waiting for your location";
        }
        location_set.setText(title);
    }
    @Override
    public void processFinish(String type, String output) {
        if (type.equals(RemoteAsync.GET_ADDRESS_ON_CAMERA_CHANGE)) {

            try {

                JSONObject mJsonObject = new JSONObject(output);
                JSONArray mJsonArray = null;
                if (!mJsonObject.isNull("results"))
                    mJsonArray = mJsonObject.getJSONArray("results");
                if (mJsonArray != null && mJsonArray.length() > 0) {
                    for (int i = 0; i < 1; i++) {
                        String res = mJsonArray.getJSONObject(i).getString("formatted_address");
                        pickup = res;
                        address = res;
                        location_set.setText(address);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (type.equals(RemoteAsync.GOOGLE_LATLNG_PICKER)) {
            try {

                JSONObject jsonObj = new JSONObject(output.toString());

                JSONObject mjsonobj = jsonObj.getJSONObject("result");

                double mLat = Double.parseDouble(mjsonobj.getJSONObject("geometry")
                        .getJSONObject("location").getString("lat"));

                double mLng = Double.parseDouble(mjsonobj.getJSONObject("geometry")
                        .getJSONObject("location").getString("lng"));
                lat = mLat;
                lng = mLng;
               // Constants.searchLat = String.valueOf(lat);
               // Constants.searchLon = String.valueOf(lng);
                Constants.lat = lat;
                Constants.lng = lng;

                Log.d("lat>>",String.valueOf(lat));
                Log.d("lng>>",String.valueOf(lng));
                AddAddressBottomSheet.lati = String.valueOf(lat);
                AddAddressBottomSheet.longi = String.valueOf(lng);
                if(mMap!=null) {
                    mMap.clear();
                    is_camera_change=false;
                    LatLng currentLocation = new LatLng(lat,lng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Test", "Cannot process JSON results", e);
            }
        }
        else if (type.equals(RemoteAsync.GOOGLE_ADDRESS_PICKER)) {
            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(output.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                // Extract the Place descriptions from the results
                listAddress = new ArrayList<GoogleAddressBean>(predsJsonArray.length());
                GoogleAddressBean googleAddressBean;
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    googleAddressBean = new GoogleAddressBean();
                    googleAddressBean.setAddress(predsJsonArray.getJSONObject(i).getString("description"));
                    googleAddressBean.setPlace_id(predsJsonArray.getJSONObject(i).getString("place_id"));
//                    googleAddressBean.setTxt1(predsJsonArray.getJSONObject(i).getString("txt1"));
//                    googleAddressBean.setTxt2(predsJsonArray.getJSONObject(i).getString("txt2"));

                    listAddress.add(googleAddressBean);
                }

                if (listAddress == null || (listAddress != null && listAddress.size() <= 0)) {
                    listView.setVisibility(View.GONE);
                    statusCall.setVisibility(View.GONE);
                    //statusCall.setText("No suggestion found.");
                } else {

                    listView.setVisibility(View.VISIBLE);
                    statusCall.setVisibility(View.GONE);
                    listView.setAdapter(new GoogleAddressAdapter(listAddress, getApplicationContext()));
                }


            } catch (JSONException e) {
                Log.e("Test", "Cannot process JSON results", e);
            }

        }
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        statusCall.setVisibility(View.VISIBLE);
        tv_pickup_on_focus = true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        final CharSequence chars = s;

        if (taskAddress != null && taskAddress.getStatus() == AsyncTask.Status.RUNNING) {
            taskAddress.cancel(true);
        }

        // If the textbox is blank, then return
        if (chars.toString().length() <= 0)
            return;

        // If a selection is made then also return.
        /*if (check)
            return;*/

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (chars.toString().length()<=0){
            //      listView.setVisibility(View.GONE);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tv_pickup_on_focus) {
                            // listView.setVisibility(View.GONE);
                            statusCall.setVisibility(View.GONE);
                            //statusCall.setText("Looking for suggestion...");
                        }
                    }
                });
                if (tv_pickup_on_focus) {
                    if (chars.toString().length()>0) {
                        address_calling(chars.toString());
                    }
                }

            }
        }, 400);
    }

    private void address_calling(String item){

        taskAddress = new RemoteAsync("");
        taskAddress.type = RemoteAsync.GOOGLE_ADDRESS_PICKER;
        taskAddress.delegate = this;

        String urlParams = "";
        try {
            urlParams = "item="+ URLEncoder.encode(item, "UTF-8");
        }catch(Exception e){
            //Log.e("ParamsException-->", e.getMessage());
        }

        taskAddress.execute(urlParams);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tv_pickup_on_focus = false;
        selectedGoogleAddressBean = listAddress.get(position);
      //  tv_pickup.setText(selectedGoogleAddressBean.getAddress());
        location_set.setText(selectedGoogleAddressBean.getAddress());
        address = selectedGoogleAddressBean.getAddress();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_pickup.getWindowToken(), 0);

        lat_lon_picker();

        listView.setVisibility(View.GONE);
        statusCall.setVisibility(View.GONE);
       /* Constants.isFromGoogle = true;

        Log.d("lng2>>",String.valueOf(lng));
        Log.d("lat2>>",String.valueOf(lat));

        //Constants.searchLat = String.valueOf(lat);
        //Constants.searchLon = String.valueOf(lng);*/
        if (Constants.isFor.equals("Select")) {
            Constants.fromAddressSearch = true;
            startActivity(new Intent(GoogleMapActivity.this, AddAddress.class).putExtra(Constants.REST_ID, restId));
            finish();
        }else {
            Constants.fromAddressSearch = true;
            startActivity(new Intent(GoogleMapActivity.this, AddAddress.class));
            finish();
        }

        /*AddAddressBottomSheet.block = location_set.getText().toString();;
        AddAddressBottomSheet.localAddress = location_set.getText().toString();
        //AddAddressBottomSheet.lati = String.valueOf(lat);
        //AddAddressBottomSheet.longi = String.valueOf(lng);
        AddAddressBottomSheet.pin = pin;
        AddAddressBottomSheet.city = "Kolkata";

        if (coming_from_login.equals("1")){
            AddAddressBottomSheet.coming_from_login = "1";
        }

        if (Constants.isFor.equals("Select")){
            AddAddressBottomSheet.restId = restId;
        }

        AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet();
        addAddressBottomSheet.show(GoogleMapActivity.this.getSupportFragmentManager(), "callAddAddress");*/
//        if (status.equals("1")){
//            status="";
//            EditAddressActivity.usr_address=location_set.getText().toString();
//            EditAddressActivity.usr_pin=pin;
//            EditAddressActivity.status="1";
//            EditAddressActivity.location=address;
//            startActivity(new Intent(GoogleMapActivity.this,EditAddressActivity.class));
//        }
//        else {
//            status="";
//            AddAddressActivity.status="1";
//            AddAddressActivity.location=address;
//            startActivity(new Intent(GoogleMapActivity.this,AddAddressActivity.class));
//
//        }
        AddAddressBottomSheet addAddressBottomSheet = new AddAddressBottomSheet();

    }

    private void lat_lon_picker() {
        String remaining_url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + selectedGoogleAddressBean.getPlace_id() + "&key="+AutoCompleteConnection.API_KEY;

        remoteAsync = new RemoteAsync(remaining_url);
        remoteAsync.type = RemoteAsync.GOOGLE_LATLNG_PICKER;
        remoteAsync.delegate = (AsyncResponse) this;

        String urlParams = "";
        try {
            urlParams = "";
            //urlParams = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + selectedGoogleAddressBean.getPlace_id() + "&key=AIzaSyDzZucI3DFyg6-JxaIFqYCNREX8FT72JAM";
        }catch(Exception e){
            //Log.e("ParamsException-->", e.getMessage());
        }

        remoteAsync.execute(urlParams);
        //Log.e("params>>",urlParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_address:
//                if (status.equals("1")){
//                    EditAddressActivity.usr_address=location_set.getText().toString();
//                    EditAddressActivity.usr_pin=pin;
//                    startActivity(new Intent(GoogleMapActivity.this,EditAddressActivity.class));
//                }
//                else {
//                    AddAddressActivity.get_address=location_set.getText().toString();
//                    AddAddressActivity.pin_code=pin;
//                    startActivity(new Intent(GoogleMapActivity.this,AddAddressActivity.class));
//                }

                break;
            case R.id.imgError:
                tv_pickup.setText("");
                statusCall.setVisibility(View.GONE);
                break;
            case R.id.btnImgBack:
                onBackPressed();
                break;
        }
    }
}
