package com.keralarecipemaster.user.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
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
}
