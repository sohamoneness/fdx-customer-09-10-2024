package com.fdxUser.app.Fragments.BottomSheets;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Models.AddressModels.AddAddressRequest;
import com.fdxUser.app.Models.AddressModels.AddAddressResponseModel;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Models.UserModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressBottomSheet extends BottomSheetDialogFragment {

    TextView tvHome, tvChange, tvOffice, tvOthers, tvBlock, tvAddress;
    EditText etHouseNo, etStreet, etRecipient;
    Button btnSave;
    ImageView ivClose;

    DialogView dialogView;
    Prefs prefs;
    String tag = "";
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    public static AddressListModel addressListModel = new AddressListModel();
    UserModel userModel = new UserModel();

    public static String block = "";
    public static String localAddress = "";
    public static String lati = "";
    public static String longi = "";
    public static String city = "";
    public static String pin = "";

    public static String coming_from_login = "0";
    public static String restId = "";


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_address_bottomsheet, container, false);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getActivity().getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());
        userModel = addressListModel.user;

        tvHome = v.findViewById(R.id.tvHome);
        tvOthers = v.findViewById(R.id.tvOthers);
        tvOffice = v.findViewById(R.id.tvOffice);
        tvChange = v.findViewById(R.id.tvChange);
        etHouseNo = v.findViewById(R.id.etHouseNo);
        etStreet = v.findViewById(R.id.etStreet);
        etRecipient = v.findViewById(R.id.etRecipient);
        btnSave = v.findViewById(R.id.btnSave);
        tvBlock = v.findViewById(R.id.tvBlock);
        tvAddress = v.findViewById(R.id.tvAddress);
        ivClose = v.findViewById(R.id.ivClose);

        tvBlock.setText(block);
        tvAddress.setText(localAddress);
        etStreet.setText(localAddress);
        etRecipient.setText(prefs.getData(Constants.USER_NAME));

        if (Constants.isFor.equals("Edit")){

            etHouseNo.setText(addressListModel.address);
            etStreet.setText(addressListModel.location);
            etRecipient.setText(userModel.name);
            if (addressListModel.tag.equals("Home")){
                tvHome.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOffice.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Home";
            }else if (addressListModel.tag.equals("Office")){
                tvHome.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvHome.setTextColor(getResources().getColor(R.color.activeColor));
                tvOffice.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Office";

            }else {
                tvHome.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvHome.setTextColor(getResources().getColor(R.color.activeColor));
                tvOffice.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.colorAccent));
                tag = "Others";
            }

            btnSave.setText("Update address and proceed");

        }else {
            tag = "Home";
        }



        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHome.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvHome.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOffice.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Home";
            }
        });

        tvOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHome.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvHome.setTextColor(getResources().getColor(R.color.activeColor));
                tvOffice.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Office";
            }
        });

        tvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHome.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOffice.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvHome.setTextColor(getResources().getColor(R.color.activeColor));
                tvOffice.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.colorAccent));
                tag = "Others";
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlanValidate()){

                    if (!Constants.isFor.equals("Edit")){

                        AddAddressRequest addAddressRequest = new AddAddressRequest(
                                prefs.getData(Constants.USER_ID),
                                etHouseNo.getText().toString(),
                                etStreet.getText().toString(),
                                lati,
                                longi,
                                "India",
                                "West Bengal",
                                city,
                                pin,
                                tag
                        );

                        Gson gson = new Gson();
                        String address = gson.toJson(addAddressRequest);
                        Log.d("ADR", address);


                        saveAddress(addAddressRequest);

                    }else{
                        AddAddressRequest addAddressRequest = new AddAddressRequest(
                                prefs.getData(Constants.USER_ID),
                                etHouseNo.getText().toString(),
                                etStreet.getText().toString(),
                                lati,
                                longi,
                                "India",
                                "West Bengal",
                                city,
                                pin,
                                tag
                        );

                        updateAddress(addAddressRequest);
                    }

                }
            }
        });


        return v;
    }

    private void updateAddress(AddAddressRequest addAddressRequest) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.updateAddress(addAddressRequest).enqueue(new Callback<AddAddressResponseModel>() {
            @Override
            public void onResponse(Call<AddAddressResponseModel> call, Response<AddAddressResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AddAddressResponseModel aarm = response.body();
                    if (aarm.error != true){
                        dialogView.showSingleButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Address updated successfully!");
                        startActivity(new Intent(getActivity(), DashboardHome.class));
                        getActivity().finish();
                        dismiss();
                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void saveAddress(AddAddressRequest addAddressRequest) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.addAddress(addAddressRequest).enqueue(new Callback<AddAddressResponseModel>() {
            @Override
            public void onResponse(Call<AddAddressResponseModel> call, Response<AddAddressResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AddAddressResponseModel aarm = response.body();
                    if (aarm.error != true){
                        dialogView.showSingleButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Address saved successfully!");

                        if (coming_from_login.equals("1")){
                            AddAddressBottomSheet.coming_from_login = "1";
                            Constants.isNewUser = "1";
                            prefs.saveData(Constants.NEW_USER, "1");
                        }else {
                            Constants.isNewUser = "0";
                            prefs.saveData(Constants.NEW_USER, "0");
                        }

                        if (Constants.isFor.equals("Select")){
                            Constants.isFromAddressSave = true;
                            startActivity(new Intent(getActivity(), AddressList.class).putExtra(Constants.REST_ID, restId));
                            getActivity().finish();
                            dismiss();

                        }else{
                            Constants.isFromAddressSave = true;
                            startActivity(new Intent(getActivity(), AddressList.class));
                            getActivity().finish();
                            dismiss();
                        }

                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private boolean isBlanValidate() {
        if (etHouseNo.getText().toString().equals("") || etRecipient.getText().toString().equals("")){
            dialogView.errorButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Please enter house no / flat no / floor / tower to continue");
            return false;
        }else if (etStreet.getText().toString().equals("") || etRecipient.getText().toString().equals("")){
            dialogView.errorButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Please enter street / society / landmark to continue.");
            return false;
        }else{
            return true;
        }
    }

    public void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
