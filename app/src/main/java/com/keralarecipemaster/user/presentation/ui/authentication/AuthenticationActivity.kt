package com.keralarecipemaster.user.presentation.ui.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.home.HomeActivity
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {
    val authenticationViewModel: AuthenticationViewModel by viewModels()

    var isFromProfileScreen: Boolean = false

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        isFromProfileScreen = bundle?.getBoolean(Constants.IS_FROM_PROFILE_SCREEN) ?: false

        setContent {
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

            if (authenticationState == AuthenticationState.INITIAL_STATE || isFromProfileScreen) {
                KeralaRecipeMasterUserTheme {
                    val navController = rememberNavController()
                    androidx.compose.material3.Scaffold(modifier = Modifier.fillMaxWidth()) {
                        AuthenticationNavHost(
                            authenticationViewModel = authenticationViewModel,
                            navController = navController,
                            isFromProfileScreen = isFromProfileScreen
                        )
                    }
                }
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ShowLoginScreenPreview() {
        KeralaRecipeMasterUserTheme {
        }
    }
}
