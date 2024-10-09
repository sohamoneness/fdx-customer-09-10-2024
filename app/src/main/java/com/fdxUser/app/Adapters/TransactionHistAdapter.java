package com.fdxUser.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.WalletModule.WalletActivity;
import com.fdxUser.app.Models.WalletModels.WalletDataModel;
import com.fdxUser.app.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionHistAdapter extends RecyclerView.Adapter<TransactionHistAdapter.Hold> {

    Context context;
    List<WalletDataModel> thList;

    public TransactionHistAdapter(WalletActivity walletActivity, List<WalletDataModel> transHistList) {

        this.context = walletActivity;
        this.thList = transHistList;

    }


    @NonNull
    @Override
    public Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_hist_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold holder, int position) {

        WalletDataModel thm = thList.get(position);
        String transactionDate = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(thm.created_at);
            transactionDate = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (thm.type.equals("1")){
            holder.tvRestName.setText("Credit");
            DecimalFormat formatter = new DecimalFormat("#,##,###.00");
            String formatted = formatter.format(Double.parseDouble(thm.amount));
            holder.tvAmount.setText("+ ₹ " + formatted);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.green));
            holder.ivImg.setImageResource(R.drawable.credit);
        }else{
            holder.tvRestName.setText(thm.comment);
            holder.tvRestName.setSingleLine(false);
            DecimalFormat formatter = new DecimalFormat("#,##,###.00");
            String formatted = formatter.format(Double.parseDouble(thm.amount));
            holder.tvAmount.setText("- ₹ " + formatted);
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.red));
            holder.ivImg.setImageResource(R.drawable.debit);
        }

        holder.tvDate.setText(transactionDate);
    }

    @Override
    public int getItemCount() {
        return thList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {
        TextView tvRestName, tvDate, tvAmount;
        ImageView ivImg;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRestName = itemView.findViewById(R.id.tvRestName);
            ivImg = itemView.findViewById(R.id.ivImg);

        }
    }
}
