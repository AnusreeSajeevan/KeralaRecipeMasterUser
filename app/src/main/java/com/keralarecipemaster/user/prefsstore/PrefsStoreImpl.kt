package com.keralarecipemaster.user.prefsstore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefsStoreImpl @Inject constructor(@ApplicationContext val context: Context) : PrefsStore {

    private companion object {
        const val RECIPE_DATA_STORE = "recipe_data_store"
    }

    private object PreferencesKeys {
        val AUTHENTICATION_STATE = preferencesKey<String>("authentication_state")
    }

    private val dataStore = context.createDataStore(name = RECIPE_DATA_STORE)

    override fun getAuthenticationState(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is Exception) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[PreferencesKeys.AUTHENTICATION_STATE] ?: AuthenticationState.INITIAL_STATE.name
        }
    }

    override suspend fun updateAuthenticationState(authenticationState: String) {
        dataStore.edit {
            it[PreferencesKeys.AUTHENTICATION_STATE] = authenticationState
        }
    }
}
