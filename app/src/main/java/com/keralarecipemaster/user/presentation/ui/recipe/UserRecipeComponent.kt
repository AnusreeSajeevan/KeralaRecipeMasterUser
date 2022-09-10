package com.keralarecipemaster.user.presentation.ui.recipe

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.presentation.ui.recipe.details.RecipeDetailsActivity
import com.keralarecipemaster.user.presentation.viewmodel.AuthenticationViewModel
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.UserType

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserRecipeComponent(
    recipe: RecipeEntity,
    recipeListViewModel: RecipeListViewModel
) {
    val context = LocalContext.current
    Card(onClick = {
        val bundle = Bundle().apply {
            putInt(Constants.KEY_RECIPE_ID, recipe.id)
        }
        val intent = Intent(context, RecipeDetailsActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
//        navController.navigate(RecipeDetailsDestinations.RecipeDetails.name)
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box {
                Image(
                    painter = painterResource(
                        id = R.drawable.chicken_biriyani
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                if (recipe.addedBy == UserType.USER.name) {
                    IconButton(onClick = {
                        recipeListViewModel.deleteRecipe(recipe)
                    }, modifier = Modifier.align(Alignment.BottomEnd)) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.ic_delete
                            ),
                            contentDescription = null
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = recipe.recipeName,
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal)
                )
                Text(
                    text = recipe.mealType.type,
                    style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic)
                )

                val dietLogo = if (recipe.diet.type == Diet.NON_VEG.type) {
                    R.drawable.ic_non_veg
                } else {
                    R.drawable.ic_veg
                }
                RatingBarView(
                    rating = remember {
                        mutableStateOf(recipe.rating)
                    },
                    isRatingEditable = false,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray
                )
                Image(
                    painter = painterResource(id = dietLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)

                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
