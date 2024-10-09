package com.fdxUser.app.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.OtherScreens.Notification;
import com.fdxUser.app.Models.NotificationModels.NotificationModel;
import com.fdxUser.app.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Hold> {

    List<NotificationModel> nList;
    Context context;

    public NotificationAdapter(Notification notification, List<NotificationModel> notificationList) {
        this.context = notification;
        this.nList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.Hold holder, int position) {

        NotificationModel nm = nList.get(position);

        holder.tvHeaderTxt.setText(nm.title);
        holder.tvBodyTxt.setText(Html.fromHtml(nm.description));

    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvHeaderTxt, tvBodyTxt;
        public Hold(@NonNull View itemView) {
            super(itemView);

            tvHeaderTxt = itemView.findViewById(R.id.tvHeaderTxt);
            tvBodyTxt = itemView.findViewById(R.id.tvBodyTxt);

        }
    }
}
