package com.example.myapplication.view.calendar

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel
import org.joda.time.DateTime

class CalendarViewModel : BaseViewModel{
    val TAG=CalendarViewModel::class.java.simpleName
    constructor(dataSource: DataSource):super(dataSource)
    val scheduleList= ObservableArrayList<Schedule>()
    val isLoading = ObservableBoolean(true)
    var currentTime = DateTime.now()
    val title=ObservableField<String>()
    var prevPosition = 150

    fun loadSchedule(){
        getCompositeDisposable().also {
            val groupId=getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
            it.add(getDataManager().loadSchedule(groupId)
                .subscribe({
                    scheduleList.clear()
                    scheduleList.addAll(it)
                    isLoading.set(false)
                },{
                    Log.e(TAG,it.message)
                }))
        }
    }

    fun OnMonthChanged(position:Int){
        currentTime = currentTime.plusMonths(position - prevPosition)
        title.set("${currentTime.year}년 ${currentTime.monthOfYear}월")
        prevPosition = position
    }

    fun getSchedule(year:Int, month:Int, day:Int):List<Schedule>{
        return scheduleList.filter { it.startDate.startsWith("$year-$month-$day ") }
    }

    fun deleteSchedule(position:Int){
        getCompositeDisposable().add(
            getDataManager().deleteSchedule(scheduleList[position].id)
                .subscribe({
                    Log.e(TAG,it.toString())
                },{
                    Log.e(TAG,it.message)
                })
        )
    }
}