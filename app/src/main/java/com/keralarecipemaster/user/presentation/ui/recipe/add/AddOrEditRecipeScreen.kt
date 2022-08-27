package com.keralarecipemaster.user.presentation.ui.recipe.add

import android.app.Activity
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.presentation.ui.location.MapAddressPickerView
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal

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
        ingredientsValue.flowWithLifecycle(
            lifeCycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val ingredients by ingredientsFlowLifeCycleAware.collectAsState(listOf())

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

    // address
    val addressValue = addRecipeViewModel.address
    val addressFlowLifeCycleAware = remember(addressValue, lifeCycleOwner) {
        addressValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val address by addressFlowLifeCycleAware.collectAsState("")

    // location
    val locationValue = addRecipeViewModel.location
    val locationValueFlowLifeCycleAware = remember(locationValue, lifeCycleOwner) {
        locationValue.flowWithLifecycle(lifeCycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val location by locationValueFlowLifeCycleAware.collectAsState(addRecipeViewModel.getInitialLocation())

    // has restaurant details
    val hasRestaurantDetailsValue = addRecipeViewModel.hasRestaurantDetails
    val hasRestaurantDetailsFlowLifeCycleAware =
        remember(hasRestaurantDetailsValue, lifeCycleOwner) {
            hasRestaurantDetailsValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val hasRestaurantDetails by hasRestaurantDetailsFlowLifeCycleAware.collectAsState(false)
/*
    var hasRestaurantChecked by remember {
        mutableStateOf(false)
    }*/

   /* var shouldShowAddRecipeButton by remember {
        mutableStateOf(true)
    }*/

    val context = LocalContext.current
    val activity = (context as? Activity)

    var ingredientName by remember {
        mutableStateOf("")
    }

    var ingredientQuantity by remember {
        mutableStateOf("")
    }

    KeralaRecipeMasterUserTheme {
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

                Spacer(modifier = Modifier.size(16.dp))
                Text(text = "Ingredients*")

                var str = ""
                if (ingredients.isNotEmpty()) {
                    ingredients.forEach {
                        str = str + it.name + "  -  " + it.quantity + "\n"
                    }
                }

                Text(
                    text = str
                )
                Row {
                    TextField(
                        label = {
                            Text(text = "Name")
                        },
                        value = ingredientName,
                        onValueChange = {
                            ingredientName = it
                        },
                        modifier = Modifier.weight(0.6.toFloat())
                    )
                    Spacer(modifier = Modifier.size(4.dp))

                    TextField(
                        label = {
                            Text(text = "Quantity")
                        },
                        value = ingredientQuantity,
                        onValueChange = {
                            ingredientQuantity = it
                        },
                        modifier = Modifier.weight(0.4.toFloat())
                    )
                }
                Row {
                    Button(onClick = {
                        if (ingredientName.trim().isNotEmpty() && ingredientQuantity.trim()
                            .isNotEmpty()
                        ) {
                            addRecipeViewModel.onAddRecipeClick(
                                ingredientName,
                                ingredientQuantity
                            )
                            ingredientName = ""
                            ingredientQuantity = ""
                        }
                    }) {
                        Text(text = "Save ingredient")
                    }
                    Spacer(Modifier.size(6.dp))
                    Button(onClick = {
                        addRecipeViewModel.clearIngredients()
                        ingredientName = ""
                        ingredientQuantity = ""
                    }) {
                        Text(text = "Clear")
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))

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

                Spacer(modifier = Modifier.size(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = hasRestaurantDetails,
                        onCheckedChange = {
                            addRecipeViewModel.onRestaurantCheckChange(it)
//                            shouldShowAddRecipeButton = !hasRestaurantDetails
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.CenterVertically)
                    )

                    Text(text = "Check this box to add famous restaurant details")
                }

                if (hasRestaurantDetails) {
                    Spacer(modifier = Modifier.size(16.dp))

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
                        value = location.latitude.toString(),
                        label = {
                            Text(text = "latitude")
                        },
                        onValueChange = {
                            addRecipeViewModel.onLatitudeChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )

                    OutlinedTextField(
                        value = location.longitude.toString(),
                        label = {
                            Text(text = "longitude")
                        },
                        onValueChange = {
                            addRecipeViewModel.onLongitudeChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )

                    OutlinedTextField(
                        value = address,
                        label = {
                            Text(text = "Address")
                        },
                        onValueChange = {
                            addRecipeViewModel.onStateChange(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )

                    // Show map to select location
                    MapAddressPickerView(addRecipeViewModel)
                }

//                if (shouldShowAddRecipeButton) {
                Button(
                    onClick = {
                        if (addRecipeViewModel.validateRecipeDetails()) {
                            if (hasRestaurantDetails) {
                                if (addRecipeViewModel.validateRestaurantDetails()) {
                                    if (actionType == "edit") {
                                        addRecipeViewModel.updateRecipe(recipeId)
                                    } else {
                                        addRecipeViewModel.addRecipe()
                                        activity?.finish()
                                    }
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
                                    activity?.finish()
                                }
                            }
                            navController.popBackStack()
                        } else {
                            Toast.makeText(
                                context,
                                "Add all mandatory details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add Recipe")
                }
//                }
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
