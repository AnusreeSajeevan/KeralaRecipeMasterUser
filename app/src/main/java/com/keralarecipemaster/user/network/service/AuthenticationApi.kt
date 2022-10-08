package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.network.model.authentication.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {
    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("/login-restaurant-owner")
    suspend fun loginRestaurantOwner(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>

    @POST("/signup")
    suspend fun registerUser(
        @Body registerRequest: RegisterUserRequest
    ): Response<RegisterResponse>

    @POST("/signup")
    suspend fun registerRestaurant(
        @Body registerRequest: RegisterRestaurantRequest
    ): Response<RegisterResponse>
}
