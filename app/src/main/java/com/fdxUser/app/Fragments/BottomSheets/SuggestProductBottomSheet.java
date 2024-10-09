package com.fdxUser.app.Fragments.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fdxUser.app.Models.SuggestionModels.SuggestionRequestModel;
import com.fdxUser.app.Models.SuggestionModels.SuggestionResponseModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuggestProductBottomSheet extends BottomSheetDialogFragment {

    ImageView ivClose;
    Button btnSend;
    EditText etSuggestions;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    Spinner spType;
    List<String> spList = new ArrayList<>();
    String sugType = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.suggest_product_bottomsheet, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        ivClose = v.findViewById(R.id.ivClose);
        btnSend = v.findViewById(R.id.btnSend);
        etSuggestions = v.findViewById(R.id.etSuggestions);
        spType = v.findViewById(R.id.spType);

        spList.add("Select");
        spList.add("Restaurant");
        spList.add("Food Item");

        ArrayAdapter<String> spinnerPhoneArrayAdapter3 = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner_row, spList);
        spinnerPhoneArrayAdapter3.setDropDownViewResource(R.layout.my_spinner_row);
        spType.setAdapter(spinnerPhoneArrayAdapter3);
        spType.setFocusableInTouchMode(false);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spList.get(position).equals("Select")){
                    //Toast.makeText(getActivity(), "Please select an option", Toast.LENGTH_SHORT).show();
                    sugType = "NA";

                }else if (spList.get(position).equals("Restaurant")){
                    sugType = "1";
                }else {
                    sugType = "0";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sugType.equals("NA")){
                    dialogView.errorButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Suggestion type is needed");
                }else if (etSuggestions.getText().toString().equals("")){
                    dialogView.errorButtonDialog(getActivity(), getResources().getString(R.string.app_name), "Enter your suggestion");
                }else {
                    SuggestionRequestModel suggestionRequestModel = new SuggestionRequestModel(
                            prefs.getData(Constants.USER_ID),
                            sugType,
                            etSuggestions.getText().toString().trim()
                    );
                    submitSuggestions(suggestionRequestModel);

                }
            }
        });

        return v;
    }

    private void submitSuggestions(SuggestionRequestModel suggestionRequestModel) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.submitSuggestion(suggestionRequestModel).enqueue(new Callback<SuggestionResponseModel>() {
            @Override
            public void onResponse(Call<SuggestionResponseModel> call, Response<SuggestionResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    SuggestionResponseModel srm = response.body();
                    if (!srm.error){
                        Toast.makeText(getActivity(), "Suggestion submitted successfully.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Something went wrong, Suggestion not submitted. Please try again!", Toast.LENGTH_SHORT).show();
                        //dismiss();
                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(getActivity(), "Server error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuggestionResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(getActivity(), "Network error! Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
