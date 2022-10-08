package com.keralarecipemaster.user.presentation.ui.home

enum class HomeItems(
    val value: String
) {
    FamousRecipes(
        value = "Famous Recipes"
    ),
    MyRecipes(
        value = "My Recipes"
    ),
    ApprovedRecipes(
        value = "Approved Recipes"
    ),
    PendingRequests(
        value = "Pending Requests"
    ),
    Profile(
        value = "Profile"
    );

    companion object {
        fun fromRoute(route: String?): HomeItems =
            when (route?.substringBefore("/")) {
                FamousRecipes.name -> FamousRecipes
                MyRecipes.name -> MyRecipes
                Profile.name -> Profile
                ApprovedRecipes.name -> ApprovedRecipes
                PendingRequests.name -> PendingRequests
                null -> FamousRecipes
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
