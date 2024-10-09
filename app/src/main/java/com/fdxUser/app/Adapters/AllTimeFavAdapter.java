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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.CustomFonts.ManropeSingleLineBoldTV;
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
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTimeFavAdapter extends RecyclerView.Adapter<AllTimeFavAdapter.Hold> {

    List<RestaurantModel> nbList;
    Context context;
    Prefs prefs;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;


    public AllTimeFavAdapter(DashboardHome dashboardHome, List<RestaurantModel> nearbyRestList) {
        this.context = dashboardHome;
        this.nbList = nearbyRestList;
    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_rest_row, parent, false);
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_choice_order_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {
        prefs = new Prefs(context);
        dialogView = new DialogView();


        RestaurantModel nbrM = nbList.get(position);

        /*if (position == nbList.size()-1){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams. WRAP_CONTENT ,
                    LinearLayout.LayoutParams. WRAP_CONTENT ) ;
            layoutParams.setMargins( 48 , 24 , 48 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainLL.setLayoutParams(layoutParams);
        }*/
        if (nbrM.is_favorite.equals("1")) {
            holder.ivFav.setImageResource(R.drawable.ic_fav_fill);
        } else {
            holder.ivFav.setImageResource(R.drawable.ic_fav_outline);
        }

        holder.tvRestName.setText(nbrM.name);
        holder.tvRestAdr.setText(nbrM.address);
        Glide.with(context).load(nbrM.image).into(holder.ivRest);
        //if (Constants.orderCount.equals("0")) {
           // holder.tvOrderStat.setText("₹0 delivery fee, first order");
        //} else {
            if (nbrM.description != null) {
                if (!nbrM.description.equals("")) {
                    holder.tvOrderStat.setText(Html.fromHtml(nbrM.description));
                } else {
                    holder.tvOrderStat.setText("");
                }
            }else {
                holder.tvOrderStat.setText("");
            }

            //holder.tvOrderStat.setText(nbrM.description);
       // }
        //holder.ratingBar.setRating(4.5f);
        holder.tvTime.setText(nbrM.estimated_delivery_time + " mins");
        //holder.tvRating.setText(nbrM.rating);
        holder.tvDist.setText(nbrM.distance);
        if (nbrM.rating.equals("")) {
            holder.tvRating.setText("0");
        } else {
            //holder.tvRating.setText(nbrM.rating);
            DecimalFormat formatter = new DecimalFormat("#,##,###.0");
            String formatted = "0";
            if (!nbrM.rating.equals("0")) {
                formatted = formatter.format(Double.parseDouble(nbrM.rating));
            } else {
                formatted = "0";
            }

            holder.tvRating.setText(formatted);
        }

        holder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nbrM.is_favorite.equals("1")) {
                    RemoveFavRequestModel removeFavRequestModel = new RemoveFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            nbrM.id
                    );
                    removeAsFav(removeFavRequestModel, holder);
                } else {
                    AddFavRequestModel addFavRequestModel = new AddFavRequestModel(
                            prefs.getData(Constants.USER_ID),
                            nbrM.id
                    );
                    addAsFav(addFavRequestModel, holder);
                }
            }
        });

        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.TAG = 0;
                Intent intent = new Intent(context, RestaurantDetails.class);
                intent.putExtra(Constants.REST_IMG, nbrM.image);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, new Pair<View, String>(holder.ivRest, "iv"));
                intent.putExtra(Constants.REST_ID, nbrM.id);
                context.startActivity(intent, options.toBundle());
            }
        });

    }

    private void removeAsFav(RemoveFavRequestModel removeFavRequestModel, Hold holder) {
        dialogView.showCustomSpinProgress(context);
        manager.service.removeFav(removeFavRequestModel).enqueue(new Callback<RemoveFavResponseModel>() {
            @Override
            public void onResponse(Call<RemoveFavResponseModel> call, Response<RemoveFavResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RemoveFavResponseModel rfrm = response.body();
                    if (!rfrm.error) {
                        //ivFav.setImageResource(R.drawable.ic_fav_fill);
                        holder.ivFav.setImageResource(R.drawable.ic_fav_outline);
                        Intent i = new Intent(((Activity) context), DashboardHome.class);
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(0, 0);
                        ((Activity) context).startActivity(i);
                        ((Activity) context).overridePendingTransition(0, 0);
                    } else {

                    }
                } else {
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
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    AddFavResponseModel afrm = response.body();
                    if (!afrm.error) {
                        holder.ivFav.setImageResource(R.drawable.ic_fav_fill);
                        Intent i = new Intent(((Activity) context), DashboardHome.class);
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(0, 0);
                        ((Activity) context).startActivity(i);
                        ((Activity) context).overridePendingTransition(0, 0);
                    } else {

                    }
                } else {
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
        return nbList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvRestName, tvRestAdr, tvTime, tvDist, tvRating;
        ManropeSingleLineBoldTV tvOrderStat;
        // RatingBar ratingBar;
        ShapeableImageView ivRest;
        LinearLayout mainLL;
        ImageView ivFav;

        public Hold(@NonNull View itemView) {
            super(itemView);

            //tvRestName = itemView.findViewById(R.id.tv_rest_name);
            tvRestName = itemView.findViewById(R.id.tv_pop_rest_name);
            tvRestAdr = itemView.findViewById(R.id.tv_pop_rest_address);
            //ratingBar = itemView.findViewById(R.id.rating);
            //ivRest = itemView.findViewById(R.id.ivRest);
            ivRest = itemView.findViewById(R.id.iv_pop_rest);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDist = itemView.findViewById(R.id.tvDist);
            tvRating = itemView.findViewById(R.id.tvRating);
            mainLL = itemView.findViewById(R.id.mainLL);
            ivFav = itemView.findViewById(R.id.ivFav);
            tvOrderStat = itemView.findViewById(R.id.tvOrderStat);


        }
    }
}
