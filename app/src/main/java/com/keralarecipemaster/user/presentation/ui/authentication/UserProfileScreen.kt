package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.utils.Constants

@Composable
fun UserProfileScreen(
    authenticationViewModel: AuthenticationViewModel,
    locationNotificationViewModel: LocationNotificationViewModel
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
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

    val notificationStatusValue = locationNotificationViewModel.notificationStatus
    val notificationStatusValueLifeCycleAware =
        remember(notificationStatusValue, lifecycleOwner) {
            notificationStatusValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val notificationStatus by notificationStatusValueLifeCycleAware.collectAsState(initial = false)

    if (authenticationState == AuthenticationState.LOGGED_IN_AS_GUEST) {
        Button(onClick = {
            val intent = Intent(activity, AuthenticationActivity::class.java)
            val bundle = Bundle()
            bundle.putBoolean(Constants.IS_FROM_PROFILE_SCREEN, true)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }) {
            Text(text = "Login")
        }
    } else if (authenticationState == AuthenticationState.AUTHENTICATED_USER) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(text = "Turn ${if (notificationStatus) "OFF" else "ON"} Location Notification")
                Spacer(modifier = Modifier.size(10.dp))
                Switch(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    checked = notificationStatus,
                    onCheckedChange = {
                        locationNotificationViewModel.updateNotificationStatus(it)
                    }
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Button(onClick = {
                logout(
                    authenticationViewModel = authenticationViewModel,
                    activity = activity,
                    context = context
                )
            }) {
                Text(text = "Logout")
            }
        }
    }
}

fun logout(
    authenticationViewModel: AuthenticationViewModel,
    activity: Activity?,
    context: Context
) {
    authenticationViewModel.logout()
    activity?.finish()
    context.startActivity(Intent(context, AuthenticationActivity::class.java))
}
