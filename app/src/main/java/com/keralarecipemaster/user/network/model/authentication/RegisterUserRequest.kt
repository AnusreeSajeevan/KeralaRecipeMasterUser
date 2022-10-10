package com.keralarecipemaster.user.network.model.authentication

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.UserType

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val usertype: String = UserType.USER.value,
    @SerializedName("restaurant_name") val restaurantName: String = Constants.EMPTY_STRING,
    val name: String
)
