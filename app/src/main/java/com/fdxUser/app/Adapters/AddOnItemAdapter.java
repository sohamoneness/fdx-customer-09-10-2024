package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.CustomFonts.ManropeRegularTextView;
import com.fdxUser.app.Models.RestaurantDetailsModels.AddOnModels;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;

import java.util.List;

public class AddOnItemAdapter extends RecyclerView.Adapter<AddOnItemAdapter.Hold> {

    List<AddOnModels> aoList;
    Context context;
    int price = 0;
    int flag = 0;
    public AddOnItemAdapter(FragmentActivity activity, List<AddOnModels> addOnItemList, int price) {
        this.context = activity;
        this.aoList = addOnItemList;
        this.price = price;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_menu_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        AddOnModels aoM = aoList.get(position);

        holder.tvAddOnItem.setText(aoM.name);
        holder.tvAddOnPrice.setText("\u20B9 "+aoM.price);
        //Constants.priceWithAddOn = price;

        if (aoM.is_veg.equals("0")){
            holder.ivType.setImageDrawable(context.getDrawable(R.drawable.icon_nonveg));
        }else{
            holder.ivType.setImageDrawable(context.getDrawable(R.drawable.icon_veg));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();

                    if (checked){
                        if (flag == 2){
                            Toast.makeText(context, "Only 2 add-ons can be added!", Toast.LENGTH_SHORT).show();
                            holder.checkBox.setChecked(false);
                        }else {
                            holder.checkBox.setChecked(true);
                            aoM.tag = 1;
                            flag = flag + 1;
                            Constants.priceWithAddOn = Constants.priceWithAddOn + Integer.parseInt(aoM.price);
                        }
                    }else{
                        Constants.priceWithAddOn = Constants.priceWithAddOn - Integer.parseInt(aoM.price);
                        holder.checkBox.setChecked(false);
                        flag = flag - 1;
                        aoM.tag = 0;
                    }


            }
        });


    }

    @Override
    public int getItemCount() {
        return aoList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        ManropeRegularTextView tvAddOnItem, tvAddOnPrice;
        ImageView ivType;

        public Hold(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            tvAddOnItem = itemView.findViewById(R.id.tvAddOnItem);
            tvAddOnPrice = itemView.findViewById(R.id.tvAddOnPrice);
            ivType = itemView.findViewById(R.id.ivType);

        }
    }
}
