package com.example.myapplication.view.calendar

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel

class CalendarViewModel : BaseViewModel{
    val TAG=CalendarViewModel::class.java.simpleName
    constructor(dataSource: DataSource):super(dataSource)
    private val alphabetMonth = arrayOf("JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER")
    val isLoading=ObservableBoolean()
    val year = ObservableField<String>()
    val month = ObservableField<String>()
    val scheduleList=ObservableArrayList<Schedule>()
    val title=ObservableField<String>()

    init{
        isLoading.set(true)

    }

    fun offsetChange(offset:Int, layoutRange:Int){
        if(offset>=layoutRange){
            title.set(month.get())
        }
        else{
            title.set("")
        }
    }

    fun loadSchedule(year:Int, month:Int, day:Int){
        getCompositeDisposable().also {
            val groupId=getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
            it.add(getDataManager().loadSchedule(groupId)
                .subscribe({
                    Log.e(TAG,it.description)
                    isLoading.set(false)
                    OnDateChanged(year, month, day)
                },{
                    Log.e(TAG,it.message)
                }))
        }
    }

    fun OnMonthChanged(year:Int, month:Int){
        this.year.set(year.toString())
        this.month.set(alphabetMonth[month-1])
    }

    fun OnDateChanged(year:Int, month:Int, day:Int){
        getCompositeDisposable().also {
            getDataManager().getSchedules(year,month,day)
                .subscribe({ list->
                    Log.e(TAG,list.toString())
                    scheduleList.clear()
                    scheduleList.addAll(list)
                },{
                    Log.e(TAG,it.message)
                })
        }
    }
}