/*
 * Class name: ChannelCreator
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 12/3/18 12:09 AM
 *
 * Last Modified: 12/3/18 12:09 AM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * ChannelCreator class, contains channelID and channelName for notification
 *
 * @version 1.0
 * @see User
 * @see Problem
 * @since 1.0
 */
public class ChannelCreator extends ContextWrapper {

    private NotificationManager nManager;
    public static final String channelID = "666";
    public static final String channelName = "666";

    /**
     * ChannelCreator constructor: automatically create a channel for use
     * @param base
     */
    //This method create the channel if version matched
    public ChannelCreator(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    /**
     * createChannel: it creates a channel for android api >=26
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel = new NotificationChannel(channelID,channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    /**
     * getChannelNotification getter for get the norification builder
     * @param title
     * @param message
     * @return NotificationCompat.Builder
     */
    //Basic Notification
    public NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_reminder)
                .setAutoCancel(true);
    }


    /**
     * NotificationManager getter for get manager
     * @return mManager
     */
    //notification manager
    public NotificationManager getManager() {
        if (nManager == null) {
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nManager;
    }



}
