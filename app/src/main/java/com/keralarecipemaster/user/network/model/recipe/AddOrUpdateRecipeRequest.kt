package com.keralarecipemaster.user.network.model.recipe

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.domain.model.RecipeEntity

data class AddOrUpdateRecipeRequest(@SerializedName("user_id") val userId: Int, val recipe: RecipeResponse)
