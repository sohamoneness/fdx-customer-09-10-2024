package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Activity.RestaurantScreens.Dashboard;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.RestaurantScreens.ShowAllScreen;
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.R;

import java.util.List;

public class DifferentCatAdapter extends RecyclerView.Adapter<DifferentCatAdapter.Hold> {

    List<CuisinesModel> dcList;
    Context context;
    int limit = 0;

    public DifferentCatAdapter(Dashboard dashboard, List<CuisinesModel> nearbyRestList) {
        this.context = dashboard;
        this.dcList = nearbyRestList;
    }

    public DifferentCatAdapter(ShowAllScreen showAllScreen, List<CuisinesModel> allCatList) {
        this.context = showAllScreen;
        this.dcList = allCatList;
    }

    public DifferentCatAdapter(DashboardHome dashboardHome, List<CuisinesModel> cuisinesList, int i) {
        this.context = dashboardHome;
        this.dcList = cuisinesList;
        this.limit = i;
    }

    public DifferentCatAdapter(SearchActivity searchActivity, List<CuisinesModel> catList, int i) {
        this.context = searchActivity;
        this.dcList = catList;
        this.limit = i;
    }


    @NonNull
    @Override
    public DifferentCatAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dif_category_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DifferentCatAdapter.Hold holder, int position) {

        CuisinesModel dcM = dcList.get(position);

        holder.tvCategory.setText(dcM.title);
        holder.tvCategory.setSingleLine(true);
        Glide.with(context).load(dcM.image).into(holder.ivCategory);


    }

    @Override
    public int getItemCount() {
        if (limit>0){
            return limit;
        }else {
            return dcList.size();
        }

    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvCategory;
        ImageView ivCategory;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_cat);
            ivCategory = itemView.findViewById(R.id.iv_cat);

        }
    }
}
