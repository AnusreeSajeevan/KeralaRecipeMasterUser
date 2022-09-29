package com.keralarecipemaster.user.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.keralarecipemaster.user.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PrefsStoreImpl @Inject constructor(@ApplicationContext val context: Context) : PrefsStore {

    private companion object {
        const val KRM_DATA_STORE = "krm_data_store"
    }

    private object PreferencesKeys {
        val AUTHENTICATION_STATE = preferencesKey<String>("authentication_state")
        val NOTIFICATION_STATUS = preferencesKey<Boolean>("notification_status")
        val USERNAME = preferencesKey<String>("username")
        val EMAIL = preferencesKey<String>("email")
        val USER_ID = preferencesKey<Int>("user_id")
    }

    private val dataStore = context.createDataStore(name = KRM_DATA_STORE)

    override suspend fun getAuthenticationState(): Flow<String> {
//        return dataStore.data.
        return flow {
            dataStore.data.catch { exception ->
                if (exception is Exception) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.collect {
                emit(
                    it[PreferencesKeys.AUTHENTICATION_STATE]
                        ?: AuthenticationState.INITIAL_STATE.name
                )
            }
        }
    }

    override suspend fun updateAuthenticationState(authenticationState: String) {
        dataStore.edit {
            it[PreferencesKeys.AUTHENTICATION_STATE] = authenticationState
        }
    }

    override suspend fun getNotificationStatus(): Flow<Boolean> {
        return flow {
            dataStore.data.catch { exception ->
                if (exception is Exception) {
                    emit(emptyPreferences())
                } else throw exception
            }.collect {
                emit(it[PreferencesKeys.NOTIFICATION_STATUS] ?: false)
            }
        }
    }

    override suspend fun setNotificationStatus(status: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.NOTIFICATION_STATUS] = status
        }
    }

    override suspend fun getUsername(): Flow<String> {
        return flow {
            dataStore.data.catch { exception ->
                if (exception is Exception) {
                    emit(emptyPreferences())
                } else throw exception
            }.collect {
                emit(it[PreferencesKeys.USERNAME] ?: Constants.EMPTY_STRING)
            }
        }
    }

    override suspend fun setUsername(username: String) {
        dataStore.edit {
            it[PreferencesKeys.USERNAME] = username
        }
    }

    override suspend fun getEmail(): Flow<String> {
        return flow {
            dataStore.data.catch { exception ->
                if (exception is Exception) {
                    emit(emptyPreferences())
                } else throw exception
            }.collect {
                emit(it[PreferencesKeys.EMAIL] ?: Constants.EMPTY_STRING)
            }
        }
    }

    override suspend fun setEmail(email: String) {
        dataStore.edit {
            it[PreferencesKeys.EMAIL] = email
        }
    }

    override suspend fun getUserId(): Flow<Int> {
        return flow {
            dataStore.data.catch { exception ->
                if (exception is Exception) {
                    emit(emptyPreferences())
                } else throw exception
            }.collect {
                emit(it[PreferencesKeys.USER_ID] ?: Constants.INVALID_USER_ID)
            }
        }
    }

    override suspend fun setUserId(userId: Int) {
        dataStore.edit {
            it[PreferencesKeys.USER_ID] = userId
        }
    }
}
