package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.OrderHistory.OrderSummeryActivity;
import com.fdxUser.app.Activity.OtherScreens.SearchActivity;
import com.fdxUser.app.Activity.RestaurantScreens.CartActivity;
import com.fdxUser.app.Models.CartModels.ClearCartResponseModel;
import com.fdxUser.app.Models.OrderSummeryModels.OrderHistoryListModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderRequestModel;
import com.fdxUser.app.Models.OrderSummeryModels.RepeatOrderResponseModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ResponseRestDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestaurantDetailsRequestModel;
import com.fdxUser.app.Models.RestaurantModels.RestaurantModel;
import com.fdxUser.app.Network.ApiManager;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchOrderHistAdapter extends RecyclerView.Adapter<SearchOrderHistAdapter.Hold> {

    Context context;
    List<OrderHistoryListModel> ohList;
    // List<OrderItemModel> oiList;

    OrderItemAdapter oiAdapter;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    ApiManagerWithAuth manager1 = new ApiManagerWithAuth();
    Prefs prefs;

    public SearchOrderHistAdapter(SearchActivity orderHistory, List<OrderHistoryListModel> orderHistList) {

        this.context = orderHistory;
        this.ohList = orderHistList;
        //this.oiList = orderItemList;

    }

    @NonNull
    @Override
    public SearchOrderHistAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_hist_row, parent, false);
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_past_order_history, parent, false);
        return new SearchOrderHistAdapter.Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchOrderHistAdapter.Hold holder, int position) {

        dialogView = new DialogView();
        prefs = new Prefs(context);
        OrderHistoryListModel ohm = ohList.get(position);

        /*if (position == 0){
            holder.mainRL.setBackground(context.getResources().getDrawable(R.drawable.refer_dash_bg));
            holder.view3.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_separater));
            holder.view1.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_separater));
        }else if (position == 1){
            holder.mainRL.setBackground(context.getResources().getDrawable(R.drawable.ord_hist_block_bg));
            holder.view3.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_orng_sep));
            holder.view1.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_orng_sep));
        }else if (position % 2 > 0){
            holder.mainRL.setBackground(context.getResources().getDrawable(R.drawable.ord_hist_block_bg));
            holder.view3.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_orng_sep));
            holder.view1.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_orng_sep));
        }else{
            holder.mainRL.setBackground(context.getResources().getDrawable(R.drawable.refer_dash_bg));
            holder.view3.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_separater));
            holder.view1.setBackground(context.getResources().getDrawable(R.drawable.dotted_line_separater));
        }*/

//        if (ohm.restaurant.image != null){
//            if (ohm.restaurant.image.equals("")){
//                Glide.with(context).load(R.drawable.ic_no_image).into(holder.rest_img);
//            }else {
//                Glide.with(context).load(ohm.restaurant.image).into(holder.rest_img);
//            }
//        }else {
//            Glide.with(context).load(R.drawable.ic_no_image).into(holder.rest_img);
//        }



        holder.orderId.setText("ID : "+ohm.unique_id);

        //holder.tvRestAddress.setText(ohm.restaurant.address);
        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
        String formatted1 = formatter1.format(Double.parseDouble(ohm.total_amount));
        holder.orderAmount1.setText("\u20B9" + formatted1);
        holder.tv_tot_amount.setText("Total items : "+ohm.total_items);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ohm.created_at);
            String newstring = new SimpleDateFormat("dd MMM, yyyy").format(date);
            //holder.tv_date_time.setText(newstring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //holder.tv_date_time.setText(ohm.created_at);
        if (ohm.status.equals("1")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("New");
        }else if (ohm.status.equals("2")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("3")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("4")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("5")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("6")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("7")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status1));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.black));
            holder.orderStatus.setText("Ongoing");
        }else if (ohm.status.equals("10")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status2));
            holder.orderStatus.setTextColor(context.getResources().getColor(R.color.red3));
            holder.orderStatus.setText("Cancelled");
        }else if (ohm.status.equals("9") || ohm.status.equals("8")){
            holder.li_status_bg.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_status));
            holder.orderStatus.setText("Delivered");
        }
