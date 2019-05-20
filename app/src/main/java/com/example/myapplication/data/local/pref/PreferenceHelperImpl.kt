package com.example.myapplication.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import java.util.HashSet

class PreferenceHelperImpl : PreferenceHelper {

    companion object {
        val CURRENT_USER_ID = "CURRENT_USER_ID"
        val CHANNEL_ID = "CHANNEL_ID"
        val CHANNEL_NAME = "CHANNEL_NAME"
        val CURRENT_GROUP_ID = "CURRENT_GROUP_ID"
        val COOKIES = "COOKIES"

        private lateinit var preference: SharedPreferences
        @Volatile
        private var INSTANCE: PreferenceHelperImpl? = null

        fun getInstance(context: Context): PreferenceHelperImpl =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceHelperImpl().apply {
                    preference = PreferenceManager.getDefaultSharedPreferences(context)
                    INSTANCE = this
                }
            }
    }

    override fun <T : Any> saveItem(key: String, item: T) {
        preference.edit {
            when (item) {
                is String -> putString(key, item as String)

                is Boolean -> putBoolean(key, item as Boolean)

                is Int -> putInt(key, item as Int)

                is Float -> putFloat(key, item as Float)

                is Long -> putLong(key, item as Long)

                is MutableSet<*> -> putStringSet(key, item as MutableSet<String>)
            }
            apply()
        }
    }

    override fun <T : Any> getItem(key: String): T = when (key) {
        COOKIES -> preference.getStringSet(key, mutableSetOf()) as T
        CURRENT_USER_ID -> preference.getString(key, "") as T
        CURRENT_GROUP_ID -> preference.getString(key, "") as T
        CHANNEL_ID -> preference.getString(key, "") as T
        CHANNEL_NAME -> preference.getString(key, "") as T
        else -> "" as T
    }
}