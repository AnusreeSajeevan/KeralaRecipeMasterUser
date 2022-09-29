package com.keralarecipemaster.user.network.model.authentication

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    @SerializedName("user_info") val userInfo: UserInfo
)

data class UserInfo(
    val email: String,
    val username: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("usertype") val usertype: String
)
