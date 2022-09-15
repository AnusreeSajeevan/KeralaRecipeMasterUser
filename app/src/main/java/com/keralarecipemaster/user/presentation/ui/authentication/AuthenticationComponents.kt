package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.widget.Toast
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
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.utils.Constants

@Composable
fun LoginScreen(
    authenticationViewModel: AuthenticationViewModel,
    isFromProfileScreen: Boolean = false,
    navController: NavController
) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val lifeCycleOwner = LocalLifecycleOwner.current
    var username by remember {
        mutableStateOf(Constants.EMPTY_STRING)
    }
    var password by remember {
        mutableStateOf(Constants.EMPTY_STRING)
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

    /* if (authenticationState != AuthenticationState.INITIAL_STATE) {
         context.startActivity(Intent(context, HomeActivity::class.java))
         activity?.finish()
     }*/
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        OutlinedTextField(value = username, onValueChange = {
            username = it
        }, label = { Text(text = "Username*") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = { Text(text = "Password*") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.size(20.dp))
        Button(
            onClick = {
                if (isAllFieldsValid(username = username, password = password)) {
                    authenticationViewModel.loginAsUser(username = username, password = password)
                } else {
                    Toast.makeText(context, "Add all mandatory fields", Toast.LENGTH_SHORT).show()
                }

//                if (authenticationState != AuthenticationState.INITIAL_STATE) {
//                activity?.finish()
//                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login_as_user))
        }

        Button(
            onClick = {
                if (isAllFieldsValid(username = username, password = password)) {
                    authenticationViewModel.loginAsRestaurantOwner(
                        username = username,
                        password = password
                    )
                } else {
                    Toast.makeText(context, "Add all mandatory fields", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login_as_restaurant_owner))
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

        Button(
            onClick = {
                navController.navigate(AuthenticationDestinations.RegisterUser.name)
//                activity?.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.register_user))
        }

        Button(
            onClick = {
                navController.navigate(AuthenticationDestinations.RegisterRestaurantOwner.name)
//                activity?.finish()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.register_restaurant_owner))
        }
    }
}

fun isAllFieldsValid(username: String, password: String): Boolean {
    return username.trim().isNotEmpty() && password.trim().isNotEmpty()
}

@Composable
fun ShowUserRegistrationScreen() {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val name = remember {
        mutableStateOf("")
    }

    val userName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = name.value, onValueChange = {
            name.value = it
        }, label = { Text(text = "Name") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = userName.value, onValueChange = {
            userName.value = it
        }, label = { Text(text = "Username") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
        }, label = { Text(text = "Password") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = { activity?.finish() }) {
            Text(text = "Register")
        }
    }
}

@Composable
fun ShowRestaurantOwnerRegistrationScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    val restaurantName = remember {
        mutableStateOf("")
    }

    val restaurantAddress = remember {
        mutableStateOf("")
    }

    val userName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Column {
        OutlinedTextField(value = restaurantName.value, onValueChange = {
            restaurantName.value = it
        }, label = { Text(text = "Name") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = restaurantAddress.value, onValueChange = {
            restaurantAddress.value = it
        }, label = { Text(text = "Restaurant Address") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = userName.value, onValueChange = {
            userName.value = it
        }, label = { Text(text = "Username") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
        }, label = { Text(text = "Password") }, modifier = Modifier.fillMaxWidth())

        Button(onClick = { activity?.finish() }) {
            Text(text = "Register")
        }
    }
}
