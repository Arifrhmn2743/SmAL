package com.example.smal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class TimeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String ext = Objects.requireNonNull(intent.getExtras()).getString("ext");
        Log.e("timereceiver intent :", " Diterima");
        Intent Ring_intent = new Intent(context,Ringtone.class);
        Ring_intent.putExtra("ext",ext);
        context.startService(Ring_intent);
    }
}
