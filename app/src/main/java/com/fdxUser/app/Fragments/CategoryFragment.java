package com.fdxUser.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Adapters.MenuItemAdapter;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private static final String ARG_COUNT = "param1";
    private Integer counter;
    List<ItemCategoryModel> catList = new ArrayList<>();
    MenuItemAdapter miAdapter;
    List<CustomCartModel> ccmList = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }
    public static CategoryFragment newInstance(Integer counter) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.category_fragment, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvCatName = view.findViewById(R.id.tvCatName);
        TextView tvDes = view.findViewById(R.id.tvDes);
        RecyclerView menuRv = view.findViewById(R.id.menu_rv);

        catList = Constants.cateList;
        ccmList = Constants.custCartList;

        for (int i = 0; i< catList.size(); i++){
            if (i == counter){
                tvCatName.setText(catList.get(i).title);
                tvDes.setText(catList.get(i).description);
                //RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();
                miAdapter = new MenuItemAdapter(catList.get(i).items, ccmList, getActivity());
                menuRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                //menuRv.setRecycledViewPool(sharedPool);
                menuRv.setAdapter(miAdapter);
            }

        }

        /*menuRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();

                if (lastVisibleItemPosition == totalItemCount - 1) {
                    // End of list reached, swipe to next page
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                }
            }
        });*/


    }
}
