package itstap.edu.homenotification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;

import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static android.Manifest.permission.INTERNET;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String CONFIGURE_ACTION="android.appwidget.action.APPWIDGET_CONFIGURE";
    private Button send;
    public static TextView messagesTV;
    public static EditText messageE;


    public static LinearLayout wrap;
    public static Context context;
    int v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.INTERNET)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("promm", "Permission is granted");
            } else {
                Log.v("promm", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{INTERNET}, PERMISSION_REQUEST_CODE);

            }
        } else {
            Log.v("promm", "Permission is granted");
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
//        stopService(new Intent(this, MyService.class));



        messagesTV = findViewById(R.id.messagesTV);
        send = findViewById(R.id.send);
        messageE = findViewById(R.id.message);
        wrap = findViewById(R.id.wrap);
        send.setOnClickListener(this);
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, MyService.class));
        }

        Intent in = getIntent();
        String f = in.getStringExtra("f");


        if (f != null) {
            Log.e("froSr",f);
            Log.e("f", f);
            TextView tv = new TextView(this);
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(lparams);
            tv.setId(v);
            tv.setPadding(15, 15, 15, 15);
            tv.setBackgroundColor(Color.CYAN);
            tv.setText(f);
            wrap.addView(tv);
            v++;
        }


    }


    public static void newTextView(String st) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lparams);
        tv.setPadding(15, 15, 15, 15);
        tv.setBackgroundColor(Color.CYAN);
        tv.setText(st);
        wrap.addView(tv);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            String f = data.getStringExtra("str");
            Log.e("str", f);

            if (f != null) {
                Log.e("f", f);
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(lparams);
                tv.setPadding(15, 15, 15, 15);
                tv.setBackgroundColor(Color.CYAN);
                tv.setText(f);
                wrap.addView(tv);
            }
        }


    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                String str = messageE.getText().toString();
                TextView tv = new TextView(this);
                Intent intent;
                PendingIntent pi = createPendingResult(1, new Intent(), 5);
                intent = new Intent("itstap.edu.homenotification.MyService.class")
                        .putExtra("str", str)
                        .putExtra("pi", pi);
                intent.setPackage(this.getPackageName());

                int result = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    startService(intent);
                }



                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lparams.gravity = Gravity.RIGHT;
                tv.setLayoutParams(lparams);
                tv.setPadding(15, 15, 15, 15);
                tv.setBackgroundColor(Color.YELLOW);
                tv.setText(str);
                wrap.addView(tv);
                messageE.setText("");

                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static Context getContext() {
        return context;
    }



}
