package com.keralarecipemaster.admin.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeScreen(
    viewModel: AddRecipeViewModel,
    onAddButtonClick: () -> Unit,
    navController: NavHostController
) {
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
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = recipeName,
                    label = {
                        Text(text = "Recipe Name *")
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
                        Text(text = "Ingredients (comma separated list) *")
                    },
                    onValueChange = {
                        viewModel.onIngredientsChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = preparationMethod,
                    label = {
                        Text(text = "Preparation Method *")
                    },
                    onValueChange = {
                        viewModel.onPreparationMethodChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShowMealTypeDropDown(viewModel)
                Spacer(modifier = Modifier.height(20.dp))
                ShowDietCategory(viewModel)
                Spacer(modifier = Modifier.height(20.dp))

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
                    onClick = {
                        navController.navigate(AddRecipeDestinations.Restaurant.name)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
private fun ShowDietCategory(viewModel: AddRecipeViewModel) {
    var selectedDietCategory by remember {
        mutableStateOf(Diet.values()[0].name)
    }
    Row {
        Diet.values().forEach {
            RadioButton(
                selected = it.name == selectedDietCategory,
                onClick = {
                    selectedDietCategory = it.name
                    viewModel.onDietCategoryChange(selectedDietCategory)
                }
            )
            Text(text = it.type)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun ShowMealTypeDropDown(viewModel: AddRecipeViewModel) {
    var selectedMealCategory by remember {
        mutableStateOf(Meal.values()[0].name)
    }
    Row {
        Meal.values().forEach {
            RadioButton(
                selected = it.name == selectedMealCategory,
                onClick = {
                    selectedMealCategory = it.name
                    viewModel.onMealCategoryChange(selectedMealCategory)
                }
            )
            Text(text = it.type)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}
