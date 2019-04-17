package com.example.myapplication.view.addschedule

import android.util.Log
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.view.base.BaseViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class AddScheduleViewModel : BaseViewModel {
    constructor(dataSource: DataSource) : super(dataSource)

    val startDate = ObservableField<String>()
    val startClock = ObservableField<String>()
    val endDate = ObservableField<String>()
    val endClock = ObservableField<String>()
    var isClicked = true     //true=시작 false=종료

    init {
        val time = DateTime()
        startDate.set(time.toString("MM 월 dd 일    "))
        endDate.set(time.toString("MM 월 dd 일    "))
        startClock.set("오전 8:00")
        endClock.set("오전 8:00")
    }

    fun OnTimeChanged(hour: Int, minute: Int) {
        when (isClicked) {
            true -> {
                if (hour >= 13) {
                    val time = hour - 12
                    if (minute >= 10)
                        startClock.set("오후 $time:$minute")
                    else
                        startClock.set("오후 $time:0$minute")
                } else {
                    if (minute >= 10)
                        startClock.set("오전 $hour:$minute")
                    else
                        startClock.set("오전 $hour:0$minute")
                }
            }

            false -> {
                if (hour >= 13) {
                    val time = hour - 12
                    if (minute >= 10)
                        endClock.set("오후 $time:$minute")
                    else
                        endClock.set("오후 $time:0$minute")
                } else {
                    if (minute >= 10)
                        endClock.set("오전 $hour:$minute")
                    else
                        endClock.set("오전 $hour:0$minute")
                }
            }
        }
    }

    fun OnDateChanged(month: Int, day: Int) {
        when (isClicked) {
            true -> {
                if (month >= 10){
                    if(day>=10)
                        startDate.set("$month 월 $day 일    ")
                    else
                        startDate.set("$month 월 0$day 일    ")
                }
                else{
                    if(day>=10)
                        startDate.set("0$month 월 $day 일    ")
                    else
                        startDate.set("0$month 월 0$day 일    ")
                }
            }
            false -> {
                if (month >= 10){
                    if(day>=10)
                        endDate.set("$month 월 $day 일    ")
                    else
                        endDate.set("$month 월 0$day 일    ")
                }
                else{
                    if(day>=10)
                        endDate.set("0$month 월 $day 일    ")
                    else
                        endDate.set("0$month 월 0$day 일    ")
                }
            }
        }
    }

    fun OnStartDateClicked() {
        isClicked = true
    }

    fun OnEndDateClicked() {
        isClicked = false
    }

    fun saveSchedule(){
        val formatter=DateTime.parse(startDate.get())
        Log.e("TEST",formatter.toString())
    }
}