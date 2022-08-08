package com.keralarecipemaster.admin.presentation.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeActivity : ComponentActivity() {

    private val viewModel: AddRecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddRecipeDetails()
        }
    }

    @Composable
    private fun AddRecipeDetails() {
        val recipeName = viewModel.recipeName.value
        val preparationMethod = viewModel.preparationMethod.value
        val ingredients = viewModel.ingredients.value

        KeralaRecipeMasterAdminTheme {
            Scaffold {
                Column(modifier = Modifier.padding(10.dp).fillMaxWidth()){
                    OutlinedTextField(
                        value = recipeName,
                        label = {
                            Text(text = "Recipe Name")
                        },
                        onValueChange = {
                            viewModel.onRecipeNameChange(it)
                        }
                    )

                    OutlinedTextField(
                        value = preparationMethod,
                        label = {
                            Text(text = "Preparation Method")
                        },
                        onValueChange = {
                            viewModel.onPreparationMethodChange(it)
                        }
                    )

                    OutlinedTextField(
                        value = ingredients,
                        label = {
                            Text(text = "Ingredients")
                        },
                        onValueChange = {
                            viewModel.onIngredientsChange(it)
                        }
                    )
                }
            }
        }
    }
}
