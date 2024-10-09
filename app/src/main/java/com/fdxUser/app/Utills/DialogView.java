package com.fdxUser.app.Utills;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fdxUser.app.Network.Constants;
import com.fdxUser.app.R;


public class DialogView {

    static ProgressDialog mAlertDialog;

    public static Dialog customSpinProgress, customNoNetwork;

    public DialogView() {}

    public void showSingleButtonDialog(Context context, String header, String msg) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);
        Constants.isDialogOn = 1;

        final AlertDialog alertD = new AlertDialog.Builder(context).create();
        alertD.getWindow().setBackgroundDrawableResource(android.R.color.transparent);*/
        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(header);
        TextView tvMsg=(TextView)dialog.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.isDialogOn = 0;
                //alertD.dismiss();
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isDialogOn = 0;
                //alertD.dismiss();
                dialog.dismiss();

            }
        });

      /*  alertD.setView(promptView);
        try {
            alertD.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
        dialog.show();

    }


    public void errorButtonDialog(Context context, String header, String msg) {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_error_dialog);
        dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_error_dialog, null);*/
        Constants.isDialogOn = 1;

        //final AlertDialog alertD = new AlertDialog.Builder(context).create();
        TextView tvHeader=(TextView)dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(header);
        TextView tvMsg=(TextView)dialog.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setVisibility(View.GONE);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_orange_bg));
        btnOk.setText("Ok");


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.isDialogOn = 0;
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.isDialogOn = 0;
                dialog.dismiss();

            }
        });

       // alertD.setView(promptView);
        //try {
        dialog.show();
        /*}
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/

    }

    public void showSpinProgress(Context context, String msg) {
        mAlertDialog = new ProgressDialog(context);
        mAlertDialog.setMessage(msg);
        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mAlertDialog.show();

    }

    public void showCustomSpinProgress(Context context) {

        customSpinProgress = new Dialog(context);
        customSpinProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customSpinProgress.setContentView(R.layout.dialog_loading);
        customSpinProgress.setCanceledOnTouchOutside(false);
        customSpinProgress.setCancelable(true);
        customSpinProgress.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        WindowManager.LayoutParams wlmp = customSpinProgress.getWindow()
                .getAttributes();
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
        customSpinProgress.show();
    }

    public void dismissCustomSpinProgress() {
        if (customSpinProgress != null)
            customSpinProgress.dismiss();
    }

    public void showHorizontalProgress(Context context, String msg) {
        mAlertDialog = new ProgressDialog(context);
        mAlertDialog.setMessage(msg);
        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setIndeterminate(false);
        mAlertDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mAlertDialog.setProgress(0);
        mAlertDialog.setMax(100);
        mAlertDialog.show();
    }

    public void dismissProgress() {
        if (mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }

}

