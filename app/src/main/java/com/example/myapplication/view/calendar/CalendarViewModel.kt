package com.example.myapplication.view.calendar

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel : BaseViewModel{
    val TAG=CalendarViewModel::class.java.simpleName
    constructor(dataSource: DataSource):super(dataSource)
    var scheduleList: ObservableArrayList<Schedule> = ObservableArrayList()
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
        return scheduleList.filter {
            val time = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA).run {
                DateTime(parse(it.startDate).time)
            }
            time.year == year && time.monthOfYear == month && time.dayOfMonth == day
        }
    }

    fun deleteSchedule(position:Int){
        getCompositeDisposable().add(
            getDataManager().deleteSchedule(scheduleList.get(position)?.id ?: "")
                .subscribe({
                    Log.e(TAG,it.toString())
                },{
                    Log.e(TAG,it.message)
                })
        )
    }
}