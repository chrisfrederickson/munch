package com.felkertech.n.munch.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.felkertech.n.munch.R;
import com.felkertech.n.munch.Stream;
import com.felkertech.n.munch.Utils.SettingsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by N on 3/20/2015.
 */
public class NotificationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private long lastChecked = 0;
    private final long timeDifference = 1000*60*60;
    private Looper mServiceLooper;
    private String TAG = "NotificationService";
    private int NOTEID = 1;
    private Intent intent;
    public static String MEAL_KEY = "MEAL";
    public static String BREAKFAST = "breakfast";
    public static String LUNCH = "lunch";
    public static String DINNER = "dinner";
    private String period = BREAKFAST;

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    GoogleApiClient gapi;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        /*HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);*/
        if(appInstalledOrNot()) {
            gapi = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "Service is Starting");
        //

        //Foreground service
        /*NotificationCompat.Builder foreground = new NotificationCompat.Builder(getApplicationContext());
        foreground.setOngoing(true);
        foreground.setContentTitle("Dream Xplorer");
        foreground.setPriority(-2);
        foreground.setSubText("The service is running");
        startForeground(4, foreground.build());*/
        this.intent = intent;
        Log.d(TAG, "Got Intent");
        Log.d(TAG, intent.getPackage() + "" + intent.getStringExtra(MEAL_KEY));
        period = intent.getStringExtra(MEAL_KEY);
        if(period == null)
            return START_REDELIVER_INTENT;
        try {
            sendNotification();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        //Create a new instance
    }
    public Notification getNotification(String period) {
        //Issue a notification for this period
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext());

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();

        int smallIcon = R.drawable.ic_launcher; //TODO Make white-tinted icon

        //TODO Get recommended food
        mBuilder.setContentTitle("Ready for "+period+"?")
                .setContentText("Try something rich in protein") /* TODO Generate important */
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Try something rich in protein, like chicken, and have a side high in fiber like an apple") /* TODO Generate Menu */
                        .setBigContentTitle("Ready for "+period+"?")
                        .setSummaryText("Munch"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(), smallIcon)) /* TODO Get key food icon */
                .setPriority(0);

        wearableExtender.setBackground(BitmapFactory.decodeResource(
                getResources(), smallIcon)); /* TODO Get generic background */

        mBuilder.extend(wearableExtender);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setVisibility(Notification.VISIBILITY_PRIVATE);
        }
        Notification n = mBuilder.build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            n.color = Color.GREEN;

        }
        return n;
    }
    public Notification getNotification2() {
        //Issue a notification for this period
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext());

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();
        ArrayList<String> cT = new ArrayList<String>();
        ArrayList<String> bT = new ArrayList<String>();
        int wearBG = R.drawable.ic_launcher;
        int largeIcon = R.drawable.ic_launcher;
        // Create an intent for the reply action
        Intent actionIntent = new Intent(this, NotificationService.class);
        actionIntent.putExtra("wearNote", true);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        "Get Weather", actionPendingIntent)
                        .build();
        wearableExtender.addAction(action);
        //Large Icons thanks to https://www.iconfinder.com/iconsets/weather-icons-8
//        Log.d(TAG, d+"");

        //Notification actions
        wearableExtender.setHintHideIcon(true);

        mBuilder.setPriority(1)
                .extend(wearableExtender)
                .setContentTitle("Weather Update")
                .setContentText("Text")/*
            .setSubText("Weather Delta")*/
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Bigtext"))
                .setSmallIcon(largeIcon)/*
            .setLargeIcon(BitmapFactory.decodeResource(
                    getResources(), largeIcon))*/
                .setDefaults(Notification.DEFAULT_ALL);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, Stream.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(Stream.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);

        int noteId = 1;
        // Gets an instance of the NotificationManager service
        Notification n =  mBuilder.build();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            n.color = Color.RED;
        }
        return n;
    }
    public void sendNotification() throws JSONException {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //TODO Issue
        mNotifyMgr.notify(1, getNotification(period));
    }
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        Log.d(TAG, "Got Activity Result "+requestCode+", "+resultCode);
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                Log.d(TAG, "Got CONNECTION_FAILURE_RESOLUTION_REQUEST");
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        Log.d(TAG, "Activity.RESULT_OK");
                        Toast.makeText(getApplicationContext(), "Cannot Connect to Google Play Services", Toast.LENGTH_SHORT);
                        break;
                }
        }
    }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
//            Log.d("Location Updates",
//                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services but we can't b/c it requires activity
            // This just fails.
        }
        return false;
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        //CONNECTED
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    private boolean appInstalledOrNot() {
        return appInstalledOrNot("com.google.android.wearable.app");
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

