package com.keralarecipemaster.admin.presentation.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
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
        val description = viewModel.description.value
        val preparationMethod = viewModel.preparationMethod.value
        val ingredients = viewModel.ingredients.value
        val restaurantName = viewModel.restaurantName.value
        val latitude = viewModel.latitude.value
        val longitude = viewModel.longitude.value
        val state = viewModel.state.value

        KeralaRecipeMasterAdminTheme {
            Scaffold {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = recipeName,
                        label = {
                            Text(text = "Recipe Name")
                        },
                        onValueChange = {
                            viewModel.onRecipeNameChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = description,
                        label = {
                            Text(text = "description")
                        },
                        onValueChange = {
                            viewModel.onDescriptionChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ingredients,
                        label = {
                            Text(text = "Ingredients (comma separated list)")
                        },
                        onValueChange = {
                            viewModel.onIngredientsChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = preparationMethod,
                        label = {
                            Text(text = "Preparation Method")
                        },
                        onValueChange = {
                            viewModel.onPreparationMethodChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = restaurantName,
                        label = {
                            Text(text = "Restaurant Name")
                        },
                        onValueChange = {
                            viewModel.onRestaurantNameChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = latitude,
                        label = {
                            Text(text = "latitude")
                        },
                        onValueChange = {
                            viewModel.onLatitudeChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = longitude,
                        label = {
                            Text(text = "longitude")
                        },
                        onValueChange = {
                            viewModel.onLongitudeChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state,
                        label = {
                            Text(text = "state")
                        },
                        onValueChange = {
                            viewModel.onStateChange(it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Pick Location")
                    }

                    Button(
                        onClick = { viewModel.addRecipe() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add Recipe")
                    }
                }
            }
        }
    }
}