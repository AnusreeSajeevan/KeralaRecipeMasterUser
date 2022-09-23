package com.keralarecipemaster.user.network.model.authentication

data class UserRegisterRequest(val username: String, val password: String, val email: String, val usertype: String = "user")
