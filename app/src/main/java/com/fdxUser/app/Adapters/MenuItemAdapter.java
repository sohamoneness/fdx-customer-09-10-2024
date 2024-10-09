package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Fragments.BottomSheets.AddItemBottomSheet;
import com.fdxUser.app.Models.CartModels.AddToCartRequest;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.CartModels.DeleteCartItemResponseModel;
import com.fdxUser.app.Models.CartModels.UpdateCartRequest;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemsModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.Hold> {

    List<ItemsModel> mimList;
    List<CustomCartModel> ccList;
    Context context;
    int qty = 0;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    Prefs prefs;
    DialogView dialogView;
    CartsModel cartsModel = new CartsModel();
    String cartID = "";
    //RestaurantDetails restaurantDetails = new RestaurantDetails();
    //RestaurantDetails restaurantDetails;

    /*public MenuItemAdapter(List<ItemsModel> menuItemList, RestaurantDetails restaurantDetails) {
        this.context = restaurantDetails;
        this.mimList = menuItemList;
    }*/

   /* public MenuItemAdapter(List<ItemsModel> items, List<CustomCartModel> customCartDataList, RestaurantDetails restaurantDetails) {

        this.context = restaurantDetails;
        this.mimList = items;
        this.ccList = customCartDataList;

    }*/

    public MenuItemAdapter(List<ItemsModel> items, List<CustomCartModel> ccmList, Context context) {
        this.context = context;
        this.mimList = items;
        this.ccList = ccmList;
    }

    @NonNull
    @Override
    public MenuItemAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        prefs = new Prefs(context.getApplicationContext());
        dialogView = new DialogView();

        ItemsModel mim = mimList.get(position);

        if (position == mimList.size() - 1) {
            holder.view.setVisibility(View.INVISIBLE);
        }

        /*if (mim.flag > 0){
            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(mim.flag));

        }*/

        if (mim.quantity.equals("0") && mim.flag == 0) {

            holder.addItemLL.setVisibility(View.VISIBLE);
            holder.qtyCountRL.setVisibility(View.GONE);
            Constants.add_on_q = Integer.parseInt(mim.quantity);

        } else if (mim.quantity.equals("0") || mim.flag != 0) {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            holder.tvQty.setText(String.valueOf(mim.flag));

        } else if (!mim.quantity.equals("0") || mim.flag == 0) {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            mim.flag = Integer.parseInt(mim.quantity);
            holder.tvQty.setText(String.valueOf(mim.flag));

        } else {

            holder.addItemLL.setVisibility(View.GONE);
            holder.qtyCountRL.setVisibility(View.VISIBLE);
            mim.flag = Integer.parseInt(mim.quantity);
            Constants.add_on_q = Integer.parseInt(mim.quantity);
            holder.tvQty.setText(String.valueOf(mim.flag));

        }

        holder.tvMenuItemName.setText(mim.name);
        holder.tvExtraTxt.setText(Html.fromHtml(mim.description));

        if (mim.description.length() > 30 || mim.name.length() > 30){
            holder.ivItemInfo.setVisibility(View.VISIBLE);
        }else {
            holder.ivItemInfo.setVisibility(View.GONE);
        }

        if (mim.image != null) {
            if (!mim.image.equals("")) {
                Glide.with(context).load(mim.image).into(holder.ivFood);
            } else {
                Glide.with(context).load(R.drawable.no_img_menu).into(holder.ivFood);
            }
        }
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (mim.offer_price.equals(mim.price)){
            holder.tvOldPrice.setVisibility(View.GONE);
            DecimalFormat formatter = new DecimalFormat("#,##,###");
            String formatted = formatter.format(Double.parseDouble(mim.offer_price));
            //holder.tvPrice.setText("\u20B9 " + mim.price);
            holder.tvPrice.setText("\u20B9 " + formatted);

        }else {
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            DecimalFormat formatter = new DecimalFormat("#,##,###");
            String formatted = formatter.format(Double.parseDouble(mim.offer_price));
            //holder.tvPrice.setText("\u20B9 " + mim.price);
            holder.tvPrice.setText("\u20B9 " + formatted);
            String oldPrice = formatter.format(Double.parseDouble(mim.price));
            holder.tvOldPrice.setText("\u20B9 " + oldPrice);
        }



        if (mim.is_veg.equals("0")) {
            Glide.with(context).load(R.drawable.icon_nonveg).into(holder.ivItemType);
        } else {
            Glide.with(context).load(R.drawable.icon_veg).into(holder.ivItemType);
        }

        if (mim.in_stock.equals("1")){
            holder.addItemLL.setVisibility(View.VISIBLE);
            holder.osLL.setVisibility(View.GONE);
        }else {
            holder.addItemLL.setVisibility(View.GONE);
            holder.osLL.setVisibility(View.VISIBLE);
        }

        if (mim.is_bestseller.equals("1")){
            holder.tvSellStatus.setVisibility(View.VISIBLE);
        }else {
            holder.tvSellStatus.setVisibility(View.GONE);
        }



        holder.addItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (!mim.quantity.equals("0")) {
                if (Constants.isTakingOrder) {
                    if (Constants.restaurantModel.is_closed.equals("1")) {

                        Toast.makeText(context, "This restaurant is closed right now!", Toast.LENGTH_SHORT).show();

                    } else {
                        //prefs.saveData("");

                        if (mim.addonItems.size() > 0) {
                            //holder.addItemLL.setVisibility(View.GONE);
                            // holder.qtyCountRL.setVisibility(View.VISIBLE);
                            Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                            if (Constants.restIdFromCart.equals(mim.restaurant_id) || Constants.restIdFromCart.equals("")) {
                            //if (Constants.restIdFromCart.equals(mim.restaurant_id)) {

                                AddItemBottomSheet addItemBottomSheet = new AddItemBottomSheet();
                                AddItemBottomSheet.addOnItemList = mim.addonItems;
                                AddItemBottomSheet.itemPrice = mim.offer_price;
                                AddItemBottomSheet.im = mim;
                                AddItemBottomSheet.ccList = ccList;
                                AddItemBottomSheet.itemName = mim.name;
                                AddItemBottomSheet.itemImage = mim.image;
                                AddItemBottomSheet.isPlusClick = "0";
                                AddItemBottomSheet.countRL = holder.qtyCountRL;
                                AddItemBottomSheet.addLL = holder.addItemLL;
                                AddItemBottomSheet.tvQtyMenu = holder.tvQty;
                                addItemBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "callAddOn");
                            } else {
                                showClearCartAlert(android_id, mim, "hasAddOn");
                            }


                        } else {
                            holder.addItemLL.setVisibility(View.GONE);
                            holder.qtyCountRL.setVisibility(View.VISIBLE);
                            Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                            String image = "";
                            if (mim.image != null) {
                                image = mim.image;
                            } else {
                                image = "test";
                            }
                            Log.d("RESTID", Constants.restIdFromCart);
                            Log.d("RESTID1", mim.restaurant_id);
                            if (Constants.restIdFromCart.equals(mim.restaurant_id) || Constants.restIdFromCart.equals("")) {
                            //if (Constants.restIdFromCart.equals(mim.restaurant_id)) {
                                AddToCartRequest addToCartRequest = new AddToCartRequest(
                                        prefs.getData(Constants.USER_ID),
                                        android_id,
                                        mim.restaurant_id,
                                        mim.id,
                                        mim.name,
                                        mim.description,
                                        image,
                                        mim.offer_price,
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
                                        mim.category_id
                                );

                                addProductToCart(addToCartRequest, mim);
                            } else {

                                showClearCartAlert(android_id, mim, "noAddOn");

                            }
                        }
                    }
                } else {
                    showPopup();
                }
                //  }
                // restaurantDetails.callAddOn();

            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.restaurantModel.is_closed.equals("1")) {

                    Toast.makeText(context, "This restaurant is closed right now!", Toast.LENGTH_SHORT).show();

                } else {
                    if (mim.addonItems.size() > 0) {
                        showRemoveAddOnPopup();
                    }else {
                        double price = Double.parseDouble(mim.offer_price);
                        qty = Integer.parseInt(holder.tvQty.getText().toString());

                        if (mim.cart_id.equals("")) {
                            for (int i = 0; i < ccList.size(); i++) {
                                if (ccList.get(i).item_id.equals(mim.id)) {
                                    cartID = ccList.get(i).cart_id;
                                }
                            }

                        } else {
                            cartID = mim.cart_id;
                        }


                        if (qty > 1) {
                            qty = qty - 1;
                            Constants.TAG = Constants.TAG - 1;
                            Constants.cartPrice = Constants.cartPrice - price;
                            holder.tvQty.setText(String.valueOf(qty));

                            String img = "";

                            if (mim.image != null) {
                                img = mim.image;
                            } else {
                                img = "test";
                            }

                            mim.flag = mim.flag - 1;
                            // mim.flag = mim.flag - 1;


                            UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                    cartID,
                                    mim.id,
                                    mim.name,
                                    mim.description,
                                    img,
                                    mim.offer_price,
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

                            updateCartItem(updateCartRequest, mim);

                        } else {
                            Constants.TAG = Constants.TAG - 1;
                            mim.flag = 0;
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

                }

            }
        });

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.restaurantModel.is_closed.equals("1")) {

                    Toast.makeText(context, "This restaurant is closed right now!", Toast.LENGTH_SHORT).show();

                } else {

                    if (mim.addonItems.size() > 0) {
                        AddItemBottomSheet addItemBottomSheet = new AddItemBottomSheet();
                        AddItemBottomSheet.addOnItemList = mim.addonItems;
                        AddItemBottomSheet.itemPrice = mim.offer_price;
                        AddItemBottomSheet.im = mim;
                        AddItemBottomSheet.isPlusClick = "1";
                        AddItemBottomSheet.countRL = holder.qtyCountRL;
                        AddItemBottomSheet.addLL = holder.addItemLL;
                        AddItemBottomSheet.tvQtyMenu = holder.tvQty;
                        addItemBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "callAddOn");
                    } else {
                        for (int i = 0; i < ccList.size(); i++) {
                            if (ccList.get(i).item_id.equals(mim.id)) {
                                cartID = ccList.get(i).cart_id;
                            }
                        }

                        qty = Integer.parseInt(holder.tvQty.getText().toString());
                        int temp = qty;
                        qty = qty + 1;
                        Constants.TAG = Constants.TAG + 1;
                        double price = Double.parseDouble(mim.offer_price);
                        Constants.cartPrice = Constants.cartPrice + price;
                        holder.tvQty.setText(String.valueOf(qty));
                        String img = "";
                        if (mim.image != null) {
                            img = mim.image;
                        } else {
                            img = "test";
                        }

                        mim.flag = mim.flag + 1;

                        if (temp == 1) {
                            cartID = mim.cart_id;
                        }

                        UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                cartID,
                                mim.id,
                                mim.name,
                                mim.description,
                                img,
                                mim.offer_price,
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

                        updateCartItem(updateCartRequest, mim);
                    }

                }
            }
        });

        holder.ivItemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailsPopup(mim);
            }
        });

    }

    private void showRemoveAddOnPopup() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.remove_add_ons_lay);
        //dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);

       Button btnCancel = dialog.findViewById(R.id.btnCancel);
       Button btnOk = dialog.findViewById(R.id.btnOk);

       btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });

       btnOk.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((Activity)context).startActivity(new Intent(((Activity)context), CartActivity.class));
           }
       });


       /* ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();

    }


    private void showDetailsPopup(ItemsModel mim) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.item_details_popup);
        //dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);

        TextView tvItemName = (TextView) dialog.findViewById(R.id.tvItemName);
        TextView tvDes = (TextView) dialog.findViewById(R.id.tvDes);
        //ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivItemType = (ImageView) dialog.findViewById(R.id.ivItemType);

        if (mim.is_veg.equals("0")) {
            Glide.with(context).load(R.drawable.icon_nonveg).into(ivItemType);
        } else {
            Glide.with(context).load(R.drawable.icon_veg).into(ivItemType);
        }

        tvItemName.setText(mim.name);
        tvDes.setText(Html.fromHtml(mim.description));

       /* ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

        dialog.show();

    }

    private void showClearCartAlert(String android_id, ItemsModel mim, String addOn) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.cart_clear_alert_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

       /* LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.cart_clear_alert_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(((Activity) context).getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);

        //btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCart(android_id, mim, addOn);

                dialog.dismiss();

                //deleteFromCart(id, pos);


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


       /* alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);*/
        dialog.show();
    }

    private void showPopup() {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.not_taking_orders_popup_lay);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
       /* LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.not_taking_orders_popup_lay, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(((Activity) context).getResources().getString(R.string.app_name));
        //EditText etReasonMsg=(EditText) promptView.findViewById(R.id.etReasonMsg);
        //TextView tvMsg=(TextView) promptView.findViewById(R.id.tvMsg);
        //tvMsg.setText(msg);

        //btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        //btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                //deleteFromCart(id, pos);


            }
        });


       /* alertD.setCancelable(false);
        alertD.setCanceledOnTouchOutside(false);

        alertD.setView(promptView);*/
        dialog.show();
    }

    private void clearCart(String android_id, ItemsModel mim, String addOn) {
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
                        prefs.saveData(Constants.RestId_FromCart, "");

                        if (addOn.equals("hasAddOn")) {
                            //Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                            //if (Constants.restIdFromCart.equals(mim.restaurant_id) || Constants.restIdFromCart.equals("")) {
                            AddItemBottomSheet addItemBottomSheet = new AddItemBottomSheet();
                            AddItemBottomSheet.addOnItemList = mim.addonItems;
                            AddItemBottomSheet.itemPrice = mim.offer_price;
                            AddItemBottomSheet.im = mim;
                            AddItemBottomSheet.ccList = ccList;
                            AddItemBottomSheet.itemName = mim.name;
                            AddItemBottomSheet.itemImage = mim.image;
                            AddItemBottomSheet.isPlusClick = "0";
                            addItemBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "callAddOn");
                        } else {
                            String image = "";
                            if (mim.image != null) {
                                image = mim.image;
                            } else {
                                image = "test";
                            }


                            AddToCartRequest addToCartRequest = new AddToCartRequest(
                                    prefs.getData(Constants.USER_ID),
                                    android_id,
                                    mim.restaurant_id,
                                    mim.id,
                                    mim.name,
                                    mim.description,
                                    image,
                                    mim.offer_price,
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
                                    mim.category_id
                            );

                            addProductToCart(addToCartRequest, mim);

                        }

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

    private void updateCartItem(UpdateCartRequest updateCartRequest, ItemsModel mim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.updateCartItem(updateCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    if (crm.error != true) {

                        CartsModel cm = crm.cart;

                        mim.cart_id = cartsModel.id;


                        // Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        // notifyDataSetChanged();


                    }

                } else {
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

    private void addProductToCart(AddToCartRequest addToCartRequest, ItemsModel mim) {
        dialogView.showCustomSpinProgress(context);
        manager.service.addItemToCart(addToCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    /*Gson gson = new Gson();
                    String resp = gson.toJson(crm);
                    Log.d("RESPONSE>>", resp);*/
                    if (crm.error != true) {

                        cartsModel = crm.cart;
                        double price = Double.parseDouble(mim.offer_price);
                        // Constants.restIdFromCart = mim.restaurant_id;
                        prefs.saveData(Constants.RestId_FromCart, mim.restaurant_id);
                        Constants.cartID = cartsModel.id;
                        mim.cart_id = cartsModel.id;
                        prefs.saveData(Constants.CART_ID, cartsModel.id);
                        Constants.cartPrice = Constants.cartPrice + price;
                        Constants.TAG = Constants.TAG + 1;
                        mim.flag = mim.flag + 1;

                        //Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        // notifyDataSetChanged();


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
        return mimList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvMenuItemName, tvExtraTxt, tvPrice, tvQty, tvSellStatus, tvOldPrice;
        LinearLayout addItemLL, osLL;
        RelativeLayout qtyCountRL;
        ImageView ivMinus, ivPlus, ivItemType, ivItemInfo;
        ShapeableImageView ivFood;
        View view;
        //ShowMoreTextView tvItemName;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvMenuItemName = itemView.findViewById(R.id.tvItemName);
            //tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            addItemLL = itemView.findViewById(R.id.addItemLL);
            qtyCountRL = itemView.findViewById(R.id.qtyCountRL);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            tvQty = itemView.findViewById(R.id.tvQty);
            ivFood = itemView.findViewById(R.id.ivFood);
            ivItemType = itemView.findViewById(R.id.ivItemType);
            view = itemView.findViewById(R.id.view);
            osLL = itemView.findViewById(R.id.osLL);
            ivItemInfo = itemView.findViewById(R.id.ivItemInfo);
            tvSellStatus = itemView.findViewById(R.id.tvSellStatus);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);


        }
    }
}
