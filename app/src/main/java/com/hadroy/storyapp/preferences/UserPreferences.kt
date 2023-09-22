package com.hadroy.storyapp.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hadroy.storyapp.data.model.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_user")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }

        }

        private val ID_USER_KEY = stringPreferencesKey("userId")
        private val NAME_USER_KEY = stringPreferencesKey("NAME")
        private val TOKEN_USER_KEY = stringPreferencesKey("TOKEN")
    }


    suspend fun saveUserLogin(data: LoginResult) {
        dataStore.edit { preferences ->
            preferences[ID_USER_KEY] = data.userId.toString()
            preferences[NAME_USER_KEY] = data.name.toString()
            preferences[TOKEN_USER_KEY] = data.token
        }
    }

    fun getUserLogin(): Flow<LoginResult> {
        return dataStore.data.map {
            LoginResult(
                name = it[NAME_USER_KEY],
                userId = it[ID_USER_KEY],
                token = it[TOKEN_USER_KEY] ?: ""
            )
        }
    }

    suspend fun deleteUserLogin() {
        dataStore.edit {
            it.remove(NAME_USER_KEY)
            it.remove(ID_USER_KEY)
            it.remove(TOKEN_USER_KEY)
        }
    }
}