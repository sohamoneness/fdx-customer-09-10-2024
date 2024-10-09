package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.CheckOutActivity;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.R;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutOrderAdapter extends RecyclerView.Adapter<CheckoutOrderAdapter.Hold> {

    List<CartsModel> ciList;
    Context context;
    double totPrice = 0.0;


    public CheckoutOrderAdapter(CheckOutActivity checkOutActivity, List<CartsModel> cartItemList) {
        this.context = checkOutActivity;
        this.ciList = cartItemList;
    }

    @NonNull
    @Override
    public CheckoutOrderAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_checkout_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutOrderAdapter.Hold holder, int position) {

        CartsModel cim = ciList.get(position);

        if (position == (ciList.size()-1)){
            holder.dotView.setVisibility(View.GONE);
        }else{
            holder.dotView.setVisibility(View.VISIBLE);
        }

        holder.tvItemName.setText(cim.product_name);
        if (cim.add_on_id.equals("0") && cim.add_on_id2.equals("0")){
            holder.tvExtraTxt.setVisibility(View.GONE);
            totPrice = Double.parseDouble(cim.price) * Integer.parseInt(cim.quantity);
        }else if (!cim.add_on_id.equals("0") && cim.add_on_id2.equals("0")){
            holder.tvExtraTxt.setVisibility(View.VISIBLE);
            holder.tvExtraTxt.setText("With Add-ons: " + cim.add_on_name);
            totPrice = (Double.parseDouble(cim.price) + Double.parseDouble(cim.add_on_price)) * Integer.parseInt(cim.quantity);
        }else if (!cim.add_on_id.equals("0") && !cim.add_on_id2.equals("0")){
            holder.tvExtraTxt.setVisibility(View.VISIBLE);
            holder.tvExtraTxt.setText("With Add-ons: " + cim.add_on_name + " & " + cim.add_on_name2);
            totPrice = (Double.parseDouble(cim.price) + Double.parseDouble(cim.add_on_price) +
                    Double.parseDouble(cim.add_on_price2)) * Integer.parseInt(cim.quantity);

        }
        //holder.tvExtraTxt.setText(Html.fromHtml(cim.product_description));
        //holder.tvExtraTxt.setVisibility(View.GONE);
        double itemPrice = Double.parseDouble(cim.price) + Double.parseDouble(cim.add_on_price) + Double.parseDouble(cim.add_on_price2);

       // holder.tvItemPrice.setText("\u20B9 " + String.valueOf(itemPrice));
        holder.tvPrice.setText("\u20B9 " + String.valueOf(itemPrice));
        //if (cim.extra_price.equals("0")){
         //   holder.tvExtraPrice.setVisibility(View.GONE);
        //}else{
            //holder.tvExtraPrice.setVisibility(View.VISIBLE);
            //holder.tvExtraPrice.setText("\u20B9 " + cim.extra_price);
       // }

        //double totPrice = Double.parseDouble(cim.price) * Integer.parseInt(cim.quantity);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted = formatter1.format(totPrice);
        holder.tvTotPrice.setText("\u20B9 " + formatted);
        holder.tvQty.setText(cim.quantity + " X ");

        holder.ivMinus.setVisibility(View.INVISIBLE);
        holder.ivPlus.setVisibility(View.INVISIBLE);

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* int q = Integer.parseInt(holder.tvQty.getText().toString());
                if (q > 1){
                    q = q-1;
                    holder.tvQty.setText(String.valueOf(q));
                }else{
                    Toast.makeText(context, "Can't reduce!", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*int q = Integer.parseInt(holder.tvQty.getText().toString());
                q = q+1;
                holder.tvQty.setText(String.valueOf(q));*/
            }
        });



    }

    @Override
    public int getItemCount() {
        return ciList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName, tvExtraTxt, tvPrice, tvQty;
        TextView tvTotPrice, tvExtraPrice;
        ImageView ivMinus, ivPlus;
        View dotView;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotPrice = itemView.findViewById(R.id.tvTotPrice);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvExtraPrice = itemView.findViewById(R.id.tvExtraPrice);
            dotView = itemView.findViewById(R.id.dotView);

        }
    }
}
