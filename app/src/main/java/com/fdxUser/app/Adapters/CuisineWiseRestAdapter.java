package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.CustomFonts.ManropeSingleLineBoldTV;
import com.fdxUser.app.Network.Constants;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.imageview.ShapeableImageView;
import com.fdxUser.app.Activity.RestaurantScreens.CuisinesWiseRestaurantActivity;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.R;

import java.text.DecimalFormat;
import java.util.List;

public class CuisineWiseRestAdapter extends RecyclerView.Adapter<CuisineWiseRestAdapter.Hold> {

    Context context;
    List<RestaurantModel> popList;
    SpecialDishAdapter specialDishAdapter;

    public CuisineWiseRestAdapter(CuisinesWiseRestaurantActivity cuisinesWiseRestaurantActivity, List<RestaurantModel> restList) {
        this.context = cuisinesWiseRestaurantActivity;
        this.popList = restList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_popular_rest_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        RestaurantModel nbrM = popList.get(position);

        holder.tvPopRestName.setText(nbrM.name);
        Glide.with(context).load(nbrM.image).into(holder.iv_pop_rest);
        holder.tvPopRestAddress.setText(nbrM.address);
        holder.tvEstTime.setText(nbrM.estimated_delivery_time + " mins");
        if (nbrM.rating.equals("")){
            holder.tvRating.setText("0");
        }else {
            //holder.tvRating.setText(nbrM.rating);
            DecimalFormat formatter = new DecimalFormat("#,##,###.0");
            String formatted = "0";
            if(!nbrM.rating.equals("0")){
                formatted = formatter.format(Double.parseDouble(nbrM.rating));
            }else{
                formatted = "0";
            }

            holder.tvRating.setText(formatted);
        }

        specialDishAdapter = new SpecialDishAdapter(context, nbrM.special_items);
        holder.tvCuisines.setText(nbrM.special_items_string);
        //holder.specialRv.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        // holder.specialRv.setLayoutManager(new GridLayoutManager(context, 5));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        holder.specialRv.setLayoutManager(layoutManager);
        holder.specialRv.setAdapter(specialDishAdapter);

        if (Constants.orderCount.equals("0")){
            holder.tvOrderStat.setText("₹0 delivery fee, first order");
        }else {
            //holder.tvOrderStat.setText(Html.fromHtml(nbrM.description));
            //holder.tvOrderStat.setText(nbrM.description);
            if (nbrM.description != null) {
                if (!nbrM.description.equals("")) {
                    holder.tvOrderStat.setText(Html.fromHtml(nbrM.description));
                } else {
                    holder.tvOrderStat.setText("");
                }
            }else {
                holder.tvOrderStat.setText("");
            }
        }

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra(Constants.REST_IMG, nbrM.image);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.iv_pop_rest,"iv"));
                intent.putExtra(Constants.REST_ID, nbrM.id);
                context.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return popList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvPopRestName, tvPopRestAddress, tvEstTime, tvCuisines,tvRating;
        ManropeSingleLineBoldTV tvOrderStat;
        RatingBar ratingBar;
        ShapeableImageView iv_pop_rest;
        RecyclerView specialRv;
        LinearLayout mainLL;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvPopRestAddress = itemView.findViewById(R.id.tv_pop_rest_address);
            tvPopRestName = itemView.findViewById(R.id.tv_pop_rest_name);
            ratingBar = itemView.findViewById(R.id.rating);
            iv_pop_rest = itemView.findViewById(R.id.iv_pop_rest);
            tvEstTime = itemView.findViewById(R.id.tvEstTime);
            specialRv = itemView.findViewById(R.id.specialRv);
            mainLL = itemView.findViewById(R.id.mainLL);
            tvCuisines = itemView.findViewById(R.id.tvCuisines);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvOrderStat = itemView.findViewById(R.id.tvOrderStat);
        }
    }
}
