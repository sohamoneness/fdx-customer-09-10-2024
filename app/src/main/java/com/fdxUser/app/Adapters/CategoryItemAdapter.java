package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.RestaurantScreens.NewRestaurantDetails;
import com.fdxUser.app.Activity.RestaurantScreens.RestaurantDetails;
import com.fdxUser.app.Models.CustomCartModel;
import com.fdxUser.app.Models.RestaurantDetailsModels.ItemCategoryModel;
import com.fdxUser.app.R;

import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CustomCartModel> ccmList;
    List<ItemCategoryModel> cList;
    Context context;
    MenuItemAdapter miAdapter;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();


    public CategoryItemAdapter(NewRestaurantDetails newRestaurantDetails, List<ItemCategoryModel> categoryList, List<CustomCartModel> customCartDataList) {
        this.ccmList = customCartDataList;
        this.cList = categoryList;
        this.context = newRestaurantDetails;
    }

    public CategoryItemAdapter(RestaurantDetails restaurantDetails, List<ItemCategoryModel> categoryList, List<CustomCartModel> customCartDataList) {
        this.ccmList = customCartDataList;
        this.cList = categoryList;
        this.context = restaurantDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.category_wise_menu_row, parent, false);
                viewHolder = new Hold(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
       /* View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_wise_menu_row, parent, false);
        return new Hold(mView);*/
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemCategoryModel icm = cList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                Hold dataViewHolder = (Hold) holder;
                dataViewHolder.tvCatName.setText(icm.title);
                dataViewHolder.tvDes.setText(icm.description);

                miAdapter = new MenuItemAdapter(icm.items, ccmList, context);
                dataViewHolder.menuRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                dataViewHolder.menuRv.setRecycledViewPool(sharedPool);
                dataViewHolder.menuRv.setAdapter(miAdapter);

                //miAdapter = new MenuItemAdapter(cList.get(0).items, ccmList, context);
//                if (icm.items.size()>0){
//                    //holder.mainLL.setVisibility(View.VISIBLE);
//
//                }else{
//                    //holder.mainLL.setVisibility(View.GONE);
//                }
                //Glide.with(context).load(movie.getImageUrl()).apply(RequestOptions.centerCropTransform()).into(movieViewHolder.movieImage);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == cList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ItemCategoryModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = cList.size() - 1;
        ItemCategoryModel result = getItem(position);

        if (result != null) {
            cList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ItemCategoryModel icm) {
        cList.add(icm);
        notifyItemInserted(cList.size() - 1);
    }

    public void addAll(List<ItemCategoryModel> icmList) {
        for (ItemCategoryModel result : icmList) {
            add(result);
        }
    }

    public ItemCategoryModel getItem(int position) {
        return cList.get(position);
    }


    public class Hold extends RecyclerView.ViewHolder {

        RecyclerView menuRv;
        TextView tvCatName, tvDes;
        LinearLayout mainLL;

        public Hold(@NonNull View itemView) {
            super(itemView);

            menuRv = itemView.findViewById(R.id.menu_rv);
            tvCatName = itemView.findViewById(R.id.tvCatName);
            mainLL = itemView.findViewById(R.id.mainLL);
            tvDes = itemView.findViewById(R.id.tvDes);

        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }

}
