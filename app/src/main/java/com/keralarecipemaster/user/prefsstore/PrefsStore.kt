package com.keralarecipemaster.user.prefsstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    fun getAuthenticationState(): Flow<String>
    suspend fun updateAuthenticationState(authenticationState: String)
}