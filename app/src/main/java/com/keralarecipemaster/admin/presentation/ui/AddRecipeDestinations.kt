package com.keralarecipemaster.admin.presentation.ui

enum class AddRecipeDestinations() {
    RecipeDetails,
    Restaurant;

    companion object {
        fun fromRoute(route: String?): AddRecipeDestinations =
            when (route?.substringBefore("/")) {
                RecipeDetails.name -> RecipeDetails
                Restaurant.name -> Restaurant
                null -> RecipeDetails
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
