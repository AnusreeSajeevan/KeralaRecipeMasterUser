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

import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.keralarecipemaster.user.R;
import com.keralarecipemaster.user.domain.model.FamousRestaurant;
import com.keralarecipemaster.user.domain.model.util.FamousRestaurants;
import com.keralarecipemaster.user.prefsstore.AuthenticationState;
import com.keralarecipemaster.user.presentation.ui.authentication.AuthenticationActivity;
import com.keralarecipemaster.user.utils.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GoogleService extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    Boolean isNotificationEnabled = false;
    LocationManager locationManager;
    long notify_interval = 50000;
    private Timer mTimer = null;
    Double radius = 50.0;
    Intent intent;
    Location location;
    double latitude, longitude;
    private Handler mHandler = new Handler();
    List<FamousRestaurant> famousRestaurants = Collections.emptyList();
    AuthenticationState authenticationState = AuthenticationState.INITIAL_STATE;
    public static String famousRestaurantsStr = Constants.EMPTY_STRING;
    public static String str_receiver = "krm.service.receiver";
    public static String bigContentTitle = "Nearby Restaurant Alert!";
    public static String channelId = "notify_001";
    public static String channelName = "Channel human readable title";
    public static String contentTitle = "Nearby Restaurant Alert!";


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
        famousRestaurantsStr = bundle.getString(Constants.KEY_FAMOUS_RESTAURANTS);
        authenticationState = AuthenticationState.valueOf(bundle.getString(Constants.KEY_AUTHENTICATION_STATE));
        isNotificationEnabled = bundle.getBoolean(Constants.KEY_IS_NOTIFICATION_ENABLED);
        if (famousRestaurantsStr != null) {
            Gson gson = new Gson();
            FamousRestaurants famousRestaurants = gson.fromJson(famousRestaurantsStr, FamousRestaurants.class);
            this.famousRestaurants = famousRestaurants.getRestaurants();
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
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        for (FamousRestaurant famousRestaurant : this.famousRestaurants) {
                            Location famousLocation = new Location("");
                            famousLocation.setLatitude(famousRestaurant.getLatitude());
                            famousLocation.setLongitude(famousRestaurant.getLongitude());

                            Float distance = location.distanceTo(famousLocation);
                            if (distance < radius) {
                                showNotification(famousRestaurant);
                            } else {
                            }
                        }

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update();
                    }
                }

            }


            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();


                        for (FamousRestaurant famousRestaurant : this.famousRestaurants) {
                            Location famousLocation = new Location("");
                            famousLocation.setLatitude(famousRestaurant.getLatitude());
                            famousLocation.setLongitude(famousRestaurant.getLongitude());

                            Float distance = location.distanceTo(famousLocation);
                            if (distance < radius) {
                                showNotification(famousRestaurant);
                            }
                        }

                        fn_update();
                    }
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
        String bigStr = famousRestaurant.getRestaurantName() + "\n" + famousRestaurant.getRestaurantAddress();
        bigText.bigText(bigStr);
        bigText.setBigContentTitle(bigContentTitle);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this,
                channelId
        );


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(com.keralarecipemaster.user.R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(famousRestaurant.getRestaurantName());
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        mBuilder.setChannelId(channelId);

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
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
            if (authenticationState == AuthenticationState.AUTHENTICATED_USER && isNotificationEnabled) {
                mHandler.post(() -> fn_getlocation());
            }
        }
    }

    private void fn_update() {
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        authenticationState = AuthenticationState.INITIAL_STATE;

    }

    public void stopLocationUpdates() {

//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
//        mGoogleApiClient.disconnect();

    }
}
