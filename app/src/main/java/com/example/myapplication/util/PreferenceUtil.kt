package com.example.myapplication.util

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

class PreferenceUtil {
    companion object {
        val USER_ID = "USER_ID"
        fun getUserId(context: Context): String =
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(USER_ID, "1") ?: "1"

        fun saveUserId(context: Context, userId: String) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit().apply {
                    putString(USER_ID, userId)
                    apply()
                }
        }
    }
}