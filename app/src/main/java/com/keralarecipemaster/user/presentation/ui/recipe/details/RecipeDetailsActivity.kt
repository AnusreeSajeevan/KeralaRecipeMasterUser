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
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Constants.Companion.INVALID_RECIPE_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsActivity : ComponentActivity() {

    private val recipeDetailsViewModel: RecipeDetailsViewModel by viewModels()
    private val addRecipeViewModel: AddRecipeViewModel by viewModels()
    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        val recipeId = bundle?.getInt(Constants.KEY_RECIPE_ID) ?: INVALID_RECIPE_ID

        setContent {
            val navController = rememberNavController()
//            if (recipeId != INVALID_RECIPE_ID) {
//                viewModel.getRecipeDetails(recipeId)
            Scaffold(modifier = Modifier.fillMaxWidth()) {
                RecipeDetailsNavHost(
                    recipeId = recipeId,
                    recipeDetailsViewModel = recipeDetailsViewModel,
                    addRecipeViewModel = addRecipeViewModel,
                    authenticationViewModel = authenticationViewModel,
                    navController = navController
                )
            }
        }
//        }
    }
}
