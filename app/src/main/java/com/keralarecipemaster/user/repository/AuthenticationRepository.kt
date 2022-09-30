package com.keralarecipemaster.user.repository

import androidx.lifecycle.LiveData
import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import com.keralarecipemaster.user.network.model.authentication.UserInfo
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(username: String, password: String): Flow<Pair<UserInfo?, Int>>
    suspend fun registerUser(username: String, password: String, email: String): Flow<Boolean>
    suspend fun registerRestaurantOwner()
    suspend fun logout()
}
