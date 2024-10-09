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
import com.fdxUser.app.Models.CouponModels.DeliveryCouponsListModel;
import com.fdxUser.app.R;

import java.util.List;

public class DelCouponListAdapter extends RecyclerView.Adapter<DelCouponListAdapter.Hold> {

    Context context;
    List<DeliveryCouponsListModel> cList;

    public DelCouponListAdapter(CouponActivity couponActivity, List<DeliveryCouponsListModel> delCouponList) {
        this.cList = delCouponList;
        this.context = couponActivity;
    }


    @NonNull
    @Override
    public DelCouponListAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_coupon_list, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DelCouponListAdapter.Hold holder, int position) {

        DeliveryCouponsListModel rwcm = cList.get(position);
        int p0 = 0, p1 = 1, p2 = 2;

        holder.code.setText(rwcm.code);
        int left = Integer.parseInt(rwcm.maximum_time_user_can_use) - Integer.parseInt(rwcm.user_used);
        holder.tv_left.setText(String.valueOf(left)+" left");
        holder.description.setText(Html.fromHtml(rwcm.description));

        if (rwcm.offer_type.equals("1")){
            holder.type.setText(rwcm.rate+"% OFF");
            holder.description2.setText("Use code "+ rwcm.code+" & get "+rwcm.rate+"% off on your orders.");
        }else {
            holder.type.setText("FLAT OFF");
            holder.description2.setText("Use code "+ rwcm.code+" & get a flat discount of ₹"+rwcm.rate+" on your orders.");
        }

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
