package com.keralarecipemaster.user.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun loginAsUser(username: String, password: String): Flow<Boolean>
    suspend fun loginAsRestaurantOwner()
    suspend fun registerUser()
    suspend fun registerRestaurantOwner()
    suspend fun logout()
}
