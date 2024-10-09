package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Models.OrderSummeryModels.OrderItemModel;
import com.fdxUser.app.R;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.Hold> {

    Context mContext;
    List<OrderItemModel> itemList;
    String from = "";

    public OrderItemAdapter(Context context, List<OrderItemModel> orderItemModelList) {

        this.mContext = context;
        this.itemList = orderItemModelList;

    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        OrderItemModel oim = itemList.get(position);

        if (position == itemList.size()-1){
            holder.viewSep.setVisibility(View.GONE);
        }else {
            holder.viewSep.setVisibility(View.VISIBLE);
        }

        holder.tvItemName.setText(oim.product_name);
        double itemPrice = Double.parseDouble(oim.price) + Double.parseDouble(oim.add_on_price) + Double.parseDouble(oim.add_on_price2);
        holder.tvExtra.setText(oim.quantity + " X " + "\u20B9" + " " + String.valueOf(itemPrice));
        if (oim.add_on_quantity.equals("0") && oim.add_on_quantity2.equals("0")){
            holder.tvAddOn.setVisibility(View.GONE);
        }else {
            holder.tvAddOn.setVisibility(View.VISIBLE);
            if (!oim.add_on_quantity.equals("0") && !oim.add_on_quantity2.equals("0")){
                holder.tvAddOn.setText("Add-ons: " + oim.add_on_name + " & " + oim.add_on_name2);
            }else if (!oim.add_on_quantity.equals("0") && oim.add_on_quantity2.equals("0")){
                holder.tvAddOn.setText("Add-ons: " + oim.add_on_name);
            }
        }
        //holder.tvExtra.setVisibility(View.GONE);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format((Double.parseDouble(oim.price) +
                Double.parseDouble(oim.add_on_price) + Double.parseDouble(oim.add_on_price2))
                * Double.parseDouble(oim.quantity));
        holder.tvAmount.setText("\u20B9" + " " + formatted);

       /* if (oim.isVeg == true){
            holder.ivVeg.setVisibility(View.VISIBLE);
            holder.ivNonVeg.setVisibility(View.GONE);
        }else{
            holder.ivVeg.setVisibility(View.GONE);
            holder.ivNonVeg.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        ImageView ivVeg, ivNonVeg;
        TextView tvItemName, tvExtra, tvAmount, tvAddOn;
        View viewSep;

        public Hold(@NonNull View itemView) {
            super(itemView);

            ivVeg = itemView.findViewById(R.id.iv_veg);
            ivNonVeg = itemView.findViewById(R.id.iv_non_veg);
            tvItemName = itemView.findViewById(R.id.tv_food_name);
            tvExtra = itemView.findViewById(R.id.tv_extra);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            viewSep = itemView.findViewById(R.id.viewSep);
            tvAddOn = itemView.findViewById(R.id.tvAddOn);

        }
    }
}
