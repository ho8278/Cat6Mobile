package com.example.myapplication.view.main

import android.app.Application
import com.facebook.stetho.Stetho

class AppInitialize:Application(){
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}