package com.keralarecipemaster.admin.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.keralarecipemaster.admin.domain.model.Recipe
import com.keralarecipemaster.admin.domain.model.Restaurant
import com.keralarecipemaster.admin.ui.view.RecipeComponent
import com.keralarecipemaster.admin.utils.Diet
import com.keralarecipemaster.admin.utils.Meal

@Composable
fun DefaultRecipesScreen() {
    val list = ArrayList<Recipe>()
    for (i in 0..10) {
        list.add(
            Recipe(
                recipeName = "Chicken Biriyani",
                ingredients = listOf("Jeera rice", "Chicken", "Coriander leaves"),
                description = "description",
                preparationMethod = "preparation method",
                mealType = Meal.LUNCH,
                diet = Diet.NON_VEG,
                restaurant = Restaurant(
                    name = "Meghana Foods",
                    state = "Karnataka",
                    latitude = "",
                    longitude = ""
                )
            )
        )
    }

    LazyColumn {
        items(list) { recipe ->
            RecipeComponent(
                recipe
            )
        }
    }
}
