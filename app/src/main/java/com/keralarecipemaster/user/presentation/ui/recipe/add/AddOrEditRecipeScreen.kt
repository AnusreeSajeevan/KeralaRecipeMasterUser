package com.keralarecipemaster.user.presentation.ui.recipe.add

import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.*
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.presentation.ui.location.MapAddressPickerView
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.ui.theme.KeralaRecipeMasterUserTheme
import com.keralarecipemaster.user.presentation.viewmodel.AddRecipeViewModel
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.Meal
import com.keralarecipemaster.user.utils.RecipeUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddOrEditRecipeScreen(
    addRecipeViewModel: AddRecipeViewModel,
    authenticationViewModel: AuthenticationViewModel,
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

    /*  // has restaurant details
      val hasRestaurantDetailsValue = addRecipeViewModel.hasRestaurantDetails
      val hasRestaurantDetailsFlowLifeCycleAware =
          remember(hasRestaurantDetailsValue, lifeCycleOwner) {
              hasRestaurantDetailsValue.flowWithLifecycle(
                  lifeCycleOwner.lifecycle,
                  Lifecycle.State.STARTED
              )
          }


      val hasRestaurantDetails by hasRestaurantDetailsFlowLifeCycleAware.collectAsState(false)*/
    val lifecycleOwner = LocalLifecycleOwner.current

    val authenticationStateValue = authenticationViewModel.authenticationState
    val authenticationStateLifeCycleAware = remember(authenticationStateValue, lifeCycleOwner) {
        authenticationStateValue.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val authenticationState by authenticationStateLifeCycleAware.collectAsState(AuthenticationState.INITIAL_STATE)

    val errorMessageValue = addRecipeViewModel.errorMessage
    val errorMessageValueLifeCycleAware =
        remember(errorMessageValue, lifeCycleOwner) {
            errorMessageValue.flowWithLifecycle(
                lifeCycleOwner.lifecycle,
                Lifecycle.State.STARTED
            )
        }
    val errorMessage by errorMessageValueLifeCycleAware.collectAsState(initial = Constants.EMPTY_STRING)

    val context = LocalContext.current
    val activity = (context as? Activity)

    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

        if (errorMessage == "Recipe added successfully" || errorMessage == "Recipe Request submitted for approval!" ||
            errorMessage == "Recipe details updated successfully"
        ) {
            activity?.finish()
        }
        addRecipeViewModel.resetErrorMessage()
    }

    var ingredientName by remember {
        mutableStateOf("")
    }

    var ingredientQuantity by remember {
        mutableStateOf("")
    }

    var hasImage by remember {
        mutableStateOf(false)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                hasImage = it != null
                imageUri = it
                if (Build.VERSION.SDK_INT < 28) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        imageUri
                    )
                    addRecipeViewModel.setImageBitmap(bitmap)
//                    imageView.setImageBitmap(bitmap)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    val bitmap = ImageDecoder.decodeBitmap(source)
//                    imageView.setImageBitmap(bitmap)
                    addRecipeViewModel.setImageBitmap(bitmap)
                }
            }
        }
    )

    KeralaRecipeMasterUserTheme(authenticationState = authenticationState, content = {
        Scaffold {
            Column(
                modifier = Modifier
                    .clipToBounds()
                    .fillMaxWidth()
                    .padding(PaddingValues(8.dp))
                    .verticalScroll(rememberScrollState())
            ) {
                Box {
//                    if (hasImage && imageUri != null) {
                    if (hasImage && imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Selected recipe image",
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (actionType == "edit") {
                        if (addRecipeViewModel.image.isNotEmpty()) {
                            val bitmap =
                                RecipeUtil.getBitmapFromBase64Image(addRecipeViewModel.image)
                            bitmap?.let {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        } else {
                            ShowPlaceholderImage()
                        }
                    } else {
                        ShowPlaceholderImage()
                    }
                    IconButton(onClick = {
                        imagePicker.launch("image/*")
                    }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Image"
                        )
                    }
                }

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

                if (authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
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

                Button(
                    onClick = {
                        if (authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
                            addRecipeViewModel.addRecipeRequest()
                        } else if (authenticationState == AuthenticationState.AUTHENTICATED_USER) {
                            if (actionType == "edit") {
                                addRecipeViewModel.updateRecipe(recipeId)
                            } else {
                                addRecipeViewModel.addRecipe()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (actionType == "edit") "Update Recipe" else {
                            if (authenticationState == AuthenticationState.AUTHENTICATED_USER) {
                                "Add Recipe"
                            } else "Add Recipe Request"
                        }
                    )
                }
            }
        }
    })
}

@Composable
fun ShowPlaceholderImage() {
    Image(
        painter = painterResource(
            id = R.drawable.recipe_place_holder
        ),
        contentDescription = null,
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
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
