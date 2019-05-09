package com.example.myapplication.view.main

import android.app.Application
import android.content.Context
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
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
        dataSource=DataManager.getInstance(applicationContext)
        //DataManager.getInstance(applicationContext).saveItem(PreferenceHelperImpl.CURRENT_GROUP_ID,"group1")
        //DataManager.getInstance(this).insertTeam(Team("1234","TestTeam"))
    }
}