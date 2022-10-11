package com.keralarecipemaster.user.presentation.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.user.presentation.ui.authentication.logout
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.LocationNotificationViewModel
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {
    val locationNotificationViewModel: LocationNotificationViewModel by viewModels()
    val authenticationViewModel: AuthenticationViewModel by viewModels()

    var isFromProfileScreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KeralaRecipeMasterUserTheme {
                SettingsScreen(locationNotificationViewModel, authenticationViewModel)
            }
        }
    }
}

@Composable
fun SettingsScreen(
    locationNotificationViewModel: LocationNotificationViewModel,
    authenticationViewModel: AuthenticationViewModel
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifecycleOwner = LocalLifecycleOwner.current

    val notificationStatusValue = locationNotificationViewModel.notificationStatus
    val notificationStatusValueLifeCycleAware =
        remember(notificationStatusValue, lifecycleOwner) {
            notificationStatusValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val notificationStatus by notificationStatusValueLifeCycleAware.collectAsState(initial = false)

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
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "$name", fontWeight = FontWeight.Bold)
        Text(text = "$email")
        Spacer(modifier = Modifier.size(32.dp))
        Text(text = "Turn ${if (notificationStatus) "OFF" else "ON"} Location Notification to${if (notificationStatus) " stop receiving" else " receive"} notifications when you are close to famous restaurants")
        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Text(
                text = "Location Notification",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Switch(
                modifier = Modifier.align(Alignment.CenterVertically),
                checked = notificationStatus,
                onCheckedChange = {
                }
            )
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}
