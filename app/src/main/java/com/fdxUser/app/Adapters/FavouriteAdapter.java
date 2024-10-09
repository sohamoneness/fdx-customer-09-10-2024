package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OtherScreens.FavouriteActivity;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.FavouriteModels.AddFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.AddFavResponseModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavRequestModel;
import com.fdxUser.app.Models.FavouriteModels.RemoveFavResponseModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.Hold> {

    List<RestaurantModel> popList;
    Context context;
    SpecialDishAdapter specialDishAdapter;
    Prefs prefs;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;

    public FavouriteAdapter(FavouriteActivity favouriteActivity, List<RestaurantModel> favRestList) {
        this.context = favouriteActivity;
        this.popList = favRestList;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_rest_row, parent, false);
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_popular_rest_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        prefs = new Prefs(context);
        dialogView = new DialogView();

        RestaurantModel nbrM = popList.get(position);

        holder.tvPopRestName.setText(nbrM.name);
        Glide.with(context).load(nbrM.image).into(holder.iv_pop_rest);
        holder.tvPopRestAddress.setText(nbrM.address);
        holder.tvEstTime.setText(nbrM.estimated_delivery_time + " mins");
        holder.tvCuisines.setText(nbrM.special_items_string);

        specialDishAdapter = new SpecialDishAdapter(context, nbrM.special_items);
        //holder.specialRv.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        // holder.specialRv.setLayoutManager(new GridLayoutManager(context, 5));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        holder.specialRv.setLayoutManager(layoutManager);
        holder.specialRv.setAdapter(specialDishAdapter);
        holder.tvDist.setText(nbrM.distance);
        //holder.tvRating.setText(nbrM.rating);

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

        if (nbrM.is_favorite.equals("1")){
            holder.ivFav.setImageResource(R.drawable.ic_fav_fill);
        }else {
            holder.ivFav.setImageResource(R.drawable.ic_fav_outline);
        }

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
            public void onClick(View view) {
                Constants.TAG = 0;
                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra(Constants.REST_IMG, nbrM.image);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.iv_pop_rest,"iv"));
                intent.putExtra(Constants.REST_ID, nbrM.id);
                context.startActivity(intent, options.toBundle());
            }
        });

        holder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nbrM.is_favorite.equals("1")){
                    RemoveFavRequestModel removeFavRequestModel = new RemoveFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            nbrM.id
                    );
                    removeAsFav(removeFavRequestModel, holder);
                }else{
                    AddFavRequestModel addFavRequestModel = new AddFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            nbrM.id
                    );
                    addAsFav(addFavRequestModel, holder);
                }
            }
        });

        //holder.ratingBar.setRating(nbrM.getRating());

    }

    private void removeAsFav(RemoveFavRequestModel removeFavRequestModel, Hold holder) {
        dialogView.showCustomSpinProgress(context);
        manager.service.removeFav(removeFavRequestModel).enqueue(new Callback<RemoveFavResponseModel>() {
            @Override
            public void onResponse(Call<RemoveFavResponseModel> call, Response<RemoveFavResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RemoveFavResponseModel rfrm = response.body();
                    if (!rfrm.error){
                        //ivFav.setImageResource(R.drawable.ic_fav_fill);
                        holder.ivFav.setImageResource(R.drawable.ic_fav_outline);
                        Intent i = new Intent(((Activity)context), DashboardHome.class);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).overridePendingTransition(0, 0);
                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RemoveFavResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }

    private void addAsFav(AddFavRequestModel addFavRequestModel, Hold holder) {
        dialogView.showCustomSpinProgress(context);
        manager.service.addFav(addFavRequestModel).enqueue(new Callback<AddFavResponseModel>() {
            @Override
            public void onResponse(Call<AddFavResponseModel> call, Response<AddFavResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AddFavResponseModel afrm = response.body();
                    if (!afrm.error){
                        holder.ivFav.setImageResource(R.drawable.ic_fav_fill);
                        Intent i = new Intent(((Activity)context), DashboardHome.class);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).overridePendingTransition(0, 0);
                    }else {

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddFavResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }

    @Override
    public int getItemCount() {
        return popList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvPopRestName, tvPopRestAddress, tvEstTime, tvRating, tvDist, tvCuisines,tvOrderStat;
        RatingBar ratingBar;
        ShapeableImageView iv_pop_rest;
        RecyclerView specialRv;
        LinearLayout mainLL;
        ImageView ivFav;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvPopRestAddress = itemView.findViewById(R.id.tv_pop_rest_address);
            tvPopRestName = itemView.findViewById(R.id.tv_pop_rest_name);
            ratingBar = itemView.findViewById(R.id.rating);
            iv_pop_rest = itemView.findViewById(R.id.iv_pop_rest);
            tvEstTime = itemView.findViewById(R.id.tvEstTime);
            specialRv = itemView.findViewById(R.id.specialRv);
            tvDist = itemView.findViewById(R.id.tvDist);
            tvRating = itemView.findViewById(R.id.tvRating);
            mainLL = itemView.findViewById(R.id.mainLL);
            tvCuisines = itemView.findViewById(R.id.tvCuisines);
            ivFav = itemView.findViewById(R.id.ivFav);
            tvOrderStat = itemView.findViewById(R.id.tvOrderStat);

        }
    }
}
