package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.utils.Constants

@Composable
fun UserProfileScreen(authenticationViewModel: AuthenticationViewModel) {
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
    }
}