package com.keralarecipemaster.admin.utils

import com.keralarecipemaster.admin.domain.model.Ingredient
import com.keralarecipemaster.admin.domain.model.RecipeEntity

class RecipeUtil {
    companion object {
        fun provideRecipe(
            id: Int = 0,
            recipeName: String = "",
            description: String = "",
            ingredients: List<Ingredient> = listOf(),
            image: String? = null,
            restaurantName: String = "",
            restaurantLatitude: String = "",
            restaurantLongitude: String = "",
            restaurantState: String = "",
            preparationMethod: String = "",
            mealType: Meal = Meal.DINNER,
            diet: Diet = Diet.VEG,
            rating: Int = 0
        ): RecipeEntity {
            return RecipeEntity(
                id = id,
                recipeName = recipeName,
                description = description,
                ingredients = ingredients,
                image = image,
                restaurantName = restaurantName,
                restaurantLatitude = restaurantLatitude,
                restaurantLongitude = restaurantLongitude,
                restaurantAddress = restaurantState,
                preparationMethod = preparationMethod,
                mealType = Meal.valueOf(mealType.name),
                diet = Diet.valueOf(diet.name),
                addedBy = UserType.ADMIN.name,
                rating = rating
            )
        }
    }
}