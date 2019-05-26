package com.example.myapplication.view.addschedule

import com.example.myapplication.view.main.ErrorCode

interface AddNavigator{
    fun OnSaveFail(errorCode: ErrorCode)
    fun OnSaveSuccess()
}