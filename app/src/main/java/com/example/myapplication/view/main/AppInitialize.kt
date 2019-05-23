package com.example.myapplication.view.main

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.data.model.Team
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging

class AppInitialize:Application(){
    companion object {
        lateinit var dataSource: DataSource
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        dataSource = DataManager.getInstance(applicationContext)
        DataManager.getInstance(this).login("test1","123")
            .subscribe({ it ->
                Log.e("APP",it.data.toString())
            },{
                Log.e("APP",it.message)
            })
    }
}