package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fdxUser.app.Models.SearchModels.SearchCustomModel;
import com.fdxUser.app.R;

import java.util.ArrayList;

public class ProductArrayAdapter extends ArrayAdapter<SearchCustomModel> {
    public ProductArrayAdapter(@NonNull Context context, ArrayList<SearchCustomModel> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_search_row, parent, false);
        }

        // get the position of the view from the ArrayAdapter


        // then return the recyclable view
        return currentItemView;
    }
}
