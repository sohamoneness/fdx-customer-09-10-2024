package com.fdxUser.app.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CartModels.DeleteCartItemResponseModel;
import com.fdxUser.app.Models.CartModels.UpdateCartRequest;
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

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.Hold> {

    List<CartsModel> ciList;
    Context context;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;
    double totPrice = 0.0;

    public CartItemAdapter(CartActivity cartActivity, List<CartsModel> cartItemList) {
        this.context = cartActivity;
        this.ciList = cartItemList;
    }


    @NonNull
    @Override
    public CartItemAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.Hold holder, @SuppressLint("RecyclerView") int position) {

        prefs = new Prefs(context.getApplicationContext());
        dialogView = new DialogView();

        CartsModel cm = ciList.get(position);

        if (position == (ciList.size() - 1)){
            holder.viewLay.setVisibility(View.GONE);
        }else {
            holder.viewLay.setVisibility(View.VISIBLE);
        }
        //holder.tvExtraTxt.setText(cm.product_description);
        //new change
        holder.tvExtraTxt.setText(Html.fromHtml(cm.product_description));

        holder.tvItemName.setText(cm.product_name);
        if (cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){
            holder.tvAddOn.setVisibility(View.GONE);
            totPrice = Double.parseDouble(cm.price) * Integer.parseInt(cm.quantity);
        }else if (!cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){
            holder.tvAddOn.setVisibility(View.VISIBLE);
            holder.tvAddOn.setText("With Add-ons: " + cm.add_on_name);
            totPrice = (Double.parseDouble(cm.price) + Double.parseDouble(cm.add_on_price)) * Integer.parseInt(cm.quantity);
        }else if (!cm.add_on_id.equals("0") && !cm.add_on_id2.equals("0")){
            holder.tvAddOn.setVisibility(View.VISIBLE);
            holder.tvAddOn.setText("With Add-ons: " + cm.add_on_name + " & " + cm.add_on_name2);
            totPrice = (Double.parseDouble(cm.price) + Double.parseDouble(cm.add_on_price) +
                    Double.parseDouble(cm.add_on_price2)) * Integer.parseInt(cm.quantity);

        }


        holder.tvPrice.setText("\u20B9 " + String.valueOf(totPrice));
        holder.tvQty.setText(cm.quantity);

        //if (cm.add_on_price.equals("") && cm.add_on_price2.equals("")){
            double itemPrice = Double.parseDouble(cm.price) + Double.parseDouble(cm.add_on_price) + Double.parseDouble(cm.add_on_price2);

            holder.tvItemPrice.setText("\u20B9 " + String.valueOf(itemPrice));
        //}else if (){

        //}

        //holder.tvItemPrice.setText("\u20B9 " + cm.price);

        if (cm.product_image != null){
            if (!cm.product_image.equals("")){
                Glide.with(context).load(cm.product_image).into(holder.ivFood);
            }else {
                Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivFood);
            }
        }else {
            Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivFood);
        }




        /*if (cm..equals("0")){
            Glide.with(context).load(R.drawable.non_veg).into(holder.ivItemType);
        }else {
            Glide.with(context).load(R.drawable.veg).into(holder.ivItemType);
        }*/

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(cm.price);
                int q = Integer.parseInt(holder.tvQty.getText().toString());
                if (q > 1){
                    q = q-1;
                    Constants.TAG = Constants.TAG - 1;
                    Constants.cartPrice = Constants.cartPrice - price;
                    holder.tvQty.setText(String.valueOf(q));
                    String img = "";
                    if (cm.product_image != null){
                        img = cm.product_image;
                    }else {
                        img = "";
                    }

                    if (cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){
                        UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                cm.id,
                                cm.product_id,
                                cm.product_name,
                                cm.product_description,
                                img,
                                cm.price,
                                holder.tvQty.getText().toString(),
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
                                "0"
                        );
                        updateCartItem(updateCartRequest);

                    }else if (!cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){
                        UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                cm.id,
                                cm.product_id,
                                cm.product_name,
                                cm.product_description,
                                img,
                                cm.price,
                                holder.tvQty.getText().toString(),
                                cm.add_on_id,
                                cm.add_on_name,
                                cm.add_on_price,
                                holder.tvQty.getText().toString(),
                                "0",
                                "",
                                "0",
                                "0",
                                "0",
                                "0",
                                "0"
                        );
                        updateCartItem(updateCartRequest);
                    }else if (!cm.add_on_id.equals("0") && !cm.add_on_id2.equals("0")){
                        UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                cm.id,
                                cm.product_id,
                                cm.product_name,
                                cm.product_description,
                                img,
                                cm.price,
                                holder.tvQty.getText().toString(),
                                cm.add_on_id,
                                cm.add_on_name,
                                cm.add_on_price,
                                holder.tvQty.getText().toString(),
                                cm.add_on_id2,
                                cm.add_on_name2,
                                cm.add_on_price2,
                                holder.tvQty.getText().toString(),
                                "0",
                                "0",
                                "0"
                        );
                        updateCartItem(updateCartRequest);

                    }

                }else{
                    showDeleteAlertPopup(cm.id, position, cm.restaurant_id, price);


                }

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int q = Integer.parseInt(holder.tvQty.getText().toString());
                q = q+1;
                holder.tvQty.setText(String.valueOf(q));
                Constants.TAG = Constants.TAG + 1;
                double price = Double.parseDouble(cm.price);
                Constants.cartPrice = Constants.cartPrice + price;
                String img = "";
                if (cm.product_image != null){
                    img = cm.product_image;
                }else {
                    img = "";
                }

                if (cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){
                    UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                            cm.id,
                            cm.product_id,
                            cm.product_name,
                            cm.product_description,
                            img,
                            cm.price,
                            holder.tvQty.getText().toString(),
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
                            "0"
                    );

                    updateCartItem(updateCartRequest);

                }else if (!cm.add_on_id.equals("0") && cm.add_on_id2.equals("0")){

                    UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                            cm.id,
                            cm.product_id,
                            cm.product_name,
                            cm.product_description,
                            img,
                            cm.price,
                            holder.tvQty.getText().toString(),
                            cm.add_on_id,
                            cm.add_on_name,
                            cm.add_on_price,
                            holder.tvQty.getText().toString(),
                            "0",
                            "",
                            "0",
                            "0",
                            "0",
                            "0",
                            "0"
                    );

                    updateCartItem(updateCartRequest);

                }else if (!cm.add_on_id.equals("0") && !cm.add_on_id2.equals("0")){
                    UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                            cm.id,
                            cm.product_id,
                            cm.product_name,
                            cm.product_description,
                            img,
                            cm.price,
                            holder.tvQty.getText().toString(),
                            cm.add_on_id,
                            cm.add_on_name,
                            cm.add_on_price,
                            holder.tvQty.getText().toString(),
                            cm.add_on_id2,
                            cm.add_on_name2,
                            cm.add_on_price2,
                            holder.tvQty.getText().toString(),
                            "0",
                            "0",
                            "0"
                    );

                    updateCartItem(updateCartRequest);
                }

            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(cm.price);
                showDeleteAlertPopup(cm.id, position, cm.restaurant_id, price);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(cm.price);
                showDeleteAlertPopup(cm.id, position, cm.restaurant_id, price);
            }
        });



    }

    private void showDeleteAlertPopup(String id, int pos, String restId, double price) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(context.getResources().getString(R.string.app_name));
        TextView tvMsg=(TextView)dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Are you sure to remove this item ?");
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteFromCart(id, pos, restId, price);

                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

       /* alertD.setView(promptView);
        try {*/
            dialog.show();
       /* }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }

    private void deleteFromCart(String id, int pos, String restId, double price) {
        dialogView.showCustomSpinProgress(context);
        manager.service.deleteCartItem(id).enqueue(new Callback<DeleteCartItemResponseModel>() {
            @Override
            public void onResponse(Call<DeleteCartItemResponseModel> call, Response<DeleteCartItemResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    DeleteCartItemResponseModel deleteCartItemResponseModel = response.body();
                    if (deleteCartItemResponseModel.error != true){
                       // context.startActivity(new Intent(context.getApplicationContext(), CartActivity.class));
                        Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();

                        if (ciList.size() > 1){
                            Constants.TAG = Constants.TAG - 1;
                            Constants.cartPrice = Constants.cartPrice - price;
                            ciList.remove(pos);
                            notifyItemRemoved(pos);
                            Intent i = new Intent(((Activity)context), CartActivity.class);
                            ((Activity)context).finish();
                            ((Activity)context).overridePendingTransition(0, 0);
                            ((Activity)context).startActivity(i);
                            ((Activity)context).overridePendingTransition(0, 0);
                        }else{
                            //ciList.remove(pos);
                            Constants.isCartEmpty = true;
                            //context
                            ((Activity)context).startActivity(new Intent(((Activity)context), RestaurantDetails.class).putExtra(Constants.REST_ID, restId));
                            ((Activity)context).finish();

                        }

                    }
                }else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteCartItemResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();
                Toast.makeText(context, "Server failed! Please try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateCartItem(UpdateCartRequest updateCartRequest) {
        dialogView.showCustomSpinProgress(context);
        manager.service.updateCartItem(updateCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()){

                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    if (crm.error != true){

                        Intent i = new Intent(((Activity)context), CartActivity.class);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(0, 0);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).overridePendingTransition(0, 0);


                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    @Override
    public int getItemCount() {
        return ciList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName, tvExtraTxt, tvPrice, tvQty;
        TextView tvDelete, tvItemPrice, tvAddOn;
        ImageView ivMinus, ivPlus, ivType, ivDelete;
        View viewLay;
        ShapeableImageView ivFood;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            ivType = itemView.findViewById(R.id.ivType);
            viewLay = itemView.findViewById(R.id.viewLay);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvAddOn = itemView.findViewById(R.id.tvAddOn);

        }
    }
}
