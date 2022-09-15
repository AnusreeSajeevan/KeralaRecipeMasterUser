package com.keralarecipemaster.user.repository

import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.network.service.AuthenticationApi
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val prefsStore: PrefsStore,
    private val authenticationApi: AuthenticationApi,
    @CoroutinesDispatchersModule.IoDispatcher val ioDispatcher: CoroutineDispatcher
) : AuthenticationRepository {
    override suspend fun loginAsUser(username: String, password: String): Flow<Boolean> {
        val result = authenticationApi.loginUser(username = username, password = password)
        withContext(ioDispatcher) {
            launch {
                prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
            }
        }
        return flow { emit(result.isSuccessful) }
    }

    override suspend fun loginAsRestaurantOwner() {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser() {
        TODO("Not yet implemented")
    }

    override suspend fun registerRestaurantOwner() {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        prefsStore.updateAuthenticationState(AuthenticationState.INITIAL_STATE.name)
    }
}
