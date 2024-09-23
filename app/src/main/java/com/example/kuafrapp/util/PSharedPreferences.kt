package com.example.kuafrapp.util

import android.content.Context
import android.content.SharedPreferences

class PSharedPreferences {

    companion object {

        private val PREFERENCES_TIME = "preferences_time"
        private var sharedPreferences : SharedPreferences? = null

        @Volatile
        private var instance: PSharedPreferences? = null
        private var lock = Any()

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
    fun saveTime(time : Long) {
        sharedPreferences?.edit()?.putLong(PREFERENCES_TIME, time)?.apply()
    }

    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)

}