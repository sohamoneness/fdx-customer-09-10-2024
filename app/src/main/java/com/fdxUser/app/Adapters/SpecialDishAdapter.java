package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.R;

import java.util.List;

public class SpecialDishAdapter extends RecyclerView.Adapter<SpecialDishAdapter.Hold> {

    List<SpecialItemModel> spiList;
    Context context;

    public SpecialDishAdapter(Context context, List<SpecialItemModel> special_items) {
        this.context = context;
        this.spiList = special_items;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.special_dish_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {
        SpecialItemModel sim = spiList.get(position);
        holder.tvSpecialDish.setText(sim.name);
    }

    @Override
    public int getItemCount() {
        return spiList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvSpecialDish;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvSpecialDish = itemView.findViewById(R.id.tvSpecialDish);

        }
    }
}
