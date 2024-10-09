package com.fdxUser.app.Fragments.BottomSheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.ReviewRestAdapter;
import com.fdxUser.app.Models.RestaurantDetailsModels.RestReviewModel;
import com.fdxUser.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ReviewsBottomSheet extends BottomSheetDialogFragment {
    ImageView ivClose;
    RecyclerView reviewRv;
    public static List<RestReviewModel> reviewList = new ArrayList<>();
    ReviewRestAdapter reviewRestAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reviews_bottom_sheet, container, false);
        ivClose = v.findViewById(R.id.ivClose);
        reviewRv = v.findViewById(R.id.reviewRv);

        reviewRestAdapter = new ReviewRestAdapter(getActivity(), reviewList);
        reviewRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        reviewRv.setAdapter(reviewRestAdapter);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }
}