//        else{
//            holder.tvStatus.setText("Ongoing");
//        }

        // holder.tvStatus.setText(ohm.getStatus());

        /*oiAdapter = new OrderItemAdapter(context, ohm.getOrderItemModelList());
        holder.ordItemRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.ordItemRv.setAdapter(oiAdapter);*/

        holder.li_repeat_order_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.restIdFromCart = prefs.getData(Constants.RestId_FromCart);
                Log.d("REST>>", Constants.restIdFromCart);
                Log.d("REST2>>", ohm.restaurant_id);
                Log.d("USER>>", prefs.getData(Constants.USER_ID));
                Log.d("REPEAT>>", ohm.id);
                if (Constants.restIdFromCart.equals(ohm.restaurant_id) || Constants.restIdFromCart.equals("")){
                    RepeatOrderRequestModel repeatOrderRequestModel = new RepeatOrderRequestModel(
                            ohm.id
                    );
                    repeatOrderOrderRequest(repeatOrderRequestModel);
                }else {
                    clearCart(prefs.getData(Constants.USER_ID), ohm);
                }

            }
        });

        holder.tv_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderSummeryActivity.ordId = ohm.id;
                Constants.isFromHomeToSearch = true;
                ((Activity)context).startActivity(new Intent((Activity)context, OrderSummeryActivity.class));
                ((Activity)context).finish();
            }
        });


    }

    private void clearCart(String data, OrderHistoryListModel ohm) {
        dialogView.showCustomSpinProgress(context);
        Log.d("DATA_ID>>", data);
        manager1.service.clearCartData(data).enqueue(new Callback<ClearCartResponseModel>() {
            @Override
            public void onResponse(Call<ClearCartResponseModel> call, Response<ClearCartResponseModel> response) {
                if (response.isSuccessful()) {
                    ClearCartResponseModel clearCartResponseModel = response.body();
                    if (!clearCartResponseModel.error) {
                        dialogView.dismissCustomSpinProgress();
                        RepeatOrderRequestModel repeatOrderRequestModel = new RepeatOrderRequestModel(
                                ohm.id
                        );
                        repeatOrderOrderRequest(repeatOrderRequestModel);

                    } else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(context, "CART NOT CLEAR!", Toast.LENGTH_SHORT).show();

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

    @Override
    public int getItemCount() {
        return ohList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        //        RecyclerView ordItemRv;
//        TextView tvRestName, tvRestAddress, tvStatus;
//        TextView tvTotAmount, tv_date_time;
//        TextView tvRating;
//        RelativeLayout mainRL;
//        ShapeableImageView rest_img;
//        View view1, view2, view3;
        TextView orderId, orderStatus, orderAmount1,tv_tot_amount, tv_date_time;
        LinearLayout li_status_bg,li_repeat_order_bg;

        public Hold(@NonNull View itemView) {
            super(itemView);

//            ordItemRv = itemView.findViewById(R.id.order_item_rv);
//            tvRestName = itemView.findViewById(R.id.tv_rest_name);
//            tvRestAddress = itemView.findViewById(R.id.tv_rest_address);
//            tvStatus = itemView.findViewById(R.id.tv_del_status);
//            tvTotAmount = itemView.findViewById(R.id.tv_tot_amount);
//            tvRating = itemView.findViewById(R.id.tv_rating);
//            mainRL = itemView.findViewById(R.id.main_rl);
//            tv_date_time = itemView.findViewById(R.id.tv_date_time);
//            rest_img = itemView.findViewById(R.id.rest_img);
//            view1 = itemView.findViewById(R.id.view1);
//            view2 = itemView.findViewById(R.id.view2);
//            view3 = itemView.findViewById(R.id.view3);

            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderAmount1 = itemView.findViewById(R.id.orderAmount1);
            tv_tot_amount = itemView.findViewById(R.id.tv_tot_amount);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            li_status_bg = itemView.findViewById(R.id.li_status_bg);
            li_repeat_order_bg = itemView.findViewById(R.id.li_repeat_order_bg);
        }
    }

    private void repeatOrderOrderRequest(RepeatOrderRequestModel repeatOrderRequestModel) {

        dialogView.showCustomSpinProgress(context);

        manager.service.repeatOrder(repeatOrderRequestModel).enqueue(new Callback<RepeatOrderResponseModel>() {
            @Override
            public void onResponse(Call<RepeatOrderResponseModel> call, Response<RepeatOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    RepeatOrderResponseModel repeatOrderResponseModel = response.body();

                    if (!repeatOrderResponseModel.error) {
                        //Toast.makeText(context, "Order Ready!", Toast.LENGTH_SHORT).show();
                        //((Activity)context).startActivity(new Intent((Activity)context, CartActivity.class));
                        //((Activity)context).finish();

                        RestaurantDetailsRequestModel restaurantDetailsRequestModel = new RestaurantDetailsRequestModel(
                                prefs.getData(Constants.USER_ID),
                                repeatOrderResponseModel.restaurant_id
                        );

                        getRestDetails(restaurantDetailsRequestModel, repeatOrderResponseModel.delivery_lat, repeatOrderResponseModel.delivery_lng, "");
                    } else {

                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<RepeatOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    private void getRestDetails(RestaurantDetailsRequestModel restID, String lat, String lng, String isVeg) {
        dialogView.showCustomSpinProgress(context);
        manager1.service.getRestaurantDetails(restID, lat, lng, isVeg).enqueue(new Callback<RestaurantDetailsModel>() {
            @Override
            public void onResponse(Call<RestaurantDetailsModel> call, Response<RestaurantDetailsModel> response) {
                if (response.isSuccessful()) {

                    dialogView.dismissCustomSpinProgress();
                    RestaurantDetailsModel rdm = response.body();

                    if (!rdm.error) {
                        ResponseRestDetailsModel rrdm = rdm.restaurantData;
                        RestaurantModel restM = rrdm.restaurant;
                        Constants.restaurantModel = rrdm.restaurant;

                        Constants.restName = restM.name;
                        Constants.restRating = restM.rating;
                        Constants.REST_ID = restM.id;

                        ((Activity)context).startActivity(new Intent((Activity)context, CartActivity.class));
                        ((Activity)context).finish();
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();

                }
            }

            @Override
            public void onFailure(Call<RestaurantDetailsModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });
    }
}

