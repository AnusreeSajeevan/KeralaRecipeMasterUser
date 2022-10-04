package com.keralarecipemaster.user.presentation.ui.reciperequests

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestDetailsViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.RecipeUtil

@Composable
fun RecipeRequestDetailsScreen(
    navController: NavController,
    recipeRequestDetailsViewModel: RecipeRequestDetailsViewModel,
    requestId: Int
) {
    recipeRequestDetailsViewModel.getRecipeRequestDetails(requestId = requestId)
    val lifecycleOwner = LocalLifecycleOwner.current
    val recipeRequestValue = recipeRequestDetailsViewModel.recipeRequest
    val recipeFlowLifecycleAware = remember(recipeRequestValue, lifecycleOwner) {
        recipeRequestValue.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val recipeRequest by recipeFlowLifecycleAware.collectAsState(RecipeUtil.provideRecipeRequest())

    val ratingValue = recipeRequestDetailsViewModel.rating
    val ratingFlowLifecycleAware = remember(ratingValue, lifecycleOwner) {
        ratingValue.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val rating by ratingFlowLifecycleAware.collectAsState(0)

    val context = LocalContext.current
    val activity = context as? Activity

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box {

            val bitmap = RecipeUtil.getBitmapFromBase64Image(recipeRequest.image ?: Constants.EMPTY_STRING)
            if (bitmap == null) {
                Image(
                    painter = painterResource(
                        id = R.drawable.placeholder
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(modifier = Modifier.padding(8.dp)) {
            /* Important Recipe Details*/
            Text(
                text = recipeRequest.recipeName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = recipeRequest.mealType.type,
                style = TextStyle(fontSize = 14.sp)
            )

            val dietLogo = if (recipeRequest.diet.type == Diet.NON_VEG.type) {
                R.drawable.ic_non_veg
            } else {
                R.drawable.ic_veg
            }

            Row {
                RatingBarView(
                    rating = remember {
                        mutableStateOf(rating)
                    },
                    isRatingEditable = false,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray,
                    viewModel = recipeRequestDetailsViewModel
                )
                Spacer(modifier = Modifier.size(10.dp))
                Image(
                    painter = painterResource(id = dietLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)
                        .align(Alignment.CenterVertically)

                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = recipeRequest.description,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
            )

            Spacer(Modifier.size(20.dp))
            /* End Important Recipe Details*/

            /* Restaurant Details*/
            Text(
                text = "Restaurant",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))
            Text(text = recipeRequest.restaurantName)
            Text(text = recipeRequest.restaurantAddress)
            Spacer(Modifier.size(20.dp))
            /* End Restaurant Details*/

            /* Other Recipe Details*/
            Text(
                text = "Ingredients",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))

            recipeRequest.ingredients.forEach {
                Text(
                    text = it.name + "  -  " + it.quantity,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(Modifier.size(2.dp))
            }

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Preparation Method",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))

            Text(
                text = recipeRequest.preparationMethod,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            )
            /* End Other Recipe Details*/
        }
    }
}
