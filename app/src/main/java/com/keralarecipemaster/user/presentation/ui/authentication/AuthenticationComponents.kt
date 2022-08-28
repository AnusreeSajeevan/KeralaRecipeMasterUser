package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.home.HomeActivity
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel

@Composable
fun ShowLoginScreen(authenticationViewModel: AuthenticationViewModel, isFromProfileScreen: Boolean = false) {

    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifeCycleOwner = LocalLifecycleOwner.current
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

    val authenticationStateValue = authenticationViewModel.authenticationState
    val authenticationStateValueLifeCycleAware =
        remember(authenticationStateValue, lifeCycleOwner) {
            authenticationStateValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val authenticationState by authenticationStateValueLifeCycleAware.collectAsState(initial = AuthenticationState.INITIAL_STATE)

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        OutlinedTextField(value = username.value, onValueChange = {
            username.value = it
        }, label = { Text(text = "Username") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
        }, label = { Text(text = "Password") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.size(20.dp))
        Button(
            onClick = {
                authenticationViewModel.login()
//                if (authenticationState != AuthenticationState.INITIAL_STATE) {
                    activity?.finish()
//                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        if (!isFromProfileScreen) {
            Button(
                onClick = {
                    authenticationViewModel.continueAsGuestUser()
                    activity?.finish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.continue_as_guest))
            }
        }
    }
}
