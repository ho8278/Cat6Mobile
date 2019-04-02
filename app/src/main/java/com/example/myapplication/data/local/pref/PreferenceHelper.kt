package com.example.myapplication.data.local.pref

interface PreferenceHelper{
    fun saveUserId(userId:String)

    fun getUserId():String
}