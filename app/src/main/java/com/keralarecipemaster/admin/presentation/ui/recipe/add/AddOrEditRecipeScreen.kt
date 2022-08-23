package com.keralarecipemaster.admin.presentation.ui.recipe.add

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.keralarecipemaster.admin.R
import com.keralarecipemaster.admin.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddOrEditRecipeScreen(
    addRecipeViewModel: AddRecipeViewModel,
    navController: NavHostController,
    actionType: String,
    recipeId: Int? = null
) {
    recipeId?.let {
        if (actionType == "edit") {
            addRecipeViewModel.getRecipeDetails(it)
        }
    }

    val lifeCycleOwner = LocalLifecycleOwner.current

    // recipe name
    val recipeNameValue = addRecipeViewModel.recipeName
    val recipeNameFlowLifecycleAware = remember(recipeNameValue, lifeCycleOwner) {
        recipeNameValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val recipeName by recipeNameFlowLifecycleAware.collectAsState("")

    // description
    val descriptionValue = addRecipeViewModel.description
    val descriptionFlowLifecycleAware = remember(descriptionValue, lifeCycleOwner) {
        descriptionValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val description by descriptionFlowLifecycleAware.collectAsState("")

    // preparation method
    val preparationMethodValue = addRecipeViewModel.preparationMethod
    val preparationMethodFlowLifecycleAware = remember(preparationMethodValue, lifeCycleOwner) {
        preparationMethodValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val preparationMethod by preparationMethodFlowLifecycleAware.collectAsState("")

    // ingredients
    val ingredientsValue = addRecipeViewModel.ingredients
    val ingredientsFlowLifeCycleAware = remember(ingredientsValue, lifeCycleOwner) {
        ingredientsValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val ingredients by ingredientsFlowLifeCycleAware.collectAsState("")

    // rating
    val ratingValue = addRecipeViewModel.rating
    val ratingFlowLifeCycleAware = remember(ratingValue, lifeCycleOwner) {
        ratingValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val rating by ratingFlowLifeCycleAware.collectAsState(0)

    // meal type
    val mealTypeValue = addRecipeViewModel.mealType
    val mealTypeFlowLifeCycleAware = remember(mealTypeValue, lifeCycleOwner) {
        mealTypeValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val mealType by mealTypeFlowLifeCycleAware.collectAsState(Meal.BREAKFAST.name)

    // diet type
    val dietTypeValue = addRecipeViewModel.dietType
    val dietTypeFlowLifeCycleAware = remember(dietTypeValue, lifeCycleOwner) {
        dietTypeValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val dietType by dietTypeFlowLifeCycleAware.collectAsState(Diet.VEG.name)

    // restaurant name
    val restaurantNameValue = addRecipeViewModel.restaurantName
    val restaurantNameFlowLifeCycleAware = remember(restaurantNameValue, lifeCycleOwner) {
        restaurantNameValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val restaurantName by restaurantNameFlowLifeCycleAware.collectAsState("")

    // latitude
    val latitudeValue = addRecipeViewModel.latitude
    val latitudeValueFlowLifeCycleAware = remember(latitudeValue, lifeCycleOwner) {
        latitudeValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val latitude by latitudeValueFlowLifeCycleAware.collectAsState("")

    // longitude
    val longitudeValue = addRecipeViewModel.longitude
    val longitudeValueFlowLifeCycleAware = remember(longitudeValue, lifeCycleOwner) {
        longitudeValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val longitude by longitudeValueFlowLifeCycleAware.collectAsState("")

    // state
    val stateValue = addRecipeViewModel.state
    val stateValueFlowLifeCycleAware = remember(stateValue, lifeCycleOwner) {
        stateValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateValueFlowLifeCycleAware.collectAsState("")

    val hasRestaurantDetails = addRecipeViewModel.hasRestaurantDetails.value

    var hasRestaurantChecked by remember {
        mutableStateOf(false)
    }

    var shouldShowAddRecipeButton by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    val activity = (context as? Activity)

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
                        addRecipeViewModel.onRecipeNameChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    label = {
                        Text(text = "description")
                    },
                    onValueChange = {
                        addRecipeViewModel.onDescriptionChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ingredients,
                    label = {
                        Text(text = "Ingredients (comma separated list) *")
                    },
                    onValueChange = {
                        addRecipeViewModel.onIngredientsChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = preparationMethod,
                    label = {
                        Text(text = "Preparation Method *")
                    },
                    onValueChange = {
                        addRecipeViewModel.onPreparationMethodChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                ShowMealTypeDropDown(addRecipeViewModel, mealType)
                Spacer(modifier = Modifier.height(20.dp))
                ShowDietCategory(addRecipeViewModel, dietType)

                RatingBarView(
                    rating = remember {
                        mutableStateOf(rating)
                    },
                    isRatingEditable = true,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray,
                    viewModel = addRecipeViewModel
                )

                OutlinedTextField(
                    value = restaurantName,
                    label = {
                        Text(text = "Restaurant Name")
                    },
                    onValueChange = {
                        addRecipeViewModel.onRestaurantNameChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = latitude,
                    label = {
                        Text(text = "latitude")
                    },
                    onValueChange = {
                        addRecipeViewModel.onLatitudeChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = longitude,
                    label = {
                        Text(text = "longitude")
                    },
                    onValueChange = {
                        addRecipeViewModel.onLongitudeChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state,
                    label = {
                        Text(text = "state")
                    },
                    onValueChange = {
                        addRecipeViewModel.onStateChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = hasRestaurantDetails,
                        onCheckedChange = {
                            hasRestaurantChecked = it
                            addRecipeViewModel.onRestaurantCheckChange(hasRestaurantChecked)
                            shouldShowAddRecipeButton = !hasRestaurantChecked
                        }
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(text = "Add famous restaurant")
                }

                if (shouldShowAddRecipeButton) {
                    Button(
                        onClick = {
                            if (hasRestaurantChecked) {
                                if (addRecipeViewModel.validateRestaurantDetails()) {
                                    if (actionType == "edit") {
                                        addRecipeViewModel.updateRecipe(recipeId)
                                    } else {
                                        addRecipeViewModel.addRecipe()
                                    }
                                    activity?.finish()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Add restaurant details",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                if (actionType == "edit") {
                                    addRecipeViewModel.updateRecipe(recipeId)
                                } else {
                                    addRecipeViewModel.addRecipe()
                                }
                                activity?.finish()
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
}

@Composable
private fun ShowDietCategory(viewModel: AddRecipeViewModel, selectedDietType: String) {
    Row {
        Diet.values().forEach {
            RadioButton(
                selected = it.name == selectedDietType,
                onClick = {
                    viewModel.onDietCategoryChange(it.name)
                }
            )
            Text(text = it.type)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun ShowMealTypeDropDown(viewModel: AddRecipeViewModel, selectedMealType: String) {
    Row {
        Meal.values().forEach {
            RadioButton(
                selected = it.name == selectedMealType,
                onClick = {
                    viewModel.onMealCategoryChange(it.name)
                }
            )
            Text(text = it.type)
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}