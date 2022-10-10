package com.keralarecipemaster.user.repository

import android.util.Log
import com.keralarecipemaster.user.di.CoroutinesDispatchersModule
import com.keralarecipemaster.user.domain.db.RecipeDao
import com.keralarecipemaster.user.network.model.authentication.LoginRequest
import com.keralarecipemaster.user.network.model.authentication.RegisterRestaurantRequest
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
    @CoroutinesDispatchersModule.IoDispatcher val ioDispatcher: CoroutineDispatcher,
    private val recipeDao: RecipeDao,
    private val recipeRecipeDao: RecipeDao
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
                        userInfo?.let {
                            if (it.usertype == UserType.USER.value) {
                                prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
                                prefsStore.setName(it.name)
                            } else {
                                prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
                                prefsStore.setRestaurantName(it.name)
                            }
                            prefsStore.setUsername(it.username)
                            prefsStore.setEmail(it.email)
                            prefsStore.setUserId(it.userId)
                        }
                    }
                }
            }
        }
        return flow { emit(Pair(userInfo, result.code())) }
    }

    override suspend fun registerUser(
        username: String,
        password: String,
        email: String,
        name: String
    ): Flow<Boolean> {
        val result =
            authenticationApi.registerUser(
                RegisterUserRequest(
                    username = username,
                    password = password,
                    email = email,
                    name = name
                )
            )
        if (result.isSuccessful) {
            val userInfo = result.body()?.userInfo
            withContext(ioDispatcher) {
                launch {
                    prefsStore.setUsername(userInfo?.username ?: Constants.EMPTY_STRING)
                    prefsStore.setEmail(userInfo?.email ?: Constants.EMPTY_STRING)
                    prefsStore.setName(userInfo?.name ?: Constants.EMPTY_STRING)
                    prefsStore.setUserId(userInfo?.userId ?: Constants.INVALID_USER_ID)
                    prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
                }
            }
        }
        return flow {
            emit(result.isSuccessful)
        }
    }

    override suspend fun registerRestaurantOwner(
        username: String,
        password: String,
        email: String,
        restaurantName: String
    ): Flow<Boolean> {
        val result =
            authenticationApi.registerRestaurant(
                RegisterRestaurantRequest(
                    username = username,
                    password = password,
                    email = email,
                    restaurantName = restaurantName
                )
            )
        if (result.isSuccessful) {
            val userInfo = result.body()?.userInfo
            withContext(ioDispatcher) {
                launch {
                    prefsStore.setUsername(userInfo?.username ?: Constants.EMPTY_STRING)
                    prefsStore.setEmail(userInfo?.email ?: Constants.EMPTY_STRING)
                    prefsStore.setUserId(userInfo?.userId ?: Constants.INVALID_USER_ID)
                    prefsStore.setRestaurantName(userInfo?.name ?: Constants.EMPTY_STRING)
                    prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
                }
            }
        }
        return flow {
            emit(result.isSuccessful)
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            prefsStore.updateAuthenticationState(AuthenticationState.INITIAL_STATE.name)
            recipeDao.deleteAll()
            recipeRecipeDao.deleteAll()
        }
    }
}
