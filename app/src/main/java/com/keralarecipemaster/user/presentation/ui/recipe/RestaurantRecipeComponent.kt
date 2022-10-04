package com.keralarecipemaster.user.presentation.ui.recipe

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.domain.model.RecipeEntity
import com.keralarecipemaster.user.presentation.ui.recipe.details.RecipeDetailsActivity
import com.keralarecipemaster.user.presentation.viewmodel.RecipeListViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet
import com.keralarecipemaster.user.utils.RecipeUtil

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RestaurantRecipeComponent(
    recipe: RecipeEntity,
    recipeViewModel: RecipeListViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    Card(onClick = {
        val bundle = Bundle().apply {
            putInt(Constants.KEY_RECIPE_ID, recipe.id)
        }
        val intent = Intent(context, RecipeDetailsActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box {
                val bitmap = RecipeUtil.getBitmapFromBase64Image(recipe.image)
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
                /* IconButton(onClick = {
                     recipeViewModel.deleteRecipe(recipe)
                 }, modifier = Modifier.align(Alignment.BottomEnd)) {
                     Icon(
                         painter = painterResource(
                             id = R.drawable.ic_delete
                         ),
                         contentDescription = null
                     )
                 }*/
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = recipe.recipeName,
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal)
                )
                Text(
                    text = recipe.restaurantName,
                    style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic)
                )
                Text(
                    text = recipe.mealType.type,
                    style = TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Normal)
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
