package com.keralarecipemaster.user.network.model.authentication

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.utils.UserType

data class RegisterRestaurantRequest(
    val username: String,
    val password: String,
    val email: String,
    @SerializedName("restaurant_name") val restaurantName: String,
    val usertype: String = UserType.OWNER.value
)
