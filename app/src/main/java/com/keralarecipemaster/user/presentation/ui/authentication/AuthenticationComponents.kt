package com.keralarecipemaster.user.presentation.ui.authentication

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.presentation.ui.home.HomeActivity
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.utils.Constants

@OptIn(ExperimentalComposeUiApi::class)
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

    val errorMessageValue = authenticationViewModel.errorMessage
    val errorMessageValueLifeCycleAware =
        remember(errorMessageValue, lifeCycleOwner) {
            errorMessageValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val errorMessage by errorMessageValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        if (errorMessage == "Logged in successfully!") {
            val intent = Intent(
                context,
                HomeActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            activity?.finish()
        }
        authenticationViewModel.resetErrorMessage()
    }

    Column(
        Modifier.padding(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        ConstraintLayout {
            val (loginText, usernameTxt, passwordTxt, loginBtn, buttonsLayout, spacer) = createRefs()
            Text(
                text = "LOGIN",
                modifier = Modifier.constrainAs(loginText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = { Text(text = "Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(usernameTxt) {
                        top.linkTo(loginText.bottom)
                        start.linkTo(loginText.start)
                        end.linkTo(loginText.end)
                    }
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text(text = "Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(passwordTxt) {
                        top.linkTo(usernameTxt.bottom)
                        start.linkTo(usernameTxt.start)
                        end.linkTo(usernameTxt.end)
                    }
            )

            Spacer(Modifier.size(20.dp))
            Button(
                onClick = {
                    if (isAllFieldsValid(username = username, password = password)) {
                        authenticationViewModel.login(username = username, password = password)
                    } else {
                        Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(loginBtn) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(passwordTxt.bottom)
                    }
            ) {
                Text(text = "Continue")
            }

            Spacer(
                Modifier
                    .size(1000.dp)
                    .constrainAs(spacer) {
                        top.linkTo(loginBtn.bottom)
                        bottom.linkTo(buttonsLayout.top)
                    }
            )
            Column(
                modifier = Modifier.constrainAs(buttonsLayout) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            ) {
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
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.register_user))
                }

                Button(
                    onClick = {
                        navController.navigate(AuthenticationDestinations.RegisterRestaurantOwner.name)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.register_restaurant_owner))
                }
            }
        }
    }
}

@Composable
fun ShowUserRegistrationScreen(authenticationViewModel: AuthenticationViewModel) {
    val context = LocalContext.current
    val activity = (context as? Activity)
    val name = remember {
        mutableStateOf("")
    }

    val email = remember {
        mutableStateOf("")
    }

    val userName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "REGISTER AS USER", modifier = Modifier.align(CenterHorizontally))
        Spacer(Modifier.size(16.dp))
        OutlinedTextField(value = name.value, onValueChange = {
            name.value = it
        }, label = { Text(text = "Name") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = email.value, onValueChange = {
            email.value = it
        }, label = { Text(text = "Email") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = userName.value, onValueChange = {
            userName.value = it
        }, label = { Text(text = "Username") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
        }, label = { Text(text = "Password") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                if (isUserDetailsValid(
                        username = userName.value,
                        password = password.value,
                        email = email.value,
                        name = name.value
                    )
                ) {
                    authenticationViewModel.registerUser(
                        username = userName.value,
                        password = password.value,
                        email = email.value,
                        name = name.value
                    )
                } else {
                    Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Continue")
        }
    }
}

@Composable
fun ShowRestaurantOwnerRegistrationScreen(authenticationViewModel: AuthenticationViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    val restaurantName = remember {
        mutableStateOf("")
    }

    val email = remember {
        mutableStateOf("")
    }

    val userName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "REGISTER AS RESTAURANT OWNER", modifier = Modifier.align(CenterHorizontally))
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(value = restaurantName.value, onValueChange = {
            restaurantName.value = it
        }, label = { Text(text = "Restaurant Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email.value, onValueChange = {
            email.value = it
        }, label = { Text(text = "Email") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = userName.value, onValueChange = {
            userName.value = it
        }, label = { Text(text = "Username") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = password.value, onValueChange = {
            password.value = it
        }, label = { Text(text = "Password") }, modifier = Modifier.fillMaxWidth())

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            if (isRestaurantOwnerDetailsValid(
                    username = userName.value,
                    password = password.value,
                    email = email.value,
                    restaurantName = restaurantName.value
                )
            ) {
                authenticationViewModel.registerRestaurantOwner(
                    username = userName.value,
                    password = password.value,
                    email = email.value,
                    restaurantName = restaurantName.value
                )
            } else {
                Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }) {
            Text(text = "Continue")
        }
    }
}

private fun isAllFieldsValid(username: String, password: String): Boolean {
    return username.trim().isNotEmpty() && password.trim().isNotEmpty()
}

fun isUserDetailsValid(username: String, password: String, email: String, name: String): Boolean {
    return username.trim().isNotEmpty() && password.trim().isNotEmpty() && email.trim()
        .isNotEmpty() && name.trim().isNotEmpty()
}

fun isRestaurantOwnerDetailsValid(
    username: String,
    password: String,
    email: String,
    restaurantName: String
): Boolean {
    return username.trim().isNotEmpty() && password.trim().isNotEmpty() && email.trim()
        .isNotEmpty() && restaurantName.trim().isNotEmpty()
}
