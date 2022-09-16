package com.keralarecipemaster.user.presentation.ui.reciperequests

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.keralarecipemaster.user.R
import com.keralarecipemaster.user.domain.model.RecipeRequestEntity
import com.keralarecipemaster.user.presentation.ui.recipe.RatingBarView
import com.keralarecipemaster.user.presentation.ui.recipe.details.RecipeDetailsActivity
import com.keralarecipemaster.user.presentation.viewmodel.RecipeRequestViewModel
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.Diet

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecipeRequestComponent(
    recipeRequest: RecipeRequestEntity,
    recipeRequestViewModel: RecipeRequestViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    Card(onClick = {
        val bundle = Bundle().apply {
            putInt(Constants.KEY_RECIPE_REQUEST_ID, recipeRequest.requestId)
        }
        val intent = Intent(context, RecipeRequestDetailsActivity::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
//        navController.navigate(RecipeDetailsDestinations.RecipeDetails.name)
    }) {
//        Box {
        Row(modifier = Modifier.height(100.dp).fillMaxWidth()) {
            Image(
                painter = painterResource(
                    id = R.drawable.chicken_biriyani
                ),
                contentDescription = null,
                modifier = Modifier.width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = recipeRequest.recipeName,
                    style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal)
                )

                RatingBarView(
                    rating = remember {
                        mutableStateOf(recipeRequest.rating)
                    },
                    isRatingEditable = false,
                    ratedStarsColor = Color(255, 220, 0),
                    starIcon = painterResource(id = R.drawable.ic_star_filled),
                    unRatedStarsColor = Color.LightGray
                )

                val dietLogo = if (recipeRequest.diet.type == Diet.NON_VEG.type) {
                    R.drawable.ic_non_veg
                } else {
                    R.drawable.ic_veg
                }
                Image(
                    painter = painterResource(id = dietLogo),
                    contentDescription = null,
                    modifier = Modifier
                        .height(15.dp)
                        .width(15.dp)

                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}
// }