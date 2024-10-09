package com.fdxUser.app.Utills;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Fragments.DeliveryFragment;
import com.fdxUser.app.Fragments.MenuFragment;
import com.fdxUser.app.Fragments.ReviewFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;
    //String uId;



    public TabLayoutAdapter(RestaurantDetails context, FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        mContext = context;
        mTotalTabs = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new MenuFragment();
            case 1:
                return new DeliveryFragment();
            case 2:
                return new ReviewFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
