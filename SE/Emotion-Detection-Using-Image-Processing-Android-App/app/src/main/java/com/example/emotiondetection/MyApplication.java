package com.example.emotiondetection;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;




public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static MyApplication myApplication;

    private static boolean isActive;
    public static Activity activityOnTop;
    public MediaPlayer mediaPlayer;

    public Bitmap bitmap;



    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        registerActivityLifecycleCallbacks(this);
    }

    public static MyApplication getApplication()
    {
        return myApplication;
    }







    public static boolean isActivityVisible() {
        return isActive;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        System.out.println("==onActivityResumed==");
        isActive = true;
        activityOnTop = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        System.out.println("==onActivityPaused==");
        isActive = false;
        activityOnTop = null;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        System.out.println("==onActivityStopped==");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        System.out.println("==onActivitySaveInstanceState==");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        System.out.println("==onActivityDestroyed==");
    }

    public static AlertDialog alert;
    public static AlertDialog.Builder builder;

//    public static void showApplicationLevelAlert(String tittle, final String msg, boolean playTone) {
//        if (isActive && activityOnTop != null) {
//            if (alert!=null) {
//                alert.dismiss();
//            }
//            builder = new AlertDialog.Builder(activityOnTop);
//            LayoutInflater inflater = activityOnTop.getLayoutInflater();
//            final View dialogView = inflater.inflate(R.layout.layout_azan_alert, null);
//            builder.setView(dialogView);
//            TextView title = (TextView) dialogView.findViewById(R.id.tvTitle);
//            TextView message = (TextView) dialogView.findViewById(R.id.tvBody);
//            Button playBtn = (Button) dialogView.findViewById(R.id.playBtn);
//            Button closeBtn = (Button) dialogView.findViewById(R.id.closeBtn);
//            title.setText(tittle);
//            message.setText(msg);
//
//            alert = builder.create();
//            alert.setCancelable(false);
//
//            playBtn.setOnClickListener(view -> {
//                alert.dismiss();
//                playAlarmTone(MyApplication.getApplication());
//            });
//            closeBtn.setOnClickListener(view -> {
//                alert.dismiss();
//            });
//
//            alert.getWindow().setBackgroundDrawableResource(R.color.transparent);
//            alert.show();
//        } else {
//            System.out.println("===activity is null===");
//        }
//    }

}

