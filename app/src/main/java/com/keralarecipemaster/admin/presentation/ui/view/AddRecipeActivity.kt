package com.keralarecipemaster.admin.presentation.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal
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

    @OptIn(ExperimentalMaterialApi::class)
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

        var dietExpanded by remember { mutableStateOf(false) }
        var selectedDiet by remember { mutableStateOf("") }

        val dietValues = Diet.values()

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
                    ShowMealTypeDropDown()
                    Spacer(modifier = Modifier.height(20.dp))
                    ShowDietCategory()
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
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Pick Location")
                    }

                    Button(
                        onClick = {
                            if (viewModel.validateFields()) {
                                viewModel.addRecipe()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@AddRecipeActivity,
                                    "Please enter all mandatory fields",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Add Recipe")
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowDietCategory() {
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
    fun ShowMealTypeDropDown() {
        val mealTypeOptions = arrayListOf<String>()

        Meal.values().forEach {
            mealTypeOptions.add(it.name)
        }

        var mealTypeExpanded by remember { mutableStateOf(false) }
        var selectedMealTypeText by remember { mutableStateOf(mealTypeOptions[0]) }

        ExposedDropdownMenuBox(
            expanded = mealTypeExpanded,
            onExpandedChange = {
                mealTypeExpanded = !mealTypeExpanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedMealTypeText,
                onValueChange = { },
                label = { Text("Meal Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = mealTypeExpanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = mealTypeExpanded,
                onDismissRequest = {
                    mealTypeExpanded = false
                }
            ) {
                mealTypeOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedMealTypeText = selectionOption
                            viewModel.onMealTypeChange(selectedMealTypeText)
                            mealTypeExpanded = false
                        }
                    ) {
                        Text(text = selectionOption)
                    }
                }
            }
        }
    }
}
