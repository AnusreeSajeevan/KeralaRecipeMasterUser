package com.keralarecipemaster.user.domain.model

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.network.model.reciperequest.RecipeRequestResponse

data class RecipeRequestResponseWrapper(
    @SerializedName("recipe-requests")
    val recipeRequests: List<RecipeRequestResponse>
)
