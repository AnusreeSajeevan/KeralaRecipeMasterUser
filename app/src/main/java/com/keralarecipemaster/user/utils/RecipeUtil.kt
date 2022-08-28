package com.keralarecipemaster.user.utils

import com.keralarecipemaster.user.domain.model.Ingredient
import com.keralarecipemaster.user.domain.model.RecipeEntity

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

        fun generateRecipeDetailsToShare(recipe: RecipeEntity): String {
            var recipeDetails = Constants.EMPTY_STRING
            recipeDetails = recipe.recipeName + "\n\n"
            recipeDetails += "Self rating - " + recipe.rating.toString() + "\n"
            recipeDetails += recipe.diet.type + "\n"
            recipeDetails += recipe.mealType.type + "\n"
            recipeDetails += recipe.description + "\n\n"
            recipeDetails += "Ingredients : " + "\n"

            recipe.ingredients.forEach {
                recipeDetails += it.name + " - " + it.quantity + "\n"
            }

            recipeDetails += "\nPreparation Method\n" + recipe.preparationMethod
            if (recipe.restaurantName != Constants.EMPTY_STRING) {
                recipeDetails += "\n\nFamous Restaurant"
                recipeDetails += "\n" + recipe.restaurantName
                recipeDetails += "\n" + recipe.restaurantAddress
            }
            return recipeDetails
        }
    }
}
