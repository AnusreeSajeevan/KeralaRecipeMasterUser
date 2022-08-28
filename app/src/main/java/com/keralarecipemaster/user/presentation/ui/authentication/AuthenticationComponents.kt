package com.keralarecipemaster.user.presentation.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel

@Composable
fun ShowLoginScreen(authenticationViewModel: AuthenticationViewModel) {
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Button(
            onClick = {
                authenticationViewModel.continueAsGuestUser()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.continue_as_guest))
        }
    }
}
