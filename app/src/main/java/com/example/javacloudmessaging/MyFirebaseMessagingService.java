package com.example.javacloudmessaging;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }
    }

    String channelId = "notification_channel";
    String channelName = "com.example.javacloudmessaging";

    public void generateNotification(String title, String message){

        Intent intent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

//        channel id, channel name
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, channelId)
                .setSmallIcon(R.drawable.chalksnboardlogo)
                .setAutoCancel(true)
                .setVibrate(new long[] {1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        builder = builder.setContent(getRemoteView(title, message));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0,builder.build());

    }

    public RemoteViews getRemoteView(String title, String message){
        @SuppressLint("RemoteViewLayout")
        RemoteViews remoteView = new RemoteViews("com.example.javacloudmessaging", R.layout.notification);
        remoteView.setTextViewText(R.id.tvTitle, title);
        remoteView.setTextViewText(R.id.tvDescription, message);
        remoteView.setImageViewResource(R.id.ivLogo, R.drawable.chalksnboardlogo);

        return remoteView;
    }

}
