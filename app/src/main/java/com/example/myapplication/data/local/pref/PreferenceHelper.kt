package com.example.myapplication.data.local.pref

interface PreferenceHelper{
    fun saveString(key:String,userId:String)

    fun getString(key:String):String
}