package com.otus.securehomework.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.otus.securehomework.data.utils.encryption.Encryptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val dataStoreFile: String = "securePref"

class UserPreferences
@Inject constructor(
    private val context: Context,
    private val encryptor: Encryptor,
) {
    private val Context.dataStore by preferencesDataStore(name = dataStoreFile)
    private var iv: ByteArray = byteArrayOf()

    val accessToken: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
                ?.let { token ->
                    Log.d("accessToken", token)
                    encryptor.decrypt(token, iv)
                }
        }

    val refreshToken: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
                ?.let { token ->
                    Log.d("refreshToken", token)
                    encryptor.decrypt(token, iv)
                }
        }

    suspend fun saveAccessTokens(accessToken: String?, refreshToken: String?) {
        context.dataStore.edit { preferences ->
            accessToken?.let {
                val out = encryptor.encrypt(it)
                preferences[ACCESS_TOKEN] = out.encryption
                iv = out.initVector
            }
            refreshToken?.let {
                val out = encryptor.encrypt(it)
                preferences[REFRESH_TOKEN] = out.encryption
                iv = out.initVector
            }
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("key_refresh_token")
    }
}