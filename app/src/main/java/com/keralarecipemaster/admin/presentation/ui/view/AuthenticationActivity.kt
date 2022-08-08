package com.keralarecipemaster.admin.presentation.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.keralarecipemaster.admin.R
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeralaRecipeMasterAdminTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ShowLoginScreen()
                }
            }
        }
    }
}

@Composable
private fun ShowLoginScreen() {
    val username = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.login))

        CustomTextField(username)
        CustomTextField(password)

        Button(
            onClick = {
                Log.d("AuthActivity", "username : ${username.value}, password : ${password.value}")
            }
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Composable
fun CustomTextField(value: MutableState<String>) {
    OutlinedTextField(value = value.value, onValueChange = {
        value.value = it
    }, label = { Text(text = stringResource(id = R.string.username)) })
}

@Preview(showBackground = true)
@Composable
fun ShowLoginScreenPreview() {
    KeralaRecipeMasterAdminTheme {
        ShowLoginScreen()
    }
}
