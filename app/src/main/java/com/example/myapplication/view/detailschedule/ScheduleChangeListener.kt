package com.example.myapplication.view.detailschedule

import com.example.myapplication.data.model.Schedule

interface ScheduleChangeListener{
    fun OnDelete(position:Int)
    fun OnUpdate(position:Int, schedule: Schedule)
}