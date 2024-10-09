package com.fdxUser.app.Activity.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fdxUser.app.Models.ProfileModels.ProfileImageUploadResponseModel;
import com.fdxUser.app.Models.ProfileModels.ProfileUpdateRequestModel;
import com.fdxUser.app.Models.ProfileModels.ProfileUpdateResponseModel;
import com.fdxUser.app.Models.UserDetailsModel;
import com.fdxUser.app.Models.UserModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.Network.ImageApiManager;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.fdxUser.app.utilities.others.CToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    ImageView ivBack, iv_edit_prf_img;
    EditText etName, etEmail, etPhn;
    TextView tvDob;
    Spinner spGender;
    Button btnUpdate;
    Prefs prefs;
    List<String> genderList = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();
    String gender = "", image = "";

    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    ImageApiManager imgManager = new ImageApiManager();

    CircleImageView prf_pic_iv;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = new Prefs(EditProfile.this);
        dialogView = new DialogView();

        genderList.add("Select");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Others");

        ivBack = findViewById(R.id.iv_back);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPhn = findViewById(R.id.etPhn);
        tvDob = findViewById(R.id.tv_dob);
        spGender = findViewById(R.id.spGender);
        btnUpdate = findViewById(R.id.btnUpdate);
        iv_edit_prf_img = findViewById(R.id.iv_edit_prf_img);
        prf_pic_iv = findViewById(R.id.prf_pic_iv);

        etPhn.setEnabled(false);

        ArrayAdapter<String> spcatAdapter = new ArrayAdapter<String>(
                EditProfile.this, R.layout.my_spinner_row, genderList);
        spcatAdapter.setDropDownViewResource(R.layout.my_spinner_row);
        spGender.setAdapter(spcatAdapter);
        spGender.setFocusableInTouchMode(false);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        iv_edit_prf_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(EditProfile.this)) {
                    selectPhoto(EditProfile.this);
                }
            }
        });

        etName.setText(prefs.getData(Constants.USER_NAME));
        etEmail.setText(prefs.getData(Constants.USER_EMAIL));
        etPhn.setText(prefs.getData(Constants.USER_MOBILE));
        if(!prefs.getData(Constants.USER_DOB).equals("1900-01-01")) {
            tvDob.setText(prefs.getData(Constants.USER_DOB));
        }else{
            tvDob.setText("");
        }
        spGender.setSelection(genderList.indexOf(prefs.getData(Constants.USER_GENDER)));
        Log.d("user img>>",prefs.getData(Constants.USER_IMG));
        //Glide.with(EditProfile.this).load(prefs.getData(Constants.USER_IMG)).into(prf_pic_iv);
        Glide.with(EditProfile.this).load(prefs.getData(Constants.USER_IMG)).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(prf_pic_iv);

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (genderList.get(position).equals("Select")) {

                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                } else{
                    gender = genderList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                /*if (isFrom == 1){
                    catID = itemModel.id;
                }*/

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("IMG", image);
                CToast.show(EditProfile.this,image);
                ProfileUpdateRequestModel profileUpdateRequestModel = new ProfileUpdateRequestModel(
                        prefs.getData(Constants.USER_ID),
                        etName.getText().toString(),
                        etEmail.getText().toString(),
                        gender,
                        tvDob.getText().toString(),
                        image
                );

                updateProfile(profileUpdateRequestModel);
            }
        });

    }

    private void selectPhoto(Context context) {
       // final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        final CharSequence[] optionsMenu = {"Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else*/
                if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(EditProfile.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditProfile.this,
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(EditProfile.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditProfile.this,
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    selectPhoto(EditProfile.this);
                }
                break;
        }


    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 0) {
            if (resultCode == RESULT_OK && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                File file = savebitmap(photo);
                //tvPath.setText("image captured");
                uploadImg(file);

            }

        }else*/
        if (requestCode == 1){
            if (resultCode == RESULT_OK) {
                try {

                    InputStream is = getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void uploadImage(byte[] bytes) {
        long lengthbmp = bytes.length;
        double lengthMb = lengthbmp/1024.0/1024.0;
        Log.d("len>>",String.valueOf(lengthMb));

        if (lengthMb<=2.0){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
            imgManager.service.uploadRestaurantProfileImage(body).enqueue(new Callback<ProfileImageUploadResponseModel>() {
                @Override
                public void onResponse(Call<ProfileImageUploadResponseModel> call, Response<ProfileImageUploadResponseModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().status.equals("1")){
                            image = response.body().file_link;
                            Log.d("img>>",image);
                            //CToast.show(EditProfile.this,image);
                            Glide.with(EditProfile.this).load(image).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(prf_pic_iv);
                            Toast.makeText(EditProfile.this, "Success!!!!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(EditProfile.this, "Server error!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileImageUploadResponseModel> call, Throwable t) {
                    Toast.makeText(EditProfile.this, "Failed!!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(EditProfile.this, "Maximum image size should be of 2.0 Mb", Toast.LENGTH_SHORT).show();
        }

    }



   /* private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        // String temp = null;
        File file = new File(extStorageDirectory, "temp.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp.png");

        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }*/

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    private void updateProfile(ProfileUpdateRequestModel profileUpdateRequestModel) {
        dialogView.showCustomSpinProgress(EditProfile.this);
        manager.service.updateUserProfile(profileUpdateRequestModel).enqueue(new Callback<ProfileUpdateResponseModel>() {
            @Override
            public void onResponse(Call<ProfileUpdateResponseModel> call, Response<ProfileUpdateResponseModel> response) {
                if (response.isSuccessful()){
                    ProfileUpdateResponseModel purm = response.body();
                    if (!purm.error){
                        dialogView.dismissCustomSpinProgress();
                        getUserDetails();
                        Toast.makeText(EditProfile.this,"Success",Toast.LENGTH_SHORT).show();
                        //finish();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void getUserDetails() {
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

                    etName.setText(prefs.getData(Constants.USER_NAME));
                    etEmail.setText(prefs.getData(Constants.USER_EMAIL));
                    etPhn.setText(prefs.getData(Constants.USER_MOBILE));
                    tvDob.setText(prefs.getData(Constants.USER_DOB));
                    spGender.setSelection(genderList.indexOf(prefs.getData(Constants.USER_GENDER)));
                    Log.d("user img>>",prefs.getData(Constants.USER_IMG));
                    //Glide.with(EditProfile.this).load(prefs.getData(Constants.USER_IMG)).into(prf_pic_iv);
                    Glide.with(EditProfile.this).load(prefs.getData(Constants.USER_IMG)).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).into(prf_pic_iv);
                }
            }

            @Override
            public void onFailure(Call<UserDetailsModel> call, Throwable t) {

            }
        });

    }

    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(android.icu.util.Calendar.YEAR, year);
            myCalendar.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tvDob);
        }

    };
    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

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