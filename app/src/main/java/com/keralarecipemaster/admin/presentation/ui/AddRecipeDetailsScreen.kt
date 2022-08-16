package com.keralarecipemaster.admin.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.keralarecipemaster.admin.R
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddRecipeScreen(
    viewModel: AddRecipeViewModel,
    navController: NavHostController
) {
    val recipeName = viewModel.recipeName.value
    val description = viewModel.description.value
    val preparationMethod = viewModel.preparationMethod.value
    val ingredients = viewModel.ingredients.value
    val rating = viewModel.rating.value

    val context = LocalContext.current

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

                RatingBarView(
                    rating = remember {
                        mutableStateOf(0)
                    },
                    isRatingEditable = true,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray,
                    viewModel = viewModel
                )

                Button(
                    onClick = {
                        if (viewModel.validateRecipeDetails()) {
                            navController.navigate(AddRecipeDestinations.Restaurant.name)
                        } else {
                            Toast.makeText(
                                context,
                                "Enter all mandatory fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
