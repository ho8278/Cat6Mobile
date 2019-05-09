package com.example.myapplication.data.local.pref

interface PreferenceHelper{
    fun <T:Any> saveItem(key:String,item:T)

    fun <T:Any> getItem(key:String):T
}