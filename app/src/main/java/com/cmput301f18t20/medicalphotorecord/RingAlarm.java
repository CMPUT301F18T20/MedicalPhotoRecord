package com.cmput301f18t20.medicalphotorecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class RingAlarm extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {

        Log.d("Alarm", "Please take photos for your problem");
    }
}