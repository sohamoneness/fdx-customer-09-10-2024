package com.fdxUser.app.Utills.SliderHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OtherScreens.AllCouponsActivity;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestWiseCouponModel;
import com.fdxUser.app.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    //List<SliderDataModel> mSliderItems;
    List<RestWiseCouponModel> mSliderItems;
    Context context;

    // Constructor

    public SliderAdapter(FragmentActivity activity, List<RestWiseCouponModel> sliderImgList) {
        this.context = activity;
        this.mSliderItems = sliderImgList;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_row, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final RestWiseCouponModel sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(mSliderItems.get(position).image)
                .fitCenter()
                .into(viewHolder.slideImg);

        viewHolder.slideImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).startActivity(new Intent((Activity)context, AllCouponsActivity.class));
                ((Activity)context).finish();
            }
        });

    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ShapeableImageView slideImg;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            slideImg = itemView.findViewById(R.id.slideImg);
            this.itemView = itemView;
        }
    }
}
