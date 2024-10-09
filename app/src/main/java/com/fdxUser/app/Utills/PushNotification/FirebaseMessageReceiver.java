package com.fdxUser.app.Utills.PushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fdxUser.app.R;
import com.fdxUser.app.Utills.DialogView;
import com.fdxUser.app.Utills.Prefs;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    final  static String CHANNEL_ID="1";
    static String TAG="pushdata";
    Prefs prefs;
    DialogView dialogView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        prefs = new Prefs(getApplicationContext());
        prefs.saveData("hasNoti", "y");
        dialogView = new DialogView();
        //Log.d("type>>",remoteMessage.getData().get("type"));
        //Log.d("notification>>",remoteMessage.getNotification().toString());
        String extra = "";
        //String type = remoteMessage.getData().get("type");
        //Constants.notification_type = type;
        //Log.d("type>>",type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (remoteMessage.getData().get("extra").equals("")){
//                extra = "";
//            }else{
//                extra = remoteMessage.getData().get("extra");
//            }
            try{
                issueNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
            }catch (Exception e){
                e.printStackTrace();
            }


            //dialogView.errorButtonDialog(this, getResources().getString(R.string.app_name), remoteMessage.getNotification().getBody());
        }
        else
        {
            addNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void addNotification2aa(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


    void issueNotification(String title,String msg)
    {
        Log.d("In>>","in issue notification");
        Log.d("msg>>",msg);
        //Log.d("extra>>",extra);
        int notificationId = 001;
        Intent viewIntent = new Intent(this, Notification.class);
        //PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                viewIntent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotification2aa("CHANNEL_1", "Echannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
//        NotificationCompat.Builder notification =
//                new NotificationCompat.Builder(this, "CHANNEL_1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        notification
//                //.setSmallIcon(R.drawable.ic_stat_name)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setLargeIcon(bitmap)
//                .setLargeIcon(bitmap)
//                .setAutoCancel(true)
//                .setContentIntent(viewPendingIntent)
//                .setNumber(3);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        assert notificationManager != null;
//        notificationManager.notify(1, notification.build());

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder ( this, "CHANNEL_1" );
        notificationBuilder.setAutoCancel ( true )
                .setStyle ( new NotificationCompat.BigTextStyle ().bigText ( msg ) )
                .setDefaults ( Notification.DEFAULT_ALL )
                .setWhen ( System.currentTimeMillis () )
                .setSmallIcon ( R.drawable.app_icon )
                .setTicker ( title )
                .setPriority ( Notification.PRIORITY_MAX )
                .setContentTitle ( title )
                .setContentText (msg );
        notificationManager.notify ( 1, notificationBuilder.build () );

        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        //Toast toast = Toast.makeText(this, "Toast", Toast.LENGTH_SHORT);
        //toast.show();

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String msg,String title)
    {
        Log.d("In>>","in add notification");
        int notificationId = 001;
        Intent viewIntent = new Intent(this, Notification.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int myColor =getResources().getColor(R.color.colorAccent);
        Notification mNotification =new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.ic_stat_name)
                //.setColor(myColor)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(bitmap)
                .setContentIntent(viewPendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, mNotification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FIRE-TOKEN", token);
        //sendTokenToTheAppServer(token);
    }
}
