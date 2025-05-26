package com.example.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra("phoneNumber");
        String message = intent.getStringExtra("message");

        if (phoneNumber != null && message != null) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(context, "Reminder SMS Sent", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "SMS failed to send", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
