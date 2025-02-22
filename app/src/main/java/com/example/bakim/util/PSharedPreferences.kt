package com.example.bakim.util

import android.content.Context
import android.content.SharedPreferences

class PSharedPreferences {

    companion object {

        private const val PREFERENCES_TIME = "preferences_time"
        private const val PREFERENCES_USER_ID = "preferences_user_id"
        private const val PREFERENCES_TOKEN = "preferences_token"
        
        @Volatile
        private var instance: PSharedPreferences? = null
        private var sharedPreferences: SharedPreferences? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makePSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makePSharedPreferences(context: Context): PSharedPreferences {
            sharedPreferences =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            return PSharedPreferences()
        }
    }
    fun saveTime(time: Long) {
        sharedPreferences?.edit()?.putLong(PREFERENCES_TIME, time)?.apply()
    }

    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)

    fun saveUserId(userId: Int) {
        sharedPreferences?.edit()?.putInt(PREFERENCES_USER_ID, userId)?.apply()
    }

    fun getUserId() = sharedPreferences?.getInt(PREFERENCES_USER_ID, -1)

    fun saveToken(token: String) {
        sharedPreferences?.edit()?.putString(PREFERENCES_TOKEN, token)?.apply()
    }

    fun getToken() = sharedPreferences?.getString(PREFERENCES_TOKEN, null)

    fun clearAll() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}