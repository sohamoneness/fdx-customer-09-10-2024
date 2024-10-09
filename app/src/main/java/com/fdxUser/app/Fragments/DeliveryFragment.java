package com.fdxUser.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.Prefs;

public class DeliveryFragment extends Fragment {

    Prefs prefs;
    TextView tv_cust_name, tv_cust_address;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_fragment, container, false);

        prefs = new Prefs(getActivity());

        tv_cust_address = view.findViewById(R.id.tv_cust_address);
        tv_cust_name = view.findViewById(R.id.tv_cust_name);

        tv_cust_name.setText(prefs.getData(Constants.USER_NAME));
        tv_cust_address.setText(prefs.getData(Constants.USER_ADDRESS) +",\n" +prefs.getData(Constants.USER_CITY));

        return view;
    }
}
