package com.lexneoapps.concerttickets.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.lexneoapps.concerttickets.data.local.PreferencesManager.PreferencesKeys.IS_FIRST_OPEN_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.prefDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")



@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.prefDataStore


    val firstOpen: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val firstOpen = preferences[IS_FIRST_OPEN_KEY] ?: true
            firstOpen
        }

    suspend fun isFirstOpen(isTrue: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_OPEN_KEY] = isTrue
        }
    }



    private object PreferencesKeys {

        val IS_FIRST_OPEN_KEY = booleanPreferencesKey("IS_FIRST_OPEN_KEY")

    }
}