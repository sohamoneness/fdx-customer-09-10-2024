package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Models.CartModels.AddToCartRequest;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.UpsellItemModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteMealAdapter extends RecyclerView.Adapter<CompleteMealAdapter.Hold> {

    List<UpsellItemModel> cmList;
    Context context;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;


    /*public CompleteMealAdapter(CartActivity cartActivity, List<CompleteMealModel> completeMealList) {
        this.cmList = completeMealList;
        this.context = cartActivity;
    }*/

    public CompleteMealAdapter(CartActivity cartActivity, List<UpsellItemModel> upsellItemList) {
        this.cmList = upsellItemList;
        this.context = cartActivity;
    }


    @NonNull
    @Override
    public CompleteMealAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_meal_row, parent, false);
        return new CompleteMealAdapter.Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteMealAdapter.Hold holder, int position) {
        prefs = new Prefs(context.getApplicationContext());
        dialogView = new DialogView();

        UpsellItemModel cmm = cmList.get(position);

        /*if (position == cmList.size()-1){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    428,
                    LinearLayout.LayoutParams. MATCH_PARENT ) ;
            layoutParams.setMargins( 48 , 0 , 48 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainLL.setLayoutParams(layoutParams);
        }*/

        holder.tvItem.setText(cmm.name);
        holder.tvPrice.setText("₹ " + cmm.price);

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (cmm.quantity.equals("0")){
            holder.tvAdded.setVisibility(View.GONE);
            holder.btnAdd.setVisibility(View.VISIBLE);
        }else {
            holder.tvAdded.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.GONE);
        }

        if (cmm.image != null){
            if (!cmm.image.equals("")){
                Glide.with(context).load(cmm.image).into(holder.ivFoodImg);
            }else {
                Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivFoodImg);
            }
        }else {
            Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivFoodImg);
        }


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToCartRequest addToCartRequest = new AddToCartRequest(
                        prefs.getData(Constants.USER_ID),
                        android_id,
                        cmm.restaurant_id,
                        cmm.id,
                        cmm.name,
                        cmm.description,
                        "test",
                        cmm.price,
                        "1",
                        "0",
                        "",
                        "0",
                        "0",
                        "0",
                        "",
                        "0",
                        "0",
                        "0",
                        "0",
                        "0",
                        cmm.category_id
                );

                addProductToCart(addToCartRequest, cmm, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItem,tvPrice, tvAdded;
        ShapeableImageView ivFoodImg;
        Button btnAdd;
        LinearLayout mainLL;

        public Hold(@NonNull View itemView) {
            super(itemView);

           tvItem = itemView.findViewById(R.id.tvItem);
           tvPrice = itemView.findViewById(R.id.tvPrice);
           ivFoodImg = itemView.findViewById(R.id.ivFoodImg);
           btnAdd = itemView.findViewById(R.id.btnAdd);
           tvAdded = itemView.findViewById(R.id.tvAdded);
            mainLL = itemView.findViewById(R.id.mainLL);

        }
    }

    private void addProductToCart(AddToCartRequest addToCartRequest, UpsellItemModel mim, Hold holder) {
        dialogView.showCustomSpinProgress(context);
        manager.service.addItemToCart(addToCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    /*Gson gson = new Gson();
                    String resp = gson.toJson(crm);
                    Log.d("RESPONSE>>", resp);*/
                    if (crm.error != true){

                        Toast.makeText(context, "Added to cart!", Toast.LENGTH_SHORT).show();
                        holder.btnAdd.setVisibility(View.GONE);
                        holder.tvAdded.setVisibility(View.VISIBLE);
                        Intent i = new Intent(context, CartActivity.class);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).overridePendingTransition(0, 0);


                       /* cartsModel = crm.cart;
                        int price = Integer.parseInt(mim.price);
                        Constants.restIdFromCart = mim.restaurant_id;
                        prefs.saveData(Constants.RestId_FromCart, mim.restaurant_id);
                        Constants.cartID = cartsModel.id;
                        mim.cart_id = cartsModel.id;
                        prefs.saveData(Constants.CART_ID, cartsModel.id);
                        Constants.cartPrice = Constants.cartPrice + price;
                        Constants.TAG = Constants.TAG + 1;
                        mim.flag = mim.flag + 1;*/
                        //Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        // notifyDataSetChanged();


                    }else {
                        Toast.makeText(context, "Error->TRUE", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "Session exp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(context, "Server Error!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
