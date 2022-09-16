package com.keralarecipemaster.user.presentation.ui.recipe.details

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
import com.keralarecipemaster.user.presentation.ui.reciperequests.RecipeRequestDetailsNavHost
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestDetailsViewModel
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeRequestDetailsActivity : ComponentActivity() {
    private val recipeRequestDetailsViewModel: RecipeRequestDetailsViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val recipeRequestId = bundle?.getInt(Constants.KEY_RECIPE_REQUEST_ID) ?: Constants.INVALID_RECIPE_REQUEST_ID

        setContent {
            val navController = rememberNavController()
            Scaffold(modifier = Modifier.fillMaxWidth()) {
                RecipeRequestDetailsNavHost(
                    navController = navController,
                    recipeRequestDetailsViewModel = recipeRequestDetailsViewModel,
                    recipeId = recipeRequestId
                )
            }
        }
    }
}
