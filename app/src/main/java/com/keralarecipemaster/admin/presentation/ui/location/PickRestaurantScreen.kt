package com.keralarecipemaster.admin.presentation.ui.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.keralarecipemaster.admin.presentation.ui.theme.KeralaRecipeMasterAdminTheme
import com.keralarecipemaster.admin.presentation.viewmodel.AddRecipeViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PickRestaurantScreen(
    viewModel: AddRecipeViewModel
) {
    val restaurantName = viewModel.restaurantName.value
    val latitude = viewModel.latitude.value
    val longitude = viewModel.longitude.value
    val state = viewModel.state.value
    val hasRestaurantDetails = viewModel.hasRestaurantDetails.value

    val context = LocalContext.current
    val activity = (context as? Activity)

    var hasRestaurantChecked by remember {
        mutableStateOf(false)
    }

    var shouldShowAddRecipeButton by remember {
        mutableStateOf(true)
    }

    KeralaRecipeMasterAdminTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
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

                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = hasRestaurantDetails,
                        onCheckedChange = {
                            hasRestaurantChecked = it
                            viewModel.onRestaurantCheckChange(hasRestaurantChecked)
                            shouldShowAddRecipeButton = !hasRestaurantChecked
                        }
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(text = "Add famous restaurant")
                }

                Button(
                    onClick = {
                        if (hasRestaurantChecked) {
                            if (viewModel.validateRestaurantDetails()) {
                                viewModel.addRecipe()
                                activity?.finish()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Add restaurant details",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            viewModel.addRecipe()
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
