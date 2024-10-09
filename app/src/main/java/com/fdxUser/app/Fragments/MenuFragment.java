package com.fdxUser.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.RestWiseCouponAdapter;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestWiseCouponModel;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    TextView tv_rest_name, tv_rest_address, tv_distance, tvRating, tvEstTime, tvDist;
    RatingBar rating;
    RecyclerView couponsRv;
    LinearLayout noCouponLL;
    List<RestWiseCouponModel> couponList = new ArrayList<>();
    RestWiseCouponAdapter restWiseCouponAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        tv_rest_name = view.findViewById(R.id.tv_rest_name);
        tv_rest_address = view.findViewById(R.id.tv_rest_address);
        tv_distance = view.findViewById(R.id.tv_distance);
        tvRating = view.findViewById(R.id.tvRating);
        tvEstTime = view.findViewById(R.id.tvEstTime);
        tvDist = view.findViewById(R.id.tvDist);
        couponsRv = view.findViewById(R.id.couponsRv);
        noCouponLL = view.findViewById(R.id.noCouponLL);

        couponList = Constants.restWiseCoupons;

        tv_rest_name.setText(Constants.restaurantModel.name);
        tv_rest_address.setText(Constants.restaurantModel.address);
        //tv_distance.setText(Constants.restaurantModel.estimated_delivery_time + " mins");
        tvEstTime.setText(Constants.restaurantModel.estimated_delivery_time + " mins");

        if (couponList.size()>0){
            couponsRv.setVisibility(View.VISIBLE);
            noCouponLL.setVisibility(View.GONE);
            restWiseCouponAdapter = new RestWiseCouponAdapter(getActivity(), couponList);
            couponsRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            couponsRv.setAdapter(restWiseCouponAdapter);

        }else {
            couponsRv.setVisibility(View.GONE);
            noCouponLL.setVisibility(View.VISIBLE);
        }




        return view;
    }
}
