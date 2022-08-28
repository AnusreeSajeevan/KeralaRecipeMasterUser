package com.keralarecipemaster.user.presentation.ui.recipe.details

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.keralarecipemaster.user.presentation.ui.recipe.add.AddRecipeDestinations
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.RecipeUtil
import com.keralarecipemaster.user.utils.UserType

@Composable
fun RecipeDetailsScreen(
    recipeId: Int,
    recipeDetailsViewModel: RecipeDetailsViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    recipeDetailsViewModel.getRecipeDetails(recipeId)
    val lifecycleOwner = LocalLifecycleOwner.current
    val recipe = recipeDetailsViewModel.recipe
    val recipeFlowLifecycleAware = remember(recipe, lifecycleOwner) {
        recipe.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val recipeEntity by recipeFlowLifecycleAware.collectAsState(RecipeUtil.provideRecipe())

    val ratingValue = recipeDetailsViewModel.rating
    val ratingFlowLifecycleAware = remember(ratingValue, lifecycleOwner) {
        ratingValue.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val rating by ratingFlowLifecycleAware.collectAsState(0)

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box {
            Image(
                painter = painterResource(
                    id = R.drawable.chicken_biriyani
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            if (recipeEntity.addedBy == UserType.USER.name) {
                IconButton(onClick = {
                    navController.navigate(AddRecipeDestinations.AddRecipeDetails.name)
                }, modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_edit
                        ),
                        contentDescription = null
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = recipeEntity.recipeName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = recipeEntity.mealType.type,
                style = TextStyle(fontSize = 14.sp)
            )

            val dietLogo = if (recipeEntity.diet.type == Diet.NON_VEG.type) {
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
                    viewModel = recipeDetailsViewModel
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

                if (recipeEntity.addedBy == UserType.USER.name) {
                    IconButton(onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, RecipeUtil.generateRecipeDetailsToShare(recipeEntity))
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_share
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = recipeEntity.description,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
            )

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Ingredients",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.size(4.dp))

            recipeEntity.ingredients.forEach {
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
                text = recipeEntity.preparationMethod,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal
                )
            )

            // Restaurant Details
            Spacer(Modifier.size(16.dp))
            Text(
                text = "Famous Restaurant",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 14.sp)
            )
            Text(text = recipeEntity.restaurantName)
            Text(text = recipeEntity.restaurantAddress)
        }
    }
}
