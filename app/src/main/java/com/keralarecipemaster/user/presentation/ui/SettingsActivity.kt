package com.keralarecipemaster.user.presentation.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.gson.Gson
import com.keralarecipemaster.user.domain.model.util.FamousRestaurants
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.authentication.LoginScreen
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.service.GoogleService
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    private val locationNotificationViewModel: LocationNotificationViewModel by viewModels()
    val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val authenticationStateValue = authenticationViewModel.authenticationState
            val authenticationStateValueLifeCycleAware =
                remember(authenticationStateValue, lifecycleOwner) {
                    authenticationStateValue.flowWithLifecycle(
                        lifecycleOwner.lifecycle,
                        Lifecycle.State.STARTED
                    )
                }
            val authenticationState by authenticationStateValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)
           Log.d("CheckAuthState", authenticationState.toString())
            KeralaRecipeMasterUserTheme(
                authenticationState =
                    authenticationState,
                content = {
                    SettingsScreen(locationNotificationViewModel, authenticationViewModel)
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    locationNotificationViewModel: LocationNotificationViewModel,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    TurnONOrOFFLocationNotification(
        lifecycleOwner = lifecycleOwner,
        locationNotificationViewModel = locationNotificationViewModel
    )

    val isNotificationEnabledValue = locationNotificationViewModel.isNotificationEnabled
    val isNotificationEnabledValueLifeCycleAware =
        remember(isNotificationEnabledValue, lifecycleOwner) {
            isNotificationEnabledValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val isNotificationEnabled by isNotificationEnabledValueLifeCycleAware.collectAsState(initial = false)

    val isLocationPermissionGrantedValue = locationNotificationViewModel.isLocationPermissionGranted
    val isLocationPermissionGrantedValueLifeCycleAware =
        remember(isLocationPermissionGrantedValue, lifecycleOwner) {
            isLocationPermissionGrantedValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val isLocationPermissionGranted by isLocationPermissionGrantedValueLifeCycleAware.collectAsState(
        initial = false
    )

    val locationPermissionStatusMsgValue = locationNotificationViewModel.locationPermissionStatusMsg
    val locationPermissionStatusMsgValueLifeCycleAware =
        remember(locationPermissionStatusMsgValue, lifecycleOwner) {
            locationPermissionStatusMsgValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val locationPermissionStatusMsg by locationPermissionStatusMsgValueLifeCycleAware.collectAsState(
        initial = Constants.EMPTY_STRING
    )

    val nameValue = authenticationViewModel.name
    val nameValueLifeCycleAware =
        remember(nameValue, lifecycleOwner) {
            nameValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val name by nameValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    val emailValue = authenticationViewModel.email
    val emailValueLifeCycleAware =
        remember(emailValue, lifecycleOwner) {
            emailValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val email by emailValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    val famousRestaurantsValue = locationNotificationViewModel.famousRestaurants
    val famousRestaurantsValueLifeCycleAware =
        remember(famousRestaurantsValue, lifecycleOwner) {
            famousRestaurantsValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val famousRestaurants by famousRestaurantsValueLifeCycleAware.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "$name", fontWeight = FontWeight.Bold)
        Text(text = "$email")
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "Turn ${if (isNotificationEnabled) "OFF" else "ON"} Location Notification to" +
                "${if (isNotificationEnabled) " stop receiving" else " receive"} notifications when you are close to famous restaurants"
        )
        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Text(
                text = "Location Notification",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Switch(
                modifier = Modifier.align(Alignment.CenterVertically),
                checked = isNotificationEnabled,
                onCheckedChange = {
                    if (isLocationPermissionGranted) {
                        locationNotificationViewModel.updateLocationPermissionStatusMsg(Constants.EMPTY_STRING)
                        locationNotificationViewModel.updateNotificationStatus(it)
                        if (it) {
                            if (famousRestaurants.isNotEmpty()) {
                                val intent = Intent(
                                    context.applicationContext,
                                    GoogleService::class.java
                                )
                                val bundle = Bundle()
                                val restaurants = FamousRestaurants(famousRestaurants)
                                bundle.putString(
                                    Constants.KEY_FAMOUS_RESTAURANTS,
                                    Gson().toJson(restaurants)
                                )
                                intent.putExtras(bundle)
                                context.startService(intent)
                            }
                        }
                    } else {
                        if (locationPermissionStatusMsg.isNotEmpty()) {
                            Toast.makeText(context, locationPermissionStatusMsg, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TurnONOrOFFLocationNotification(
    lifecycleOwner: LifecycleOwner,
    locationNotificationViewModel: LocationNotificationViewModel
) {
    val permissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val eventObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(eventObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(eventObserver)
        }
    })

    permissionState.permissions.forEach { it ->
        when (it.permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                if (it.status.isGranted) {
                    /* Permission has been granted by the user.
                    You can use this permission to now acquire the location of the device.
                    You can perform some other tasks here.
                    */
                    locationNotificationViewModel.updateLocationPermissionStatus(true)
                    locationNotificationViewModel.updateLocationPermissionStatusMsg(Constants.EMPTY_STRING)
                } else if (it.status.shouldShowRationale) {
                    // Happens if a user denies the permission two times
                    locationNotificationViewModel.updateLocationPermissionStatus(false)
                    locationNotificationViewModel.updateLocationPermissionStatusMsg("Location permission is needed to enable notifications\nGo back and open settings again!")
                } else if (!it.status.isGranted && !it.status.shouldShowRationale) {
                    /* If the permission is denied and the should not show rationale
                    You can only allow the permission manually through app settings
                    */
                    locationNotificationViewModel.updateLocationPermissionStatus(false)
                    locationNotificationViewModel.updateLocationPermissionStatusMsg("Navigate to settings and enable the Location permission to enable notifications!")
                }
            }
        }
    }
}
