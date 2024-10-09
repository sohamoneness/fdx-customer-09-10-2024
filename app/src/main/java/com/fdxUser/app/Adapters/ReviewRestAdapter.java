package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Models.RestaurantDetailsModels.RestReviewModel;
import com.fdxUser.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReviewRestAdapter extends RecyclerView.Adapter<ReviewRestAdapter.Hold> {

    List<RestReviewModel> rList;
    Context context;

    /*public ReviewRestAdapter(RestaurantDetails restaurantDetails, List<RestReviewModel> reviewList) {
        this.context = restaurantDetails;
        this.rList = reviewList;
    }*/

    public ReviewRestAdapter(FragmentActivity activity, List<RestReviewModel> reviewList) {
        this.context = activity;
        this.rList = reviewList;
    }


    @NonNull
    @Override
    public ReviewRestAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRestAdapter.Hold holder, int position) {

        RestReviewModel rm = rList.get(position);

        float x = Float.valueOf(rm.rating);

        holder.tvUserName.setText(rm.user_name);
        holder.ratingBar.setRating(x);
        holder.tvDate.setText(getFormattedDate(rm.created_at));
        holder.tvRevs.setText(rm.review);

    }

    @Override
    public int getItemCount() {
        return rList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvUserName, tvDate, tvRevs;
        RatingBar ratingBar;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRevs = itemView.findViewById(R.id.tvRevs);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }

    public String getFormattedDate(String fDate) {
        String out = "";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println(sdf.format(cal.getTime()));

        try {
            Date date = sdf.parse(fDate);

            out = sdf1.format(date);

        } catch (ParseException e) {
        }
        return out;
    }
}