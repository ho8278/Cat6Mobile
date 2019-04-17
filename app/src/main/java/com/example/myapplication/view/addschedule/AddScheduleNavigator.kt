package com.example.myapplication.view.addschedule

import com.example.myapplication.view.main.ErrorCode

interface AddScheduleNavigator{
    fun OnSaveFail(errorCode: ErrorCode)
    fun OnSaveSuccess()
}