package com.fdxUser.app.Fragments.BottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fdxUser.app.Models.ClaimGiftModels.ClaimGiftRequestModel;
import com.fdxUser.app.Models.ClaimGiftModels.ClaimGiftResponseModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimGiftBottomSheet extends BottomSheetDialogFragment {

    TextView tvFemale, tvMale, tvOthers, tvDob, tvAnniversary;
    Button btnSave;
    ImageView ivClose;
    String anni = "";

    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    DialogView dialogView;
    Prefs prefs;
    String tag = "";
    ApiManager manager = new ApiManager();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.claim_gift_bottomsheet, container, false);

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getActivity().getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        tvFemale = v.findViewById(R.id.tvFemale);
        tvOthers = v.findViewById(R.id.tvOthers);
        tvMale = v.findViewById(R.id.tvMale);
        tvDob = v.findViewById(R.id.tvDob);
        tvAnniversary = v.findViewById(R.id.tvAnniversary);
        btnSave = v.findViewById(R.id.btnSave);
        ivClose = v.findViewById(R.id.ivClose);

        tag = "Female";


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFemale.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvMale.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvFemale.setTextColor(getResources().getColor(R.color.colorAccent));
                tvMale.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Female";
            }
        });

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFemale.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvMale.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvFemale.setTextColor(getResources().getColor(R.color.activeColor));
                tvMale.setTextColor(getResources().getColor(R.color.colorAccent));
                tvOthers.setTextColor(getResources().getColor(R.color.activeColor));
                tag = "Male";
            }
        });

        tvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFemale.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvMale.setBackground(getResources().getDrawable(R.drawable.white_bg_with_border));
                tvOthers.setBackground(getResources().getDrawable(R.drawable.rounded_address_selected));
                tvFemale.setTextColor(getResources().getColor(R.color.activeColor));
                tvMale.setTextColor(getResources().getColor(R.color.activeColor));
                tvOthers.setTextColor(getResources().getColor(R.color.colorAccent));
                tag = "Other";
            }
        });

        tvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tvAnniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlankValidate()) {
                    Log.d("USER_ID>>", prefs.getData(Constants.USER_ID) + " - " + tvAnniversary.getText().toString());
                    if (tvAnniversary.getText().toString().equals("YYYY/MM/DD")){
                        anni = "";
                    }else {
                        anni = tvAnniversary.getText().toString();
                    }
                    ClaimGiftRequestModel claimGiftRequestModel = new ClaimGiftRequestModel(
                            prefs.getData(Constants.USER_ID),
                            tag,
                            tvDob.getText().toString(),
                            anni
                    );
                    saveGiftRequest(claimGiftRequestModel);
                }
            }
        });


        return v;
    }

    private void saveGiftRequest(ClaimGiftRequestModel claimGiftRequestModel) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.claimGifts(claimGiftRequestModel).enqueue(new Callback<ClaimGiftResponseModel>() {
            @Override
            public void onResponse(Call<ClaimGiftResponseModel> call, Response<ClaimGiftResponseModel> response) {
                if (response.isSuccessful()){
                    ClaimGiftResponseModel cgrm = response.body();
                    if (!cgrm.error){
                        dialogView.dismissCustomSpinProgress();
                        //Toast.makeText(getActivity(), cgrm.message, Toast.LENGTH_SHORT).show();
                        dismiss();
                        showClaimedGiftPopup();
                    }else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(getActivity(), cgrm.message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ClaimGiftResponseModel> call, Throwable t) {
               dialogView.dismissCustomSpinProgress();

            }
        });
    }


    private boolean isBlankValidate() {
        if (tvDob.getText().toString().equals("YYYY/MM/DD") || tvDob.getText().toString().equals("")){
            dialogView.errorButtonDialog(getActivity(), getActivity().getResources().getString(R.string.app_name), "Please select your Date Of Birth!");
            return false;
        }/*else if (tvAnniversary.getText().toString().equals("YYYY/MM/DD")){
            //dialogView.errorButtonDialog(getActivity(), getActivity().getResources().getString(R.string.app_name), "Please select your Date Of Birth!");
           // return false;
        }*/else {
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

    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(android.icu.util.Calendar.YEAR, year);
            myCalendar1.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar1.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1(tvAnniversary);
        }

    };

    private void updateLabel(TextView editText) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1(TextView editText) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar1.getTime()));
    }

    private void showClaimedGiftPopup() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.claimed_gift_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.claimed_gift_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();*/
        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);


        Button btnCancel = (Button) dialog.findViewById(R.id.btnMyOrder);
        //btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnHome);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //deleteFromCart(id, pos);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //alertD.dismiss();

            }
        });

        /*alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);*/
        dialog.show();
    }

}
