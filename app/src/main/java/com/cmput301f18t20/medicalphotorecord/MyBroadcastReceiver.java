/*
 * Class name: MyBroadcastReceiver
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/2/18 9:45 PM
 *
 * Last Modified: 12/2/18 9:45 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        final Handler handler = new Handler();

        //vibrator for the phone to vibrate
        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        ChannelCreator channelCreator = new ChannelCreator(context);
        NotificationCompat.Builder builder = channelCreator.getChannelNotification(title,message);
        channelCreator.getManager().notify(1,builder.build());

        /*Notification noti = new Notification.Builder(context)
                .setContentTitle("Reminder is ON")
                .setContentText("It is time to update your photo for Problem")
                .setSmallIcon(R.mipmap.ic_launcher).build();

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(0,noti);*/

        //Plays ringtone
        Uri notifcation = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        final Ringtone r = RingtoneManager.getRingtone(context,notifcation);
        r.play();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                r.stop();
            }
        }, 1000 * 8); // runs stop() after 8 seconds
    }
}
