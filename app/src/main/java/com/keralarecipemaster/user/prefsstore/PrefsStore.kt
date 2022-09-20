package com.keralarecipemaster.user.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    suspend fun getAuthenticationState(): Flow<String>
    suspend fun updateAuthenticationState(authenticationState: String)
    suspend fun getNotificationStatus(): Flow<Boolean>
    suspend fun setNotificationStatus(status: Boolean)
}