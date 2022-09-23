package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import com.keralarecipemaster.user.network.model.authentication.RegisterResponse
import com.keralarecipemaster.user.network.model.authentication.UserRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/login")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>

    @POST("/login-restaurant-owner")
    suspend fun loginRestaurantOwner(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>

    @POST("/signup")
    suspend fun registerUser(
        @Body registerRequest: UserRegisterRequest
    ): Response<RegisterResponse>
}
