package com.keralarecipemaster.user.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun loginAsUser(username: String, password: String): Flow<Boolean>
    suspend fun loginAsRestaurantOwner(username: String, password: String): Flow<Boolean>
    suspend fun registerUser(username: String, password: String, email: String): Flow<Boolean>
    suspend fun registerRestaurantOwner()
    suspend fun logout()
}
