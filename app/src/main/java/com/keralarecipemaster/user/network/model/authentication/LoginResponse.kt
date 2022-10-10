package com.keralarecipemaster.user.network.model.authentication

import com.google.gson.annotations.SerializedName
import com.keralarecipemaster.user.utils.Constants

data class LoginResponse(
    val message: String,
    @SerializedName("user_info") val userInfo: UserInfo
)

data class UserInfo(
    val email: String,
    val username: String,
    val name: String = Constants.EMPTY_STRING,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("usertype") val usertype: String
)
