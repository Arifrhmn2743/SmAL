package com.example.smal;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //variabel tags
    EditText editText;
    String et;
    String stx;
    private ViewFlipper viewFlipper1,viewFlipper2;
    AlarmManager al_time;
    private TimePicker timepick;
    private TextView textv;
    Context context;
    public PendingIntent penten;
    private TimeReceiver timeReceiver;
    MainActivity inst;
    public static final String sp = "sp";
    public static  final  String tx = "text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utama);
        this.context=this;

     //inisialisasi entity (NAMA JGN KETUKER!!!)
        textv = findViewById(R.id.timetext);
        // Intent
        final Intent n_intent = new Intent(this.context,TimeReceiver.class);
        al_time = (AlarmManager) getSystemService(ALARM_SERVICE);
        //instance untuk waktu
        final Calendar calendar = Calendar.getInstance();
        timepick= findViewById(R.id.timepick);
        viewFlipper1 = findViewById(R.id.viewflip1);
        viewFlipper2= findViewById(R.id.viewflip2);
        editText=findViewById(R.id.edit_t);


     //setting Buttons
        Button start = findViewById(R.id.b_start);
        start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.SECOND,3);
                final int jam = timepick.getHour();
                final int min = timepick.getMinute();
                calendar.set(Calendar.HOUR_OF_DAY,timepick.getCurrentHour());
                calendar.set(Calendar.MINUTE,timepick.getCurrentMinute());
                String s_jam = String.valueOf(jam);
                String s_min= String.valueOf(min);
                if (et == null){
                    et = "tidak ada catatan";
                }
                set_text("Alarm set On "+s_jam+":"+s_min + "\n" + et);
                SharedPreferences shp = getSharedPreferences(sp,MODE_PRIVATE);
                SharedPreferences.Editor editor = shp .edit();
                editor.putString(tx,textv.getText().toString());
                editor.apply();
                n_intent.putExtra("ext","on");
                penten = PendingIntent.getBroadcast(MainActivity.this,0,n_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                al_time.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),penten);
            }
        });
        putext();

        Button stop = findViewById(R.id.b_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penten = PendingIntent.getBroadcast(MainActivity.this,0,n_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                n_intent.putExtra("ext","off");
                sendBroadcast(n_intent);
                al_time.cancel(penten);
                set_text("Need another reminder?");
            }
        });
        Button addnote = findViewById(R.id.lay2);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper1.showNext();
                viewFlipper2.showNext();
            }
        });
        Button added = findViewById(R.id.edit_b);
        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et = editText.getText().toString();
                viewFlipper1.showPrevious();
                viewFlipper2.showPrevious();
            }
        });
    }

    private void putext() {
SharedPreferences sharedPreferences = getSharedPreferences(sp,MODE_PRIVATE);
stx = sharedPreferences.getString(tx,"");
textv.setText(stx);
    }

    //semua method panggilan disini!!
    public void set_text(String text) {
        textv.setText(text);
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }
}
