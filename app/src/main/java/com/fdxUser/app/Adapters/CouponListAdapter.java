package com.fdxUser.app.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.OtherScreens.CouponActivity;
import com.fdxUser.app.Models.CouponModels.CouponListModel;
import com.fdxUser.app.R;

import java.util.List;

public class CouponListAdapter extends RecyclerView.Adapter<CouponListAdapter.Hold> {

    Context context;
    List<CouponListModel> cList;

    public CouponListAdapter(CouponActivity couponActivity, List<CouponListModel> couponList) {
        this.cList = couponList;
        this.context = couponActivity;
    }

    @NonNull
    @Override
    public CouponListAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_coupon_list, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponListAdapter.Hold holder, int position) {

        CouponListModel rwcm = cList.get(position);
        int p0 = 0, p1 = 1, p2 = 2;

        /*if (position == cList.size()-1){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams. WRAP_CONTENT,
                    RelativeLayout.LayoutParams. WRAP_CONTENT ) ;
            layoutParams.setMargins( 24 , 0 , 24 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainRL.setLayoutParams(layoutParams);
        }*/

        //holder.tvOffer.setText(rwcm.rate + "% OFF" + " up to ₹" + rwcm.maximum_offer_rate);
        //holder.tvMaxDiscount.setText();
        holder.code.setText(rwcm.code);
        int left = Integer.parseInt(rwcm.maximum_time_user_can_use) - Integer.parseInt(rwcm.user_used);
        holder.tv_left.setText(String.valueOf(left)+" left");
        holder.description.setText(Html.fromHtml(rwcm.description));

        if (rwcm.type.equals("1")){
            holder.type.setText(rwcm.rate+"% OFF");
            holder.description2.setText("Use code "+ rwcm.code+" & get "+rwcm.rate+"% off on your orders. Maximum discount - ₹"+rwcm.maximum_offer_rate);
        }else {
            holder.type.setText("FLAT OFF");
            holder.description2.setText("Use code "+ rwcm.code+" & get a flat discount of ₹"+rwcm.rate+" on your orders.");
        }
        //holder.tvValid.setText("Valid upto: " + rwcm.end_date);

//        if (cList.size() > 1){
//            if (position == 0) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            } else if (position == 1) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            }
//        }else if (cList.size() > 2){
//            if (position == 0) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            } else if (position == 1) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            } else if (position == 2) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            }
//        }else if (cList.size() > 3){
//            if (position == 0) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            } else if (position == 1) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            } else if (position == 2) {
//                Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//            }else {
//                if (position == p0+3){
//                    p0 = position;
//                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//                }else if (position == p1+3){
//                    p1 = position;
//                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//                }else if (position == p2+3){
//                    p2 = position;
//                    Glide.with(context).load(R.drawable.coupon_img).into(holder.ivBG);
//                }
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
//        TextView tvOffer, tvCode, tvValid, tvMaxDiscount;
//        ImageView ivBG;
//        RelativeLayout mainRL;
        TextView code, type, description, description2,tv_left;
        public Hold(@NonNull View itemView) {
            super(itemView);
//            tvOffer = itemView.findViewById(R.id.tvOffer);
//            tvCode = itemView.findViewById(R.id.tvCode);
//            tvValid = itemView.findViewById(R.id.tvValid);
//            tvMaxDiscount = itemView.findViewById(R.id.tvMaxDiscount);
//            ivBG = itemView.findViewById(R.id.ivBG);
//            mainRL = (RelativeLayout) itemView.findViewById(R.id.mainRL);

            code = itemView.findViewById(R.id.code);
            type = itemView.findViewById(R.id.type);
            description = itemView.findViewById(R.id.description);
            description2 = itemView.findViewById(R.id.description2);
            tv_left = itemView.findViewById(R.id.tv_left);
        }
    }
}
