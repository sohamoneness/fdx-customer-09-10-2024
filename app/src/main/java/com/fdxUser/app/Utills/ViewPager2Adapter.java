package com.fdxUser.app.Utills;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fdxUser.app.Fragments.CategoryFragment;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;

import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {
    List<ItemCategoryModel> catList;
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, List<ItemCategoryModel> categoryList) {
        super(fragmentActivity);
        this.catList = categoryList;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return CategoryFragment.newInstance(position);
    }
    @Override public int getItemCount() {
        return catList.size();
    }
}
