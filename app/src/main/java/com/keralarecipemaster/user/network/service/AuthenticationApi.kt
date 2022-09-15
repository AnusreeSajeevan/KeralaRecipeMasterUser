package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/login-user")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>
}
