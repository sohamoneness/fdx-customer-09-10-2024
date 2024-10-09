package com.fdxUser.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;

public class ReviewFragment extends Fragment {

    TextView tv_rest_name_rev, tv_rest_rating_rev, tv_no_of_reviews;
    RatingBar rating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_fragment, container, false);

        tv_rest_name_rev = view.findViewById(R.id.tv_rest_name_rev);
        tv_rest_rating_rev = view.findViewById(R.id.tv_rest_rating_rev);
        tv_no_of_reviews = view.findViewById(R.id.tv_no_of_reviews);
        rating = view.findViewById(R.id.rating);

        tv_rest_rating_rev.setText(Constants.restRating);
        tv_rest_name_rev.setText(Constants.restName);
        tv_no_of_reviews.setText(Constants.restTotalReviews + " Reviews");
        rating.setRating(Float.parseFloat(Constants.restRating));


        return view;
    }
}
