package com.keralarecipemaster.user.presentation.ui.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme

class AuthenticationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeralaRecipeMasterUserTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                }
            }
        }
    }
}

@Composable
fun CustomTextField(value: MutableState<String>) {

}

@Preview(showBackground = true)
@Composable
fun ShowLoginScreenPreview() {
    KeralaRecipeMasterUserTheme {
//        ShowLoginScreen(authenticationViewModel)
    }
}
