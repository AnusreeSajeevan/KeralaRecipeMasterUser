package com.keralarecipemaster.user.network.model.authentication

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    val message: String,
    @SerializedName("user_info")
    val userInfo: UserInfo
)
