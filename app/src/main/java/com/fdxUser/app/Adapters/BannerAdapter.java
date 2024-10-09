package com.fdxUser.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.Dashboard;
import com.fdxUser.app.Activity.RestaurantScreens.DashboardHome;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.DashboardModels.BannerModel;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.Hold> {

    List<BannerModel> bannerList;
    Context context;
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;

    public BannerAdapter(Dashboard dashboard, List<BannerModel> bannerList) {
        this.bannerList = bannerList;
        this.context = dashboard;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.context));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public BannerAdapter(DashboardHome dashboardHome, List<BannerModel> bannerList) {
        this.bannerList = bannerList;
        this.context = dashboardHome;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this.context));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        BannerModel bm = bannerList.get(position);

        //Glide.with(context).load(Constants.IMAGE_BASE_URL + bm.image).into(holder.ivBanner);
        //Glide.with(context).load(bm.image).into(holder.ivBanner);
        imageLoader.displayImage(bm.image, holder.ivBanner, options);

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).startActivity(new Intent((Activity) context, RestaurantDetails.class).putExtra(Constants.REST_ID, bm.restaurants_id));
            }
        });

        /*if (bm.image != null){
            Glide.with(context).load(Constants.IMAGE_BASE_URL + bm.image)
                   // .apply(new RequestOptions().placeholder(R.drawable.no_image).error(R.drawable.no_image))
                    .into(holder.ivBanner);
        }else{
            Glide.with(context).load(R.drawable.no_image).into(holder.ivBanner);
        }*/

    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        ShapeableImageView ivBanner;
        RelativeLayout mainView;
        public Hold(@NonNull View itemView) {
            super(itemView);

            ivBanner = itemView.findViewById(R.id.ivBanner);
            mainView = itemView.findViewById(R.id.mainView);

        }
    }
}
