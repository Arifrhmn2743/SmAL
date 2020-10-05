package com.example.smal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Objects;

public class Ringtone extends Service {

    MediaPlayer media;
    private boolean alarm_run;
    private int state_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Activites","In ringtone service");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int state_id){
        Log.i("Local Service","Received startid"+state_id+":"+intent);


        final NotificationManager notif =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent main_intent = new Intent(this.getApplicationContext(),MainActivity.class);
        PendingIntent penten = PendingIntent.getActivity(this, 0, main_intent, 0);

        Notification popup = new Notification.Builder(this)
                .setContentTitle("Alarm is ON!")
                .setContentText("Dismiss")
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentIntent(penten)
                .setAutoCancel(true)
                .build();

        String ext = Objects.requireNonNull(intent.getExtras()).getString("ext");

        assert ext != null;
        switch (ext) {
            case "on":
                state_id = 1;
                break;
            case "off":
                state_id = 0;
                break;
            default:
                state_id = 0;
                break;
        }

        //if state_id = 1, off ditekan
        if (!this.alarm_run && state_id == 1) {
            media = MediaPlayer.create(this,R.raw.ringtone);
            media.start();
            notif.notify(0, popup);
            this.alarm_run=true;
            this.state_id = 0;
        }
        else if (!this.alarm_run && state_id == 0){
            Log.e("if there was not sound ", " and you want end");

            this.alarm_run = false;
            this.state_id = 0;

        }

        else if (this.alarm_run && state_id == 1){
            Log.e("if there is sound ", " and you want start");

            this.alarm_run = true;
            this.state_id = 0;

        }
        else {
            Log.e("if there is sound ", " and you want end");

            media.stop();
            media.reset();

            this.alarm_run = false;
            this.state_id = 0;
        }


        Log.e("MyActivity", "In the service");

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy(){
        Log.e("onDestroy called",", task finished");
        super.onDestroy();
        this.alarm_run = false;
    }
}
