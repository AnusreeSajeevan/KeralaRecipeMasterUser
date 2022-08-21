package com.keralarecipemaster.admin.presentation.ui.recipe.details

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
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.admin.utils.Constants
import com.keralarecipemaster.admin.utils.Constants.Companion.INVALID_RECIPE_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsActivity : ComponentActivity() {

    private val viewModel: RecipeDetailsViewModel by viewModels()
    private val addRecipeViewModel: AddRecipeViewModel by viewModels()

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
                    navController = navController,
                    recipeDetailsViewModel = viewModel,
                    recipeId = recipeId,
                    addRecipeViewModel = addRecipeViewModel
                )
            }
        }
//        }
    }
}
