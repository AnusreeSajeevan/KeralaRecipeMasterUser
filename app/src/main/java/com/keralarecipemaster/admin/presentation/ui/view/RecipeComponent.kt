package com.keralarecipemaster.admin.presentation.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keralarecipemaster.admin.R
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.utils.Diet

@Composable
fun RecipeComponent(recipe: Recipe) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(
                id = R.drawable.chicken_biriyani
            ),
            contentDescription = null,
            modifier = Modifier.height(250.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = recipe.recipeName,
            style = TextStyle(fontSize = 16.sp, fontStyle = FontStyle.Normal)
        )
        Text(
            text = recipe.mealType.type,
            style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic)
        )
        Spacer(modifier = Modifier.height(10.dp))

        val dietLogo = if (recipe.diet.type.equals(Diet.NON_VEG.type)) {
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

        Spacer(modifier = Modifier.height(10.dp))

        /*Image(
            painter = painterResource(
                id = R.drawable.ic_veg
            ),
            contentDescription = null,
            modifier = Modifier
                .height(50.dp)
                .width(50.dp),
            contentScale = ContentScale.Crop
        )*/
    }
}
