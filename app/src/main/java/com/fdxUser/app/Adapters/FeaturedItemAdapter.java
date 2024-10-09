package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.CartModels.AddToCartRequest;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.CartModels.DeleteCartItemResponseModel;
import com.fdxUser.app.Models.CartModels.UpdateCartRequest;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.DashboardModels.SpecialItemModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeaturedItemAdapter extends RecyclerView.Adapter<FeaturedItemAdapter.Hold> {

    List<SpecialItemModel> spList;
    List<CustomCartModel> ccList;
    Context context;
    int qty = 0;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;
    CartsModel cartsModel = new CartsModel();
    String cartID = "";

    public FeaturedItemAdapter(RestaurantDetails restaurantDetails, List<SpecialItemModel> specialItemList, List<CustomCartModel> customCartDataList) {
        this.context = restaurantDetails;
        this.spList = specialItemList;
        this.ccList = customCartDataList;
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {
        prefs = new Prefs(context.getApplicationContext());
        dialogView = new DialogView();

        SpecialItemModel sim = spList.get(position);

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        /*if (position == spList.size()-1){
            holder.view.setVisibility(View.GONE);
        }*/

        if (position == spList.size()-1){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    360,
                    LinearLayout.LayoutParams. MATCH_PARENT ) ;
            layoutParams.setMargins( 48 , 0 , 48 , 0 ) ;
            //ll.addView(okButton , layoutParams) ;
            holder.mainLL.setLayoutParams(layoutParams);
        }

        if (sim.quantity.equals("0") && sim.flag == 0) {

            holder.addItemLL.setVisibility(View.VISIBLE);
            holder.qtyCountRL.setVisibility(View.GONE);

        } else if (sim.quantity.equals("0") || sim.flag != 0) {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(sim.flag));

        } else if (!sim.quantity.equals("0") || sim.flag == 0) {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            if (!sim.quantity.equals("")) {
                sim.flag = Integer.parseInt(sim.quantity);
            }
            holder.tvQty.setText(String.valueOf(sim.flag));

        } else {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            if (!sim.quantity.equals("")) {
                sim.flag = Integer.parseInt(sim.quantity);
            }
            holder.tvQty.setText(String.valueOf(sim.flag));

        }


        holder.tvItem.setText(sim.name);
        holder.tvDes.setText(sim.description);

        if (sim.image != null) {
            if (!sim.image.equals("")) {
                Glide.with(context).load(sim.image).into(holder.ivImage);
            } else {
                Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivImage);
            }
        }

        holder.tvPrice.setText("\u20B9 " + sim.price);

        holder.addItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.isTakingOrder){
                    holder.addItemLL.setVisibility(View.GONE);
                    holder.qtyCountRL.setVisibility(View.VISIBLE);

                    Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                    String image = "";
                    if (sim.image != null) {
                        image = sim.image;
                    } else {
                        image = "test";
                    }

                    if (Constants.restIdFromCart.equals(sim.restaurant_id) || Constants.restIdFromCart.equals("")) {
                        AddToCartRequest addToCartRequest = new AddToCartRequest(
                                prefs.getData(Constants.USER_ID),
                                android_id,
                                sim.restaurant_id,
                                sim.id,
                                sim.name,
                                sim.description,
                                image,
                                sim.price,
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
                                sim.category_id
                        );

                        addProductToCart(addToCartRequest, sim);
                    } else {

                        clearCart(android_id, sim);

                    }
                }else {
                    showPopup();
                }
            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price = Double.parseDouble(sim.price);
                qty = Integer.parseInt(holder.tvQty.getText().toString());

                if (sim.cart_id.equals("")){
                    for(int i = 0; i < ccList.size(); i++){
                        if (ccList.get(i).item_id.equals(sim.id)){
                            cartID = ccList.get(i).cart_id;
                        }
                    }

                }else {
                    cartID = sim.cart_id;
                }



                if (qty > 1){
                    qty = qty - 1;
                    Constants.TAG = Constants.TAG - 1;
                    Constants.cartPrice = Constants.cartPrice - price;
                    holder.tvQty.setText(String.valueOf(qty));

                    String img = "";

                    if (sim.image != null){
                        img = sim.image;
                    }else{
                        img = "test";
                    }

                    sim.flag = sim.flag-1;
                    // mim.flag = mim.flag - 1;



                    UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                            cartID,
                            sim.id,
                            sim.name,
                            sim.description,
                            img,
                            sim.price,
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

                    updateCartItem(updateCartRequest, sim);

                }else{
                    Constants.TAG = Constants.TAG - 1;
                    sim.flag = 0;
                    Constants.cartPrice = Constants.cartPrice - price;
                    holder.addItemLL.setVisibility(View.VISIBLE);
                    holder.qtyCountRL.setVisibility(View.GONE);

                   /*UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                           prefs.getData(Constants.USER_ID),
                           mim.id,
                           mim.name,
                           mim.image,
                           mim.price,
                           "0",
                           "0",
                           "0",
                           "0"
                   );*/
                    // if (!mim.cart_id.equals("")){
                    deleteFromCart(cartID);
                    //  }else{

                    //  }

                    //updateCartItem();
                }


            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < ccList.size(); i++){
                    if (ccList.get(i).item_id.equals(sim.id)){
                        cartID = ccList.get(i).cart_id;
                    }
                }

                qty = Integer.parseInt(holder.tvQty.getText().toString());
                int temp = qty;
                qty = qty + 1;
                Constants.TAG = Constants.TAG + 1;
                double price = Double.parseDouble(sim.price);
                Constants.cartPrice = Constants.cartPrice + price;
                holder.tvQty.setText(String.valueOf(qty));
                String img = "";
                if (sim.image != null){
                    img = sim.image;
                }else{
                    img = "test";
                }

                sim.flag = sim.flag + 1;

                if (temp == 1){
                    cartID = sim.cart_id;
                }

                UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                        cartID,
                        sim.id,
                        sim.name,
                        sim.description,
                        img,
                        sim.price,
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

                updateCartItem(updateCartRequest, sim);
            }
        });


    }

    private void clearCart(String android_id, SpecialItemModel sim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.clearCartData(prefs.getData(Constants.USER_ID)).enqueue(new Callback<ClearCartResponseModel>() {
            @Override
            public void onResponse(Call<ClearCartResponseModel> call, Response<ClearCartResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    ClearCartResponseModel clearCartResponseModel = response.body();
                    if (!clearCartResponseModel.error) {
                        Constants.TAG = 0;
                        Constants.cartPrice = 0;

                        String image = "";
                        if (sim.image != null) {
                            image = sim.image;
                        } else {
                            image = "test";
                        }

                        AddToCartRequest addToCartRequest = new AddToCartRequest(
                                prefs.getData(Constants.USER_ID),
                                android_id,
                                sim.restaurant_id,
                                sim.id,
                                sim.name,
                                sim.description,
                                image,
                                sim.price,
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
                                sim.category_id
                        );

                        addProductToCart(addToCartRequest, sim);

                    } else {

                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ClearCartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void deleteFromCart(String id) {
        dialogView.showCustomSpinProgress(context);
        manager.service.deleteCartItem(id).enqueue(new Callback<DeleteCartItemResponseModel>() {
            @Override
            public void onResponse(Call<DeleteCartItemResponseModel> call, Response<DeleteCartItemResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    DeleteCartItemResponseModel deleteCartItemResponseModel = response.body();
                    if (deleteCartItemResponseModel.error != true) {
                        Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();
                        //Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        //notifyDataSetChanged();
                    }
                } else {
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

    private void updateCartItem(UpdateCartRequest updateCartRequest, SpecialItemModel sim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.updateCartItem(updateCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()){

                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    if (crm.error != true){

                        CartsModel cm = crm.cart;

                        sim.cart_id = cartsModel.id;


                        // Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        // notifyDataSetChanged();


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

    private void showPopup() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.not_taking_orders_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        TextView tvHeader=(TextView)promptView.findViewById(R.id.tvHeader);
        tvHeader.setText(((Activity)context).getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);

        //btnCancel.setText("Cancel");
        Button btnOk = (Button) promptView.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertD.dismiss();

                //deleteFromCart(id, pos);


            }
        });


        alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);
        alertD.show();
    }

    private void addProductToCart(AddToCartRequest addToCartRequest, SpecialItemModel sim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.addItemToCart(addToCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    Gson gson = new Gson();
                    String resp = gson.toJson(crm);
                    Log.d("RESPONSE>>", resp);
                    if (crm.error != true) {

                        cartsModel = crm.cart;
                        int price = Integer.parseInt(sim.price);
                        Constants.restIdFromCart = sim.restaurant_id;
                        prefs.saveData(Constants.RestId_FromCart, sim.restaurant_id);
                        Constants.cartID = cartsModel.id;
                        sim.cart_id = cartsModel.id;
                        prefs.saveData(Constants.CART_ID, cartsModel.id);
                        Constants.cartPrice = Constants.cartPrice + price;
                        Constants.TAG = Constants.TAG + 1;
                        sim.flag = sim.flag + 1;

                    } else {
                        Toast.makeText(context, "Error->TRUE", Toast.LENGTH_SHORT).show();
                    }

                } else {
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

    @Override
    public int getItemCount() {
        return spList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItem, tvPrice, tvDes;
        ShapeableImageView ivImage;
        LinearLayout addItemLL, mainLL;
        RelativeLayout qtyCountRL;
        ImageView ivMinus, ivPlus;
        TextView tvQty;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.tvItem);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            addItemLL = itemView.findViewById(R.id.addItemLL);
            qtyCountRL = itemView.findViewById(R.id.qtyCountRL);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            mainLL = itemView.findViewById(R.id.mainLL);

        }
    }
}
