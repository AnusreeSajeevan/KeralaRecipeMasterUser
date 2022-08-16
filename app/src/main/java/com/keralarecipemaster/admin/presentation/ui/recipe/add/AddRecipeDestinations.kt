package com.keralarecipemaster.admin.presentation.ui.recipe.add

enum class AddRecipeDestinations() {
    AddRecipeDetails,
    AddRestaurant;

    companion object {
        fun fromRoute(route: String?): AddRecipeDestinations =
            when (route?.substringBefore("/")) {
                AddRecipeDetails.name -> AddRecipeDetails
                AddRestaurant.name -> AddRestaurant
                null -> AddRecipeDetails
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
