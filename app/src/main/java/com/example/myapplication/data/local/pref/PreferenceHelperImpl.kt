package com.example.myapplication.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class PreferenceHelperImpl:PreferenceHelper {

    private val CURRENT_USER_ID="USER_ID"

    companion object {
        private lateinit var preference:SharedPreferences
        @Volatile
        private var INSTANCE:PreferenceHelperImpl?=null
        fun getInstance(context: Context):PreferenceHelperImpl =
                INSTANCE?: synchronized(this){
                    INSTANCE?:run{
                        preference=PreferenceManager.getDefaultSharedPreferences(context)
                        INSTANCE=PreferenceHelperImpl()
                        INSTANCE!!
                    }
                }
    }

    override fun saveUserId(userId: String) {
        preference.edit {
            putString(CURRENT_USER_ID,userId)
            apply()
        }
    }

    override fun getUserId(): String {
        return preference.getString(CURRENT_USER_ID,"") ?: "1"
    }
}