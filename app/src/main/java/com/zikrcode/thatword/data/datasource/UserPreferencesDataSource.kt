package com.zikrcode.thatword.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDataSource @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val USER_PREFERENCES_FILE_NAME = "user_preferences.preferences_pb"
    }

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            File(context.filesDir, USER_PREFERENCES_FILE_NAME)
        }
    )

    suspend fun saveStringPreference(key: String, value: String) {
        dataStore.edit { preferences ->
            val stringKey = stringPreferencesKey(key)
            preferences[stringKey] = value
        }
    }

    fun readStringPreference(key: String): Flow<String?> = dataStore.data
        .map { preferences ->
            val stringKey = stringPreferencesKey(key)
            preferences[stringKey]
        }
}