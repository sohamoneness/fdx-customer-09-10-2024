package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestWiseCouponModel;
import com.fdxUser.app.R;

import java.util.List;

public class RestWiseCouponAdapter extends RecyclerView.Adapter<RestWiseCouponAdapter.Hold> {

    Context context;
    List<RestWiseCouponModel> cList;

    public RestWiseCouponAdapter(FragmentActivity activity, List<RestWiseCouponModel> couponList) {

        this.context = activity;
        this.cList = couponList;

    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_list_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        RestWiseCouponModel rwcm = cList.get(position);
        int p0 = 0, p1 = 1, p2 = 2;

        /*if (position == cList.size()-1){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams. WRAP_CONTENT,
                    RelativeLayout.LayoutParams. WRAP_CONTENT ) ;
            layoutParams.setMargins( 24 , 0 , 24 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainRL.setLayoutParams(layoutParams);
        }*/

        holder.tvOffer.setText(rwcm.rate + "% OFF" + " up to ₹" + rwcm.maximum_offer_rate);
        //holder.tvMaxDiscount.setText();
        holder.tvCode.setText("USE CODE: " + rwcm.code);
        holder.tvValid.setText("Valid upto: " + rwcm.end_date);

        if (cList.size() > 1){
            if (position == 0) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            } else if (position == 1) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            }
        }else if (cList.size() > 2){
            if (position == 0) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            } else if (position == 1) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            } else if (position == 2) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            }
        }else if (cList.size() > 3){
            if (position == 0) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            } else if (position == 1) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            } else if (position == 2) {
                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
            }else {
                if (position == p0+3){
                    p0 = position;
                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
                }else if (position == p1+3){
                    p1 = position;
                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
                }else if (position == p2+3){
                    p2 = position;
                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
                }
            }
        }

        /*if (position == cList.size()-1){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins( 0 , 0 , 0 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainRL.setLayoutParams(layoutParams);
        }*/



    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvOffer, tvCode, tvValid, tvMaxDiscount;
        ImageView ivBG;
        RelativeLayout mainRL;

        public Hold(@NonNull View itemView) {
            super(itemView);
            tvOffer = itemView.findViewById(R.id.tvOffer);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvValid = itemView.findViewById(R.id.tvValid);
            tvMaxDiscount = itemView.findViewById(R.id.tvMaxDiscount);
            ivBG = itemView.findViewById(R.id.ivBG);
            mainRL = (RelativeLayout) itemView.findViewById(R.id.mainRL);

        }
    }
}
