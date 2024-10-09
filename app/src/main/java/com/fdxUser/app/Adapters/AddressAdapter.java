package com.fdxUser.app.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fdxUser.app.Activity.Address.AddressList;
import com.fdxUser.app.CustomFonts.ManropeBoldTextView;
import com.fdxUser.app.Models.AddressModels.AddAddressResponseModel;
import com.fdxUser.app.Models.AddressModels.AddressListModel;
import com.fdxUser.app.Network.ApiManagerWithAuth;
import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Hold> {

    List<AddressListModel> addressList;
    Context context;
    ApiManagerWithAuth manager = new ApiManagerWithAuth();
    DialogView dialogView;
    int selectedPosition= 0;

    public AddressAdapter(AddressList adrsActivity, List<AddressListModel> addressList) {

        this.addressList = addressList;
        this.context = adrsActivity;

    }

    @NonNull
    @Override
    public AddressAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.Hold holder, @SuppressLint("RecyclerView") int position) {

        dialogView = new DialogView();

        AddressListModel alm = addressList.get(position);

        if (Constants.isFromHome == true){
            holder.chkBox.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.GONE);
        }else {
            if (Constants.isFor.equals("Select")){
                holder.chkBox.setVisibility(View.VISIBLE);
                holder.ivDelete.setVisibility(View.GONE);
            }else{
                holder.chkBox.setVisibility(View.GONE);
                holder.ivDelete.setVisibility(View.VISIBLE);
            }
        }



        holder.tvName.setText(alm.user.name);
        holder.tvAddressType.setText(alm.tag);
        holder.tvPhn.setText("Contact: " + alm.user.mobile);
        holder.tvAddress.setText(alm.address);
        holder.tvAddress.setMaxLines(2);

        /*holder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.chkBox.isChecked()){
                    alm.flag = 1;
                }else{
                    alm.flag = 0;
                }
            }
        });*/

        if (position == selectedPosition) {
            holder.chkBox.setChecked(true);
            alm.flag = 1;
        } else {
            holder.chkBox.setChecked(false);
            alm.flag = 0;
        }

        // holder.checkBox.setOnClickListener(onStateChangedListener(chk_box, position));
        holder.chkBox.setOnClickListener(onStateChangedListener(holder.chkBox, position));

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteAlertPopup(alm.id, position);

            }
        });

    }

    private View.OnClickListener onStateChangedListener(final CheckBox chk_box, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_box.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    private void showDeleteAlertPopup(String id, int pos) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(context).create();*/
        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(context.getResources().getString(R.string.app_name));
        TextView tvMsg=(TextView)dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Are you sure to remove this address ?");
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAddress(id, pos);

                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.show();

       /* alertD.setView(promptView);
        try {

        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }

    private void deleteAddress(String id, int pos) {
        dialogView.showCustomSpinProgress(context);
        manager.service.deleteAddress(id).enqueue(new Callback<AddAddressResponseModel>() {
            @Override
            public void onResponse(Call<AddAddressResponseModel> call, Response<AddAddressResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    AddAddressResponseModel aarm = response.body();
                    if (aarm.error != true){
                        addressList.remove(pos);
                        notifyDataSetChanged();
                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<AddAddressResponseModel> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvName, tvAddressType, tvPhn;
        ManropeBoldTextView tvAddress;
        ImageView ivDelete;
        CheckBox chkBox;

        public Hold(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvAddressType = itemView.findViewById(R.id.tv_address_type);
            tvPhn = itemView.findViewById(R.id.tv_phone);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            chkBox = itemView.findViewById(R.id.chkBox);
        }
    }
}
