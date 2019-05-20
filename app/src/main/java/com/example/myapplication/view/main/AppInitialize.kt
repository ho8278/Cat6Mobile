package com.example.myapplication.view.main

import android.app.Application
import android.content.Context
import android.util.Log
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
        DataManager.getInstance(applicationContext).saveItem(PreferenceHelperImpl.CURRENT_USER_ID,"ho8278")
        //DataManager.getInstance(this).login()
        DataManager.getInstance(this).loadTeam("ho8278")
            .subscribe({ it->
                Log.e("MAIN",it.toString())
            },{
                Log.e("MAIN",it.message)
            })
    }
}