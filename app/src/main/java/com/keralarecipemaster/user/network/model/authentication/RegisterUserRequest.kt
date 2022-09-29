package com.keralarecipemaster.user.network.model.authentication

import com.keralarecipemaster.user.utils.UserType

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val usertype: String = UserType.USER.value
)
