package com.example.myapplication.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class PreferenceHelperImpl:PreferenceHelper {

    companion object {
        val CURRENT_USER_ID="CURRENT_USER_ID"
        val CHANNEL_ID="CHANNEL_ID"
        val CHANNEL_NAME="CHANNEL_NAME"

        private lateinit var preference:SharedPreferences
        @Volatile
        private var INSTANCE:PreferenceHelperImpl?=null
        fun getInstance(context: Context):PreferenceHelperImpl =
                INSTANCE?: synchronized(this){
                    INSTANCE?:PreferenceHelperImpl().apply{
                        preference=PreferenceManager.getDefaultSharedPreferences(context)
                        INSTANCE=this
                    }
                }
    }

    override fun saveString(key:String, userId: String) {
        preference.edit {
            putString(key,userId)
            apply()
        }
    }

    override fun getString(key:String): String {
        return preference.getString(key,"") ?: "Empty"
    }
}