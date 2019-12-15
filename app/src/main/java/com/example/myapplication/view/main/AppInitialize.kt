package com.example.myapplication.view.main

import android.app.Application
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.MockDataManager
import com.facebook.stetho.Stetho

class AppInitialize:Application(){
    companion object {
        lateinit var dataSource: DataSource
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        dataSource = MockDataManager.getInstance(applicationContext)
    }
}