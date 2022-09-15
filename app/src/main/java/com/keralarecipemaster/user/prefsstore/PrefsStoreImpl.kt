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
        const val RECIPE_DATA_STORE = "recipe_data_store"
    }

    private object PreferencesKeys {
        val AUTHENTICATION_STATE = preferencesKey<String>("authentication_state")
    }

    private val dataStore = context.createDataStore(name = RECIPE_DATA_STORE)

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
               emit(it[PreferencesKeys.AUTHENTICATION_STATE] ?: AuthenticationState.INITIAL_STATE.name)
            }
        }
    }

    override suspend fun updateAuthenticationState(authenticationState: String) {
        dataStore.edit {
            it[PreferencesKeys.AUTHENTICATION_STATE] = authenticationState
        }
    }
}
