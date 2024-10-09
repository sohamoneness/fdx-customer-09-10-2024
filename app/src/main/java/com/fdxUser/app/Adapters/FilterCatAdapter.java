package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;
import com.fdxUser.app.R;

import java.util.List;

public class FilterCatAdapter extends RecyclerView.Adapter<FilterCatAdapter.Hold> {
    List<ItemCategoryModel> icList;
    Context context;

    public FilterCatAdapter(RestaurantDetails restaurantDetails, List<ItemCategoryModel> categoryList) {
        this.context = restaurantDetails;
        this.icList = categoryList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_cat_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        ItemCategoryModel icm = icList.get(position);

        holder.tvCategory.setText(icm.title);

    }

    @Override
    public int getItemCount() {
        return icList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        RelativeLayout mainRL;
        TextView tvCategory;
        public Hold(@NonNull View itemView) {
            super(itemView);
            mainRL = itemView.findViewById(R.id.mainRL);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
