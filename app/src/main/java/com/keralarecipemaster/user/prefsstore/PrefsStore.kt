package com.keralarecipemaster.user.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    suspend fun getAuthenticationState(): Flow<String>
    suspend fun updateAuthenticationState(authenticationState: String)
    suspend fun getNotificationStatus(): Flow<Boolean>
    suspend fun setNotificationStatus(status: Boolean)
    suspend fun getUsername(): Flow<String>
    suspend fun setUsername(username: String)
    suspend fun getEmail(): Flow<String>
    suspend fun setEmail(email: String)
    suspend fun getUserId(): Flow<Int>
    suspend fun setUserId(userId: Int)
    suspend fun getName(): Flow<String>
    suspend fun setName(name: String)
}
