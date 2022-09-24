package com.keralarecipemaster.user.repository

import android.util.Log
import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.network.model.authentication.LoginRequest
import com.keralarecipemaster.user.network.model.authentication.LoginResponse
import com.keralarecipemaster.user.network.model.authentication.UserRegisterRequest
import com.keralarecipemaster.user.network.service.AuthenticationApi
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.utils.UserType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val prefsStore: PrefsStore,
    private val authenticationApi: AuthenticationApi,
    @CoroutinesDispatchersModule.IoDispatcher val ioDispatcher: CoroutineDispatcher
) : AuthenticationRepository {
    override suspend fun login(
        username: String,
        password: String,
        userType: UserType
    ): Flow<Int> {
        val result = authenticationApi.login(LoginRequest(username = username, password = password))
        if (result.isSuccessful) {
            withContext(ioDispatcher) {
                launch {
                    if (userType == UserType.USER) {
                        prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
                    } else {
                        prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
                    }
                }
            }
        }
        return flow { emit(result.code()) }
    }

    override suspend fun loginAsRestaurantOwner(username: String, password: String): Flow<Boolean> {
        val result =
            authenticationApi.loginRestaurantOwner(username = username, password = password)
        withContext(ioDispatcher) {
            launch {
                prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
            }
        }
        return flow { emit(result.isSuccessful) }
    }

    override suspend fun registerUser(
        username: String,
        password: String,
        email: String
    ): Flow<Boolean> {
        val result =
            authenticationApi.registerUser(
                UserRegisterRequest(
                    username = username,
                    password = password,
                    email = email
                )
            )
        if (result.isSuccessful) {
            Log.d("CheckRegisterResponse", "succesful")
            withContext(ioDispatcher) {
                launch {
                    prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
                }
            }
        }
        return flow {
            emit(result.isSuccessful)
        }
    }

    override suspend fun registerRestaurantOwner() {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        prefsStore.updateAuthenticationState(AuthenticationState.INITIAL_STATE.name)
    }
}
