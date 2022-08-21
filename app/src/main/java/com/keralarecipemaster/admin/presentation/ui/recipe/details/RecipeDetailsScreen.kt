package com.keralarecipemaster.admin.presentation.ui.recipe.details

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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.keralarecipemaster.admin.R
import com.keralarecipemaster.admin.presentation.ui.RatingBarView
import com.keralarecipemaster.admin.presentation.viewmodel.RecipeDetailsViewModel
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.RecipeUtil
import com.keralarecipemaster.admin.utils.UserType

@Composable
fun RecipeDetailsScreen(recipeDetailsViewModel: RecipeDetailsViewModel, recipeId: Int) {
    recipeDetailsViewModel.getRecipeDetails(recipeId)
    val lifecycleOwner = LocalLifecycleOwner.current
    val recipe = recipeDetailsViewModel.recipe
    val recipeFlowLifecycleAware = remember(recipe, lifecycleOwner) {
        recipe.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val recipeEntity by recipeFlowLifecycleAware.collectAsState(RecipeUtil.provideRecipe())

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

            if (recipeEntity.addedBy == UserType.ADMIN.name) {
                IconButton(onClick = {
                    //edit
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
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
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
                        mutableStateOf(recipeEntity.rating)
                    },
                    isRatingEditable = false,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray
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
                text = recipeEntity.description,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Italic)
            )

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Ingredients",
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.size(4.dp))

            Text(
                text = recipeEntity.ingredients.toString(),
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Normal)
            )

            Spacer(Modifier.size(20.dp))

            Text(
                text = "Preparation Method",
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.size(4.dp))

            Text(
                text = recipeEntity.preparationMethod,
                style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Normal)
            )
        }
    }
}
