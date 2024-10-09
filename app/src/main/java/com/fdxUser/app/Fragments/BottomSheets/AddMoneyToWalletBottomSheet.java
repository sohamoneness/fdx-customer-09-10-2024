package com.fdxUser.app.Fragments.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fdxUser.app.Activity.WalletModule.WalletActivity;
import com.fdxUser.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddMoneyToWalletBottomSheet extends BottomSheetDialogFragment  {

    ImageView ivClose;
    TextView tv1, tv2, tv3, tv4;
    EditText etAmount;
    Button btnAddMoney;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_money_to_wallet_bottomsheet, container, false);



        btnAddMoney = v.findViewById(R.id.btnAddMoney);
        ivClose = v.findViewById(R.id.ivClose);
        etAmount = v.findViewById(R.id.etAmount);
        tv1 = v.findViewById(R.id.tv1);
        tv2 = v.findViewById(R.id.tv2);
        tv3 = v.findViewById(R.id.tv3);
        tv4 = v.findViewById(R.id.tv4);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tv1.getText());
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tv2.getText());
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tv3.getText());
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tv4.getText());
            }
        });

        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etAmount.getText().toString().equals("")){
                    String str = etAmount.getText().toString();
                    if (str.contains("\u20B9")){
                        String newStr = str.substring(2, str.length());
                        String[] ss = str.split("\\s");
                        newStr = ss[1].toString();
                        /*Toast.makeText(getActivity(), newStr+">>", Toast.LENGTH_SHORT).show();*/
                        ((WalletActivity) getActivity()).openRazorPay(newStr);
                        dismiss();
                    }else{
                        ((WalletActivity) getActivity()).openRazorPay(etAmount.getText().toString());
                        dismiss();
                    }


                }
            }
        });


        return v;
    }


}
