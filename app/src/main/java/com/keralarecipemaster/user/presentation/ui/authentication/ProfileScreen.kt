package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.service.GoogleService
import com.keralarecipemaster.user.utils.Constants

@Composable
fun UserProfileScreen(
    authenticationViewModel: AuthenticationViewModel
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

    val emailValue = authenticationViewModel.email
    val emailValueLifeCycleAware =
        remember(emailValue, lifecycleOwner) {
            emailValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val email by emailValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    val nameValue = authenticationViewModel.name
    val nameValueLifeCycleAware =
        remember(nameValue, lifecycleOwner) {
            nameValue.flowWithLifecycle(
                lifecycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val name by nameValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    when (authenticationState) {
        AuthenticationState.LOGGED_IN_AS_GUEST -> {
            Button(onClick = {
                val intent = Intent(activity, AuthenticationActivity::class.java)
                val bundle = Bundle()
                bundle.putBoolean(Constants.IS_FROM_PROFILE_SCREEN, true)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }) {
                Text(text = "Login")
            }
        }
        AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER -> {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                Text(text = "$name", fontWeight = FontWeight.Bold)
                Text(text = "$email")

                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = {
                    logout(
                        authenticationViewModel = authenticationViewModel,
                        activity = activity,
                        context = context
                    )
                }) {
                    Text(
                        text = "Logout",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        else -> {
        }
    }
}

fun logout(
    authenticationViewModel: AuthenticationViewModel,
    activity: Activity?,
    context: Context
) {
    authenticationViewModel.logout()
    val myService = Intent(context, GoogleService::class.java)
    context.stopService(myService)
    activity?.finish()
    val intent = Intent(context, AuthenticationActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    context.startActivity(intent)
}
