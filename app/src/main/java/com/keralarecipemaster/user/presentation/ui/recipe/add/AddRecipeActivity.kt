package com.keralarecipemaster.user.presentation.ui.recipe.add

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeActivity : ComponentActivity() {

    private val viewModel: AddRecipeViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxWidth()) {
                AddOrEditRecipeNavHost(
                    addRecipeViewModel = viewModel,
                    navController = navController,
                    authenticationViewModel = authenticationViewModel
                )
            }
        }
    }
}
