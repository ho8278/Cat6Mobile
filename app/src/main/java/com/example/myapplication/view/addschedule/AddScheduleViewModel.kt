package com.example.myapplication.view.addschedule

import android.util.Log
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel
import com.example.myapplication.view.main.ErrorCode
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime

class AddScheduleViewModel(dataSource:DataSource, var navigator: AddScheduleNavigator) : BaseViewModel(dataSource) {
    val TAG=AddScheduleViewModel::class.java.simpleName

    val startDate = ObservableField<DateTime>()
    val endDate = ObservableField<DateTime>()
    var isClicked = true     //true=시작 false=종료

    init {
        val time = DateTime()
        startDate.set(time)
        endDate.set(time)
    }

    fun OnTimeChanged(hour: Int, minute: Int) {
        when (isClicked) {
            true -> {
                val time = startDate.get() ?: DateTime()
                val changeTime = DateTime(time.year, time.monthOfYear, time.dayOfMonth, hour, minute)
                startDate.set(changeTime)
            }

            false -> {
                val time = endDate.get() ?: DateTime()
                val changeTime = DateTime(time.year, time.monthOfYear, time.dayOfMonth, hour, minute)
                endDate.set(changeTime)
            }
        }
    }

    fun OnDateChanged(month: Int, day: Int) {
        when (isClicked) {
            true -> {
                val time = startDate.get() ?: DateTime()
                val changeTime = DateTime(time.year, month, day, time.hourOfDay, time.minuteOfHour)
                startDate.set(changeTime)
            }

            false -> {
                val time = endDate.get() ?: DateTime()
                val changeTime = DateTime(time.year, month, day, time.hourOfDay, time.minuteOfHour)
                endDate.set(changeTime)
            }
        }
    }

    fun OnStartDateClicked() {
        isClicked = true
    }

    fun OnEndDateClicked() {
        isClicked = false
    }

    fun saveSchedule() {
        val startTime=startDate.get()
        val endTime=endDate.get()
        if(startTime?.compareTo(endTime) == 1){
            navigator.OnSaveFail(ErrorCode.LATE_START_DATE)
        }
        getCompositeDisposable().add(
            getDataManager().saveSchedule(Schedule("","123456","123456","123456","123456"))
                .subscribeOn(Schedulers.io())
                .subscribe({},{
                    Log.e(TAG,it.message)
                })
        )
    }
}