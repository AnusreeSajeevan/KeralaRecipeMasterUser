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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    val username: StateFlow<String>
        get() = _username

    private val _username =
        MutableStateFlow(Constants.EMPTY_STRING)

    val email: StateFlow<String>
        get() = _email

    private val _email =
        MutableStateFlow(Constants.EMPTY_STRING)

    private val _restaurantName =
        MutableStateFlow(Constants.EMPTY_STRING)

    val restaurantName: StateFlow<String>
        get() = _restaurantName

    init {
        getAuthenticationState()
        getUsername()
        getPassword()
        getRestaurantName()
    }

    private fun getRestaurantName() {
        viewModelScope.launch {
            prefsStore.getRestaurantName().catch { }.collect {
                _restaurantName.value = it
            }
        }
    }

    private fun getPassword() {
        viewModelScope.launch {
            prefsStore.getEmail().catch { }.collect {
                _email.value = it
            }
        }
    }

    private fun getUsername() {
        viewModelScope.launch {
            prefsStore.getUsername().catch { }.collect {
                _username.value = it
            }
        }
    }

    private fun getAuthenticationState() {
        viewModelScope.launch {
            prefsStore.getAuthenticationState().catch { }.collect {
                _authenticationState.value = AuthenticationState.valueOf(it)
            }
        }
    }

    fun login(username: String, password: String) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
            viewModelScope.launch {
                authenticationRepository.login(
                    username = username,
                    password = password
                )
                    .catch {
                        Log.d("CheckResponse", "catch")
                    }.collect {
                        if (it.second == Constants.ERROR_CODE_SUCCESS) {
                            it.first?.let { userInfo ->
                                if (userInfo.usertype == UserType.USER.value) {
                                    _authenticationState.value =
                                        AuthenticationState.AUTHENTICATED_USER
                                } else {
                                    _authenticationState.value =
                                        AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER
                                    _errorMessage.value = "Logged in successfully!"
                                }
                            }
                        } else if (it.second == Constants.ERROR_CODE_INVALID_CREDENTIALS) {
                            _errorMessage.value = "Enter valid credentials"
                        }
                    }

                // prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
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
                        _errorMessage.value = "Registration successful!"
                        if (it) _authenticationState.value = AuthenticationState.AUTHENTICATED_USER
                    }
            }
        }
    }

    fun registerRestaurantOwner(
        username: String,
        password: String,
        email: String,
        restaurantName: String
    ) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty() && email.trim()
            .isNotEmpty() && restaurantName.trim().isNotEmpty()
        ) {
            viewModelScope.launch {
                authenticationRepository.registerRestaurantOwner(
                    username = username,
                    password = password,
                    email = email,
                    restaurantName = restaurantName
                )
                    .catch {
                    }.collect {
                        _errorMessage.value = "Registration successful!"
                        if (it) _authenticationState.value = AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER
                    }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = Constants.EMPTY_STRING
    }
}
