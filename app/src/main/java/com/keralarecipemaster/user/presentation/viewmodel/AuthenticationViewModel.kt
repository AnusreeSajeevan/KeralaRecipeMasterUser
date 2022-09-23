package com.keralarecipemaster.user.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keralarecipemaster.user.prefsstore.AuthenticationState
import com.keralarecipemaster.user.prefsstore.PrefsStore
import com.keralarecipemaster.user.repository.AuthenticationRepository
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
                authenticationRepository.loginAsUser(username = username, password = password)
                    .catch {
                        Log.d("CheckResponse", "catch")
                    }.collect {
                        if (it) _authenticationState.value = AuthenticationState.AUTHENTICATED_USER
                    }

                // prefsStore.updateAuthenticationState(AuthenticationState.AUTHENTICATED_USER.name)
            }
        }
    }

    fun loginAsRestaurantOwner(username: String, password: String) {
        if (username.trim().isNotEmpty() && password.trim().isNotEmpty()) {
            viewModelScope.launch {
                authenticationRepository.loginAsRestaurantOwner(
                    username = username,
                    password = password
                ).catch { }.collect {
                    if (it) _authenticationState.value =
                        AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER
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
