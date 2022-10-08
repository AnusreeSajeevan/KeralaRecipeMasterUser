package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.network.model.authentication.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(username: String, password: String): Flow<Pair<UserInfo?, Int>>
    suspend fun registerUser(username: String, password: String, email: String): Flow<Boolean>
    suspend fun registerRestaurantOwner(
        username: String,
        password: String,
        email: String,
        restaurantName: String
    ): Flow<Boolean>

    suspend fun logout()
}
