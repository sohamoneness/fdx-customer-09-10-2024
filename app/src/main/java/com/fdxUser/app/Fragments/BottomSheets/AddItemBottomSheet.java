package com.fdxUser.app.Fragments.BottomSheets;

import android.os.Bundle;
import android.os.Handler;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fdxUser.app.Adapters.AddOnItemAdapter;
import com.fdxUser.app.CustomFonts.ManropeBoldTextView;
import com.fdxUser.app.Models.CartModels.AddToCartRequest;
import com.fdxUser.app.Models.CartModels.CartResponseModel;
import com.fdxUser.app.Models.CartModels.CartsModel;
import com.fdxUser.app.Models.CartModels.UpdateCartRequest;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.AddOnModels;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemsModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemBottomSheet extends BottomSheetDialogFragment {

    RecyclerView addOnRv;
    public static List<AddOnModels> addOnItemList = new ArrayList<>();
    //List<AddOnItemModel> tempAddOnItemList = new ArrayList<>();
    AddOnItemAdapter addOnItemAdapter;
    public static String itemPrice;
    public static String itemName = "", itemImage = "";
    int price = 0;
    public static ItemsModel im = new ItemsModel();
    public static List<CustomCartModel> ccList = new ArrayList<>();
    public static LinearLayout addLL;
    public static RelativeLayout countRL;
    public static TextView tvQtyMenu;
    ImageView ivMinus, ivPlus;
    TextView tvQty;
    TextView tvAddItem;
    ManropeBoldTextView tvItemName;
    String qty = "";
    int totAddOnPrice = 0;
    Prefs prefs;
    DialogView dialogView;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    String addOnItemId = "0", addOnItemName = "", addOnItemPrice = "0", addOnItemQty = "0";
    String addOnItemId2 = "0", addOnItemName2 = "", addOnItemPrice2 = "0", addOnItemQty2 = "0";
    public static CartsModel cartsModel = new CartsModel();
    CircleImageView itemImg;
    LinearLayout addOnLay, addOtherLay;
    TextView tvLastAdd, tvPrice;
    Button btnNewAdd, btnRepeat;
    public static String isPlusClick = "";
    String addAsNew = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.additem_bottomsheet, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        addOnRv = v.findViewById(R.id.addOnRv);
        ivMinus = v.findViewById(R.id.ivMinus);
        ivPlus = v.findViewById(R.id.ivPlus);
        tvQty = v.findViewById(R.id.tvQty);
        tvAddItem = v.findViewById(R.id.tvAddonItem);
        tvItemName = v.findViewById(R.id.tvItemName);
        itemImg = v.findViewById(R.id.itemImg);
        addOnLay = v.findViewById(R.id.addOnLay);
        addOtherLay = v.findViewById(R.id.addOtherLay);
        tvLastAdd = v.findViewById(R.id.tvLastAdd);
        tvPrice = v.findViewById(R.id.tvPrice);
        btnNewAdd = v.findViewById(R.id.btnNewAdd);
        btnRepeat = v.findViewById(R.id.btnRepeat);

        Log.d("PRICE>>", itemPrice);

        //getAddOnItems();
        tvAddItem.setText("Add item ₹ " + itemPrice);


        price = Integer.parseInt(itemPrice);
        Constants.priceWithAddOn = price;

        tvItemName.setText(itemName);
        if (itemImage != null) {
            if (itemImage.equals("")) {
                Glide.with(getActivity()).load(R.drawable.no_img_menu).into(itemImg);
            } else {
                Glide.with(getActivity()).load(itemImage).into(itemImg);
            }
        } else {
            Glide.with(getActivity()).load(R.drawable.no_img_menu).into(itemImg);
        }

        if (isPlusClick.equals("1")) {
            addOnLay.setVisibility(View.GONE);
            addOtherLay.setVisibility(View.VISIBLE);
        } else {
            addOnLay.setVisibility(View.VISIBLE);
            addOtherLay.setVisibility(View.GONE);
        }

        btnNewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAsNew = "1";
                addOnLay.setVisibility(View.VISIBLE);
                addOtherLay.setVisibility(View.GONE);
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = "";
                if (im.image != null) {
                    image = im.image;
                } else {
                    image = "test";
                }
                //Log.d("update_ADDON>>", "Const"+ Constants.addOnId + ", " + Constants.addOnName + ", " + Constants.addOnPrice + ", " + addOnItemQty + ", " + addOnItemId2 + ", " + addOnItemName2 + ", " + addOnItemPrice2 + ", " + addOnItemQty2);
               // Toast.makeText(getActivity(), "Const"+ Constants.addOnId + ", " + Constants.addOnName + ", " + Constants.addOnPrice + ", " + Constants.addOnCartId, Toast.LENGTH_LONG).show();

                UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                        Constants.addOnCartId,
                        Constants.addOnForId,
                        Constants.addOnForName,
                        Constants.addOnForDescription,
                        image,
                        Constants.addOnForPrice,
                        Constants.addOnForQty,
                        Constants.addOnId,
                        Constants.addOnName,
                        Constants.addOnPrice,
                        Constants.addOnQty,
                        Constants.addOnId2,
                        Constants.addOnName2,
                        Constants.addOnPrice2,
                        Constants.addOnQty2,
                        "0",
                        "0",
                        "0"
                );

                updateCartItem(updateCartRequest, im);

            }
        });


        //tvAddItem.setText("Add item ₹ " + itemPrice);

        addOnItemAdapter = new AddOnItemAdapter(getActivity(), addOnItemList, price);
        addOnRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        addOnRv.setAdapter(addOnItemAdapter);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                Log.d("TAG>>", Constants.TAG + "");
               /* totAddOnPrice = 0;
                for (int i = 0; i < addOnItemList.size(); i++){
                    if (addOnItemList.get(i).tag == 1){
                        int p = Integer.parseInt(addOnItemList.get(i).price);
                        totAddOnPrice = totAddOnPrice + p;
                        break;
                        //addOnItemList.get(i).add_on_tag = 0;

                    }
                }
                if (totAddOnPrice != 0){
                    price = price +(Integer.parseInt(tvQty.getText().toString()) * totAddOnPrice);
                }*/

                tvAddItem.setText("Add item ₹ " + Constants.priceWithAddOn);

                handler.postDelayed(this, 900);
            }
        };
        runnable.run();


        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qnty = Integer.parseInt(tvQty.getText().toString());

                if (qnty > 1) {
                    qnty = qnty - 1;
                    Constants.TAG = Constants.TAG - 1;
                    Constants.cartPrice = Constants.cartPrice - price;
                    tvQty.setText(String.valueOf(qnty));
                } else {
                    dismiss();
                }

            }
        });

        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qnty = Integer.parseInt(tvQty.getText().toString());

                qnty = qnty + 1;
                Constants.TAG = Constants.TAG + 1;
                //double price = Double.parseDouble(mim.price);
                Constants.cartPrice = Constants.cartPrice + price;

                tvQty.setText(String.valueOf(qnty));

            }
        });

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String image = "";
                if (im.image != null) {
                    image = im.image;
                } else {
                    image = "test";
                }


                if ((Integer.parseInt(im.quantity) > 0)) {

                    if (!addAsNew.equals("1")) {

                        int count = 0;

                        for (int i = 0; i < addOnItemList.size(); i++){
                            if (addOnItemList.get(i).tag == 1) {
                                count = count + 1;
                            }
                        }

                        if (count == 1){
                            for (int i = 0; i < addOnItemList.size(); i++){
                                if (addOnItemList.get(i).tag == 1) {
                                    addOnItemId = addOnItemList.get(i).id;
                                    addOnItemName = addOnItemList.get(i).name;
                                    addOnItemPrice = addOnItemList.get(i).offer_price;
                                    addOnItemQty = String.valueOf(Integer.parseInt(im.quantity)
                                            + Integer.parseInt(tvQty.getText().toString()));
                                    addOnItemId2 = "0";
                                    addOnItemName2 = "";
                                    addOnItemPrice2 = "0";
                                    addOnItemQty2 = "0";
                                }
                            }

                        }else if (count == 2){
                            for (int i = 0; i < addOnItemList.size(); i++) {
                                if (count == 2) {
                                    if (addOnItemList.get(i).tag == 1) {
                                        count = count - 1;
                                        addOnItemId = addOnItemList.get(i).id;
                                        addOnItemName = addOnItemList.get(i).name;
                                        addOnItemPrice = addOnItemList.get(i).offer_price;
                                        addOnItemQty = String.valueOf(Integer.parseInt(im.quantity)
                                                + Integer.parseInt(tvQty.getText().toString()));


                                    } else {
                                /*addOnItemId = addOnItemList.get(i).id;
                                addOnItemName = addOnItemList.get(i).name;
                                addOnItemPrice = addOnItemList.get(i).price;
                                addOnItemQty = addOnItemList.get(i).add;*/
                                    }
                                } else if (count == 1) {
                                    if (addOnItemList.get(i).tag == 1) {
                                        count = 0;
                                        addOnItemId2 = addOnItemList.get(i).id;
                                        addOnItemName2 = addOnItemList.get(i).name;
                                        addOnItemPrice2 = addOnItemList.get(i).offer_price;
                                        addOnItemQty2 = String.valueOf(Integer.parseInt(im.quantity)
                                                + Integer.parseInt(tvQty.getText().toString()));

                                    } else {
                                        addOnItemId2 = "0";
                                        addOnItemName2 = "";
                                        addOnItemPrice2 = "0";
                                        addOnItemQty2 = "0";
                                    }
                                }
                            }
                        }else if (count == 0){
                            addOnItemId = "0";
                            addOnItemName = "";
                            addOnItemPrice = "0";
                            addOnItemQty = "0";
                            addOnItemId2 = "0";
                            addOnItemName2 = "";
                            addOnItemPrice2 = "0";
                            addOnItemQty2 = "0";
                        }



                        for (int i = 0; i < ccList.size(); i++) {
                            if (ccList.get(i).item_id.equals(im.id)) {
                                im.cart_id = ccList.get(i).cart_id;
                            }
                        }

                        //qty = String.valueOf(Integer.parseInt(im.quantity) + 1);

                       /* UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                im.cart_id,
                                im.id,
                                im.name,
                                im.description,
                                image,
                                im.price,
                                String.valueOf(Integer.parseInt(im.quantity) + Integer.parseInt(tvQty.getText().toString())),
                                addOnItemId,
                                addOnItemName,
                                addOnItemPrice,
                                addOnItemQty,
                                addOnItemId2,
                                addOnItemName2,
                                addOnItemPrice2,
                                addOnItemQty2,
                                "0",
                                "0",
                                "0"
                        );*/
                        UpdateCartRequest updateCartRequest = new UpdateCartRequest(
                                im.cart_id,
                                im.id,
                                im.name,
                                im.description,
                                image,
                                im.offer_price,
                                String.valueOf(Integer.parseInt(im.quantity) + Integer.parseInt(tvQty.getText().toString())),
                                addOnItemId,
                                addOnItemName,
                                addOnItemPrice,
                                addOnItemQty,
                                addOnItemId2,
                                addOnItemName2,
                                addOnItemPrice2,
                                addOnItemQty2,
                                "0",
                                "0",
                                "0"
                        );

                        updateCartItem(updateCartRequest, im);

                    } else {
                        int count = 0;
                        for (int i = 0; i < addOnItemList.size(); i++){
                            if (addOnItemList.get(i).tag == 1) {
                                count = count + 1;
                            }
                        }
                        Log.d("COUNT>>", ""+count);

                        if (count == 1){
                            for (int i = 0; i < addOnItemList.size(); i++){
                                if (addOnItemList.get(i).tag == 1) {
                                    addOnItemId = addOnItemList.get(i).id;
                                    addOnItemName = addOnItemList.get(i).name;
                                    addOnItemPrice = addOnItemList.get(i).offer_price;
                                    addOnItemQty = String.valueOf(Integer.parseInt(im.quantity)
                                            + Integer.parseInt(tvQty.getText().toString()));
                                    addOnItemId2 = "0";
                                    addOnItemName2 = "";
                                    addOnItemPrice2 = "0";
                                    addOnItemQty2 = "0";
                                    Constants.addOnId = addOnItemId;
                                    Constants.addOnName = addOnItemName;
                                    Constants.addOnPrice = addOnItemPrice;
                                    Constants.addOnQty = addOnItemQty;
                                    Constants.addOnId2 = addOnItemId2;
                                    Constants.addOnName2 = addOnItemName2;
                                    Constants.addOnPrice2 = addOnItemPrice2;
                                    Constants.addOnQty2 = addOnItemQty2;
                                }
                            }

                        }else if (count == 2){

                            for (int i = 0; i < addOnItemList.size(); i++) {
                                if (count == 2) {
                                    if (addOnItemList.get(i).tag == 1) {
                                        count = count - 1;
                                        addOnItemId = addOnItemList.get(i).id;
                                        addOnItemName = addOnItemList.get(i).name;
                                        addOnItemPrice = addOnItemList.get(i).offer_price;
                                        addOnItemQty = tvQty.getText().toString();
                                        Constants.addOnId = addOnItemId;
                                        Constants.addOnName = addOnItemName;
                                        Constants.addOnPrice = addOnItemPrice;
                                        Constants.addOnQty = addOnItemQty;
                                    } else {
                                        addOnItemId = "0";
                                        addOnItemName = "";
                                        addOnItemPrice = "0";
                                        addOnItemQty = "0";
                                        Constants.addOnId = addOnItemId;
                                        Constants.addOnName = addOnItemName;
                                        Constants.addOnPrice = addOnItemPrice;
                                        Constants.addOnQty = addOnItemQty;
                                    }
                                } else if (count == 1) {
                                    if (addOnItemList.get(i).tag == 1) {
                                        count = 0;
                                        addOnItemId2 = addOnItemList.get(i).id;
                                        addOnItemName2 = addOnItemList.get(i).name;
                                        addOnItemPrice2 = addOnItemList.get(i).offer_price;
                                        addOnItemQty2 = tvQty.getText().toString();
                                        Constants.addOnId2 = addOnItemId2;
                                        Constants.addOnName2 = addOnItemName2;
                                        Constants.addOnPrice2 = addOnItemPrice2;
                                        Constants.addOnQty2 = addOnItemQty2;
                                    } else {
                                        addOnItemId2 = "0";
                                        addOnItemName2 = "";
                                        addOnItemPrice2 = "0";
                                        addOnItemQty2 = "0";
                                        Constants.addOnId2 = addOnItemId2;
                                        Constants.addOnName2 = addOnItemName2;
                                        Constants.addOnPrice2 = addOnItemPrice2;
                                        Constants.addOnQty2 = addOnItemQty2;
                                    }
                                }
                            }

                        }else if (count == 0){
                            addOnItemId = "0";
                            addOnItemName = "";
                            addOnItemPrice = "0";
                            addOnItemQty = "0";
                            addOnItemId2 = "0";
                            addOnItemName2 = "";
                            addOnItemPrice2 = "0";
                            addOnItemQty2 = "0";
                            Constants.addOnId = addOnItemId;
                            Constants.addOnName = addOnItemName;
                            Constants.addOnPrice = addOnItemPrice;
                            Constants.addOnQty = addOnItemQty;
                            Constants.addOnId2 = addOnItemId2;
                            Constants.addOnName2 = addOnItemName2;
                            Constants.addOnPrice2 = addOnItemPrice2;
                            Constants.addOnQty2 = addOnItemQty2;
                        }



                        Log.d("2nd_ADDON>>", addOnItemId2 + ", " + addOnItemName2 + ", " + addOnItemPrice2 + ", " + addOnItemQty2);
                        Log.d("1st_ADDON>>", addOnItemId + ", " + addOnItemName + ", " + addOnItemPrice + ", " + addOnItemQty);
                        AddToCartRequest addToCartRequest = new AddToCartRequest(
                                prefs.getData(Constants.USER_ID),
                                android_id,
                                im.restaurant_id,
                                im.id,
                                im.name,
                                im.description,
                                image,
                                im.offer_price,
                                tvQty.getText().toString(),
                                addOnItemId,
                                addOnItemName,
                                addOnItemPrice,
                                addOnItemQty,
                                addOnItemId2,
                                addOnItemName2,
                                addOnItemPrice2,
                                addOnItemQty2,
                                "0",
                                "0",
                                "0",
                                im.category_id
                        );

                        Log.d("ADD_CART>>", addToCartRequest.add_on_id + addToCartRequest.add_on_name
                                + addToCartRequest.restaurant_id + addToCartRequest.product_name
                                + addToCartRequest.user_id);

                        addProductToCart(addToCartRequest, im, addOnItemPrice, addOnItemPrice2);
                        im.flag = im.flag+1;

                    }

                } else {
                    int count = 0;
                    for (int i = 0; i < addOnItemList.size(); i++){
                        if (addOnItemList.get(i).tag == 1) {
                            count = count + 1;
                        }
                    }

                    Toast.makeText(getActivity(), ""+count, Toast.LENGTH_SHORT).show();

                    if (count == 2){
                        for (int i = 0; i < addOnItemList.size(); i++) {
                            if (count == 2) {
                                if (addOnItemList.get(i).tag == 1) {
                                    count = count - 1;
                                    addOnItemId = addOnItemList.get(i).id;
                                    addOnItemName = addOnItemList.get(i).name;
                                    addOnItemPrice = addOnItemList.get(i).offer_price;
                                    addOnItemQty = tvQty.getText().toString();
                                    Constants.addOnId = addOnItemId;
                                    Constants.addOnName = addOnItemName;
                                    Constants.addOnPrice = addOnItemPrice;
                                    Constants.addOnQty = addOnItemQty;
                                } else {
                                    addOnItemId = "0";
                                    addOnItemName = "";
                                    addOnItemPrice = "0";
                                    addOnItemQty = "0";
                                    Constants.addOnId = addOnItemId;
                                    Constants.addOnName = addOnItemName;
                                    Constants.addOnPrice = addOnItemPrice;
                                    Constants.addOnQty = addOnItemQty;
                                }
                            } else if (count == 1) {
                                if (addOnItemList.get(i).tag == 1) {
                                    count = 0;
                                    addOnItemId2 = addOnItemList.get(i).id;
                                    addOnItemName2 = addOnItemList.get(i).name;
                                    addOnItemPrice2 = addOnItemList.get(i).price;
                                    addOnItemQty2 = tvQty.getText().toString();
                                    Constants.addOnId2 = addOnItemId2;
                                    Constants.addOnName2 = addOnItemName2;
                                    Constants.addOnPrice2 = addOnItemPrice2;
                                    Constants.addOnQty2 = addOnItemQty2;
                                } else {
                                    addOnItemId2 = "0";
                                    addOnItemName2 = "";
                                    addOnItemPrice2 = "0";
                                    addOnItemQty2 = "0";
                                    Constants.addOnId2 = addOnItemId2;
                                    Constants.addOnName2 = addOnItemName2;
                                    Constants.addOnPrice2 = addOnItemPrice2;
                                    Constants.addOnQty2 = addOnItemQty2;
                                }
                            }else {
                                addOnItemId = "0";
                                addOnItemName = "";
                                addOnItemPrice = "0";
                                addOnItemQty = "0";
                                addOnItemId2 = "0";
                                addOnItemName2 = "";
                                addOnItemPrice2 = "0";
                                addOnItemQty2 = "0";
                            }
                        }
                    }else if (count == 1){
                        for (int i = 0; i < addOnItemList.size(); i++) {
                                if (addOnItemList.get(i).tag == 1) {
                                    count = count - 1;
                                    addOnItemId = addOnItemList.get(i).id;
                                    addOnItemName = addOnItemList.get(i).name;
                                    addOnItemPrice = addOnItemList.get(i).offer_price;
                                    addOnItemQty = tvQty.getText().toString();
                                    addOnItemId2 = "0";
                                    addOnItemName2 = "";
                                    addOnItemPrice2 = "0";
                                    addOnItemQty2 = "0";
                                    Constants.addOnId = addOnItemId;
                                    Constants.addOnName = addOnItemName;
                                    Constants.addOnPrice = addOnItemPrice;
                                    Constants.addOnQty = addOnItemQty;
                                    Constants.addOnId2 = addOnItemId2;
                                    Constants.addOnName2 = addOnItemName2;
                                    Constants.addOnPrice2 = addOnItemPrice2;
                                    Constants.addOnQty2 = addOnItemQty2;
                                } /*else {
                                    addOnItemId = "0";
                                    addOnItemName = "";
                                    addOnItemPrice = "0";
                                    addOnItemQty = "0";
                                    addOnItemId2 = "0";
                                    addOnItemName2 = "";
                                    addOnItemPrice2 = "0";
                                    addOnItemQty2 = "0";
                                    Constants.addOnId = addOnItemId;
                                    Constants.addOnName = addOnItemName;
                                    Constants.addOnPrice = addOnItemPrice;
                                    Constants.addOnQty = addOnItemQty;
                                    Constants.addOnId2 = addOnItemId2;
                                    Constants.addOnName2 = addOnItemName2;
                                    Constants.addOnPrice2 = addOnItemPrice2;
                                    Constants.addOnQty2 = addOnItemQty2;
                                }*/
                        }
                    }



                    Log.d("2nd_ADDON>>", addOnItemId2 + ", " + addOnItemName2 + ", " + addOnItemPrice2 + ", " + addOnItemQty2);
                    Log.d("1st_ADDON2>>", addOnItemId + ", " + addOnItemName + ", " + addOnItemPrice + ", " + addOnItemQty);
                    Constants.addOnForId = im.id;
                    Constants.addOnForName = im.name;
                    Constants.addOnForDescription = im.description;
                    Constants.addOnForPrice = im.price;
                    int qty = Integer.parseInt(tvQty.getText().toString());
                    int temp = qty + 1;
                    Constants.addOnForQty = String.valueOf(temp);

                    AddToCartRequest addToCartRequest = new AddToCartRequest(
                            prefs.getData(Constants.USER_ID),
                            android_id,
                            im.restaurant_id,
                            im.id,
                            im.name,
                            im.description,
                            image,
                            im.offer_price,
                            tvQty.getText().toString(),
                            addOnItemId,
                            addOnItemName,
                            addOnItemPrice,
                            addOnItemQty,
                            addOnItemId2,
                            addOnItemName2,
                            addOnItemPrice2,
                            addOnItemQty2,
                            "0",
                            "0",
                            "0",
                            im.category_id
                    );

                    Log.d("ADD_CART>>", addToCartRequest.add_on_id + addToCartRequest.add_on_name
                            + addToCartRequest.restaurant_id + addToCartRequest.product_name
                            + addToCartRequest.user_id);

                    addProductToCart(addToCartRequest, im, addOnItemPrice, addOnItemPrice2);
                }


                //Constants.TAG = Integer.parseInt(tvQty.getText().toString());

            }
        });


        return v;
    }

    private void updateCartItem(UpdateCartRequest updateCartRequest, ItemsModel im) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.updateCartItem(updateCartRequest).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    CartResponseModel crm = response.body();
                    if (crm.error != true) {

                        CartsModel cm = crm.cart;

                        im.cart_id = cartsModel.id;

                        Constants.add_on_q = Constants.add_on_q + Integer.parseInt(tvQty.getText().toString());

/*
                        Intent i = new Intent(getActivity(), RestaurantDetails.class);
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().startActivity(i);
                        getActivity().overridePendingTransition(0, 0);
                        dismiss();*/
                        // notifyDataSetChanged();


                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(getActivity(), "ERROR!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }

    private void addProductToCart(AddToCartRequest addToCartRequest, ItemsModel mim, String addOnItemPrice, String addOnItemPrice2) {
        dialogView.showCustomSpinProgress(getActivity());
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
                        int price = Integer.parseInt(mim.price);
                        Constants.restIdFromCart = mim.restaurant_id;
                        prefs.saveData(Constants.RestId_FromCart, mim.restaurant_id);
                        Constants.cartID = cartsModel.id;
                        mim.cart_id = cartsModel.id;
                        Constants.addOnCartId = cartsModel.id;
                        prefs.saveData(Constants.CART_ID, cartsModel.id);
                        Constants.cartPrice = Constants.cartPrice + price;
                        Constants.TAG = Constants.TAG + 1;
                        mim.flag = mim.flag + 1;
                        //Constants.add_on_q = Integer.parseInt(tvQty.getText().toString());
                        Constants.add_on_q = Constants.add_on_q + Integer.parseInt(tvQty.getText().toString());
                        addLL.setVisibility(View.GONE);
                        countRL.setVisibility(View.VISIBLE);
                        tvQtyMenu.setText(String.valueOf(Constants.add_on_q));
                        //Toast.makeText(getActivity(), "Cart- " + cartsModel.id , Toast.LENGTH_SHORT).show();

                        if (!addOnItemPrice.equals("") && !addOnItemPrice2.equals("")) {
                            Constants.cartPrice = Constants.cartPrice + Integer.parseInt(addOnItemPrice) + Integer.parseInt(addOnItemPrice2);
                        } else if (!addOnItemPrice.equals("")) {
                            Constants.cartPrice = Constants.cartPrice + Integer.parseInt(addOnItemPrice);
                        } else {

                        }

                        //Constants.isAdded = 1;
                        dismiss();
                        //Intent i = new Intent(((Activity)context), RestaurantDetails.class);
                        //((Activity)context).finish();
                        //((Activity)context).overridePendingTransition(0, 0);
                        //((Activity)context).startActivity(i);
                        //((Activity)context).overridePendingTransition(0, 0);
                        // notifyDataSetChanged();


                    } else {
                        Toast.makeText(getActivity(), "Error->TRUE", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(getActivity(), "Session exp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
