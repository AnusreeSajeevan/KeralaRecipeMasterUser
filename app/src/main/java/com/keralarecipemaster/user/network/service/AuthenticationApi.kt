package com.keralarecipemaster.user.network.service

import com.keralarecipemaster.user.network.model.authentication.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
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
