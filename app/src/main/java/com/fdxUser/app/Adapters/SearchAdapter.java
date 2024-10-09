package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Models.SearchModels.SearchCustomModel;
import com.fdxUser.app.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Hold> {

    List<SearchCustomModel> sList;
    Context context;

    public SearchAdapter(SearchActivity searchActivity, List<SearchCustomModel> searchCustomList) {
        this.context = searchActivity;
        this.sList = searchCustomList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        SearchCustomModel searchCustomModel = sList.get(position);

        Glide.with(context).load(searchCustomModel.image).into(holder.ivProduct);
        holder.tvProduct.setText(searchCustomModel.name);


    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvProduct;
        CircleImageView ivProduct;

        public Hold(@NonNull View itemView) {
            super(itemView);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            ivProduct = itemView.findViewById(R.id.ivProduct);
        }
    }
}
