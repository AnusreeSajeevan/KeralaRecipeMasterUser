package com.keralarecipemaster.user.presentation.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.authentication.AuthenticationActivity
import com.keralarecipemaster.user.presentation.ui.recipe.RecipeApp
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val recipeViewModel: RecipeListViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()
    private val recipeRequestViewModel: RecipeRequestViewModel by viewModels()
    private val locationNotificationViewModel: LocationNotificationViewModel by viewModels()
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        //        @SuppressLint("MissingPermission")

        setContent {
            val navController = rememberNavController()
            val backstackEntry = navController.currentBackStackEntryAsState()
            val currentScreen = HomeItems.fromRoute(backstackEntry.value?.destination?.route)
            val scaffoldState = rememberScaffoldState()
            val lifeCycleOwner = LocalLifecycleOwner.current

            val authenticationStateValue = authenticationViewModel.authenticationState
            val authenticationStateValueLifeCycleAware =
                remember(authenticationStateValue, lifeCycleOwner) {
                    authenticationStateValue.flowWithLifecycle(
                        lifeCycleOwner.lifecycle,
                        Lifecycle.State.STARTED
                    )
                }
            val authenticationState by authenticationStateValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)
            if (authenticationState != AuthenticationState.INITIAL_STATE) {
                RecipeApp(
                    recipeListViewModel = recipeViewModel,
                    authenticationViewModel = authenticationViewModel,
                    recipeRequestViewModel = recipeRequestViewModel,
                    locationNotificationViewModel = locationNotificationViewModel,
                    authenticationState = authenticationState
                )
            } /*else {
                ShowLoginScreen(

                    authenticationViewModel = authenticationViewModel
                )
            }*/
        }
    }

    // method to check for permissions
    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData() {
        // Initializing LocationRequest
        // object with appropriate methods
        val mLocationRequest = com.google.android.gms.location.LocationRequest()
        mLocationRequest.priority = PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest
        // on FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            Log.d(
                "LocationTrackingValues",
                "Latitude : ${mLastLocation.latitude?.toString()}"
            )
            Log.d(
                "LocationTrackingValues",
                "Lomgitude : ${mLastLocation.longitude?.toString()}"
            )
        }
    }

    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {
            // check if location is enabled
            if (isLocationEnabled()) {
                // getting last
                // location from
                // FusedLocationClient
                // object
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                fusedLocationClient?.let {
                    it.lastLocation
                        .addOnCompleteListener { task ->
                            val location = task.result
                            if (location == null) {
                                requestNewLocationData()
                            } else {
                                Log.d(
                                    "LocationTrackingValues",
                                    "Latitude : ${location.latitude}"
                                )
                                Log.d(
                                    "LocationTrackingValues",
                                    "Lomgitude : ${location.longitude}"
                                )

                                val mBuilder = NotificationCompat.Builder(
                                    applicationContext,
                                    "notify_001"
                                )
                                val ii = Intent(
                                    applicationContext,
                                    AuthenticationActivity::class.java
                                )
                                val pendingIntent = PendingIntent.getActivity(this, 0, ii, 0)

                                val bigText = NotificationCompat.BigTextStyle()
                                bigText.bigText("big text")
                                bigText.setBigContentTitle("Today's Bible Verse")
                                bigText.setSummaryText("Text in detail")

                                mBuilder.setContentIntent(pendingIntent)
                                mBuilder.setSmallIcon(com.keralarecipemaster.user.R.mipmap.ic_launcher_round)
                                mBuilder.setContentTitle("Your Title")
                                mBuilder.setContentText("Your text")
                                mBuilder.priority = Notification.PRIORITY_MAX
                                mBuilder.setStyle(bigText)
                                mBuilder.setChannelId("1")

                                val mNotificationManager: NotificationManager =
                                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val channelId = "Your_channel_id"
                                    val channel = NotificationChannel(
                                        channelId,
                                        "Channel human readable title",
                                        NotificationManager.IMPORTANCE_HIGH
                                    )
                                    mNotificationManager.createNotificationChannel(channel)
                                    mBuilder.setChannelId(channelId)
                                }

                                mNotificationManager.notify(1, mBuilder.build())
                            }
                        }
                }
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
                val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            // if permissions aren't available,
            // request for permissions
//                requestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel"
            val descriptionText = "channel "
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
