package com.keralarecipemaster.user.repository

import android.util.Log
import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.network.model.authentication.LoginRequest
import com.keralarecipemaster.user.network.model.authentication.RegisterUserRequest
import com.keralarecipemaster.user.network.model.authentication.UserInfo
import com.keralarecipemaster.user.network.service.AuthenticationApi
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.utils.Constants
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
        password: String
    ): Flow<Pair<UserInfo?, Int>> {
        val result = authenticationApi.login(LoginRequest(username = username, password = password))
        var userInfo: UserInfo? = null
        if (result.isSuccessful) {
            result.body()?.let { loginResponse ->
                userInfo = loginResponse.userInfo
                withContext(ioDispatcher) {
                    launch {
                        if (userInfo?.usertype == UserType.USER.value) {
                            prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
                        } else {
                            prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
                        }
                        prefsStore.setUsername(userInfo?.username ?: Constants.EMPTY_STRING)
                        prefsStore.setEmail(userInfo?.email ?: Constants.EMPTY_STRING)
                        prefsStore.setUserId(userInfo?.userId ?: Constants.INVALID_USER_ID)
                    }
                }
            }
        }
        return flow { emit(Pair(userInfo, result.code())) }
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
                RegisterUserRequest(
                    username = username,
                    password = password,
                    email = email
                )
            )
        if (result.isSuccessful) {
            Log.d("CheckRegisterResponse", "succesful")
            val userInfo = result.body()?.userInfo
            withContext(ioDispatcher) {
                launch {
                    prefsStore.setUsername(userInfo?.username ?: Constants.EMPTY_STRING)
                    prefsStore.setEmail(userInfo?.email ?: Constants.EMPTY_STRING)
                    prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
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
