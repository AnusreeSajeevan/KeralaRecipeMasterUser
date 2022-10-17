package com.keralarecipemaster.user.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.keralarecipemaster.user.R;
import com.keralarecipemaster.user.domain.model.FamousRestaurant;
import com.keralarecipemaster.user.domain.model.util.FamousRestaurants;
import com.keralarecipemaster.user.presentation.ui.authentication.AuthenticationActivity;
import com.keralarecipemaster.user.utils.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 50000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    List<FamousRestaurant> famousRestaurants = Collections.emptyList();
    Double radius = 50.0;


    public GoogleService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 100, notify_interval);
        intent = new Intent(str_receiver);
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();
        String famousRestaurantsStr = bundle.getString(Constants.KEY_FAMOUS_RESTAURANTS);
        Log.d("FamousRestaurantsCheck", famousRestaurantsStr);
        if (famousRestaurantsStr != null) {
            Gson gson = new Gson();
            FamousRestaurants famousRestaurants = gson.fromJson(famousRestaurantsStr, FamousRestaurants.class);
            this.famousRestaurants = famousRestaurants.getRestaurants();
            Log.d("FamousRestaurantsCheckk", this.famousRestaurants.toString());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {
            if (isNetworkEnable) {
                Log.d("GoogleServiceLocation", "Network enabled");
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d("GoogleServiceLocation", "Permission Not Granted");
                    return;
                }
                Log.d("GoogleServiceLocation", "PermissionGranted");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("GoogleServiceLocation", location.getLatitude() + "");
                        Log.e("GoogleServiceLocation", location.getLongitude() + "");

                        for (FamousRestaurant famousRestaurant : this.famousRestaurants) {
                            Location famousLocation = new Location("");
                            famousLocation.setLatitude(famousRestaurant.getLatitude());
                            famousLocation.setLongitude(famousRestaurant.getLongitude());

                            Float distance = location.distanceTo(famousLocation);
                            if (distance < radius) {
                                Log.d("GoogleServiceLocation", "Near");
                            } else {
                                Log.d("GoogleServiceLocation", "Far");
                            }
                        }

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable) {
                Log.d("GoogleServiceLocation", "GPS enabled");
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
//                        Log.e("GoogleServiceLocation", "location : " + location.getLatitude());
//                        Log.e("GoogleServiceLocation", "location : " + location.getLongitude());
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();


                        //TODO : show notification
                        for (FamousRestaurant famousRestaurant : this.famousRestaurants) {
                            Location famousLocation = new Location("");
                            //TODO : Phoenix market city
//                            famousLocation.setLatitude(12.995854);
//                            famousLocation.setLongitude(77.696350);
                            famousLocation.setLatitude(famousRestaurant.getLatitude());
                            famousLocation.setLongitude(famousRestaurant.getLongitude());

                            Float distance = location.distanceTo(famousLocation);
                            Log.e("GoogleServiceLocation", "current latitude : " + location.getLatitude());
                            Log.e("GoogleServiceLocation", "current longitude : " + location.getLongitude());
                            Log.e("GoogleServiceLocation", "famous latitude : " + famousLocation.getLatitude());
                            Log.e("GoogleServiceLocation", "famous longitude : " + famousLocation.getLongitude());
                            if (distance < radius) {
                                Log.d("GoogleServiceLocation", "Near");
                                showNotification(famousRestaurant);
                            } else {
                                Log.d("GoogleServiceLocation", "Far");
                            }
                        }


                        fn_update(location);
                    }
                } else {
                    Log.d("GoogleServiceLocation", "locationmanager bull");
                }
            }


        }

    }

    private void showNotification(FamousRestaurant famousRestaurant) {
        Intent ii = new Intent(this, AuthenticationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
        Intent intent = new Intent(this, AuthenticationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("big text");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this,
                "notify_001"
        );

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(com.keralarecipemaster.user.R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Nearby Restaurant Alert!");
        mBuilder.setContentText(famousRestaurant.getRestaurantName());
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setChannelId("1");

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH
            );
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(1, mBuilder.build());
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(() -> fn_getlocation());

        }
    }

    private void fn_update(Location location) {

//        intent.putExtra("GoogleServiceLocation", location.getLatitude() + "");
//        intent.putExtra("GoogleServiceLocation", location.getLongitude() + "");
        sendBroadcast(intent);
    }


}
