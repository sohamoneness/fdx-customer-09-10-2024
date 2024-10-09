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
import com.fdxUser.app.Models.DashboardModels.CuisinesModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.R;

import java.util.List;

public class SearchCuisineAdapter extends RecyclerView.Adapter<SearchCuisineAdapter.Hold> {
    List<RestaurantModel> rList;
    List<CuisinesModel> cList;
    Context context;
    int limit = 0;
    String isFor = "";




    public SearchCuisineAdapter(SearchActivity searchActivity, List<CuisinesModel> catList, List<RestaurantModel> nearRestList, int i, String rest) {
        this.context = searchActivity;
        this.cList = catList;
        this.rList = nearRestList;
        this.limit = i;
        this.isFor = rest;
    }

    @NonNull
    @Override
    public SearchCuisineAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCuisineAdapter.Hold holder, int position) {

        if (cList.size()>0){
            CuisinesModel cm = cList.get(position);
            //RestaurantModel rm = rList.get(position);

            if (isFor.equals("cat")){
                holder.tvItem.setText(cm.title);
                Glide.with(context).load(R.drawable.cuisine_icon).into(holder.ivIcon);

            }
        }

        if (rList.size()>0){
            //CuisinesModel cm = cList.get(position);
            RestaurantModel rm = rList.get(position);

            if (!isFor.equals("cat")){
                holder.tvItem.setText(rm.name);
                Glide.with(context).load(R.drawable.search_icon).into(holder.ivIcon);
            }
        }



    }

    @Override
    public int getItemCount() {
        return limit;
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvItem;
        ImageView ivIcon;
        public Hold(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}
