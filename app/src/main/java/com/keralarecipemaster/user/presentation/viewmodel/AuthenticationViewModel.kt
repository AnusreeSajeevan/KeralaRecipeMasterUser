package com.keralarecipemaster.user.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.repository.AuthenticationRepository
import com.keralarecipemaster.user.utils.Constants
import com.keralarecipemaster.user.utils.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val prefsStore: PrefsStore,
    val authenticationRepository: AuthenticationRepository
) :
    ViewModel() {
    val authenticationState: StateFlow<AuthenticationState>
        get() = _authenticationState

    private val _authenticationState =
        MutableStateFlow(AuthenticationState.INITIAL_STATE)

    val errorMessage: StateFlow<String>
        get() = _errorMessage

    private val _errorMessage =
        MutableStateFlow(Constants.EMPTY_STRING)

    init {
        viewModelScope.launch {
            prefsStore.getAuthenticationState().catch { }.collect {
                _authenticationState.value = AuthenticationState.valueOf(it)
            }
        }
    }

    fun loginAsUser(username: String, password: String) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
            viewModelScope.launch {
                authenticationRepository.login(
                    username = username,
                    password = password,
                    userType = UserType.USER
                )
                    .catch {
                        Log.d("CheckResponse", "catch")
                    }.collect {
                        it?.let { code ->
                            if (code == Constants.ERROR_CODE_SUCCESS) {
                                _authenticationState.value =
                                    AuthenticationState.AUTHENTICATED_USER
                            } else if (code == Constants.ERROR_CODE_INVALID_CREDENTIALS) {
                                _errorMessage.value = "Enter valid credentials"
                            }
                        }
                    }

                // prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
            }
        }
    }

    fun loginAsRestaurantOwner(username: String, password: String) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
            viewModelScope.launch {
                authenticationRepository.login(
                    username = username,
                    password = password,
                    userType = UserType.RESTAURANT
                ).catch { }.collect {
                    it?.let {  code ->
                        if (code == Constants.ERROR_CODE_SUCCESS) {
                            _authenticationState.value =
                                AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER
                        } else if (code == Constants.ERROR_CODE_INVALID_CREDENTIALS) {
                            _errorMessage.value = "Enter valid credentials"
                        }
                    }

                }
            }
        }
    }

    fun continueAsGuestUser() {
        viewModelScope.launch {
            prefsStore.updateAuthenticationState(AuthenticationState.LOGGED_IN_AS_GUEST.name)
        }
    }

    fun registerUser(username: String, password: String, email: String) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty() && email.trim()
            .isNotEmpty()
        ) {
            Log.d("CheckRegisterResponse", "catch")
            viewModelScope.launch {
                authenticationRepository.registerUser(
                    username = username,
                    password = password,
                    email = email
                )
                    .catch {
                        Log.d("CheckRegisterResponse", "catch")
                    }.collect {
                        Log.d("CheckRegisterResponse", it.toString())
                        if (it) _authenticationState.value = AuthenticationState.AUTHENTICATED_USER
                    }
            }
        }

//        viewModelScope.launch {
//            prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER.name)
//        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}
