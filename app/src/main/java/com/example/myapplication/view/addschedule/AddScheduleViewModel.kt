package com.example.myapplication.view.addschedule

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.local.pref.PreferenceHelperImpl
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel
import com.example.myapplication.view.main.ErrorCode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.SimpleDateFormat

class AddScheduleViewModel(dataSource: DataSource) : BaseViewModel(dataSource) {

    constructor(dataSource: DataSource, navigator: AddNavigator) : this(dataSource) {
        val time = DateTime()
        this.navigator = navigator
        startDate.set(time)
        endDate.set(time)
        isDelete.set(false)
    }
    constructor(dataSource: DataSource, navigator: AddNavigator,time:DateTime) : this(dataSource) {
        this.navigator = navigator
        startDate.set(time)
        endDate.set(time)
        isDelete.set(false)
    }

    constructor(dataSource: DataSource, navigator: AddNavigator?, schedule: Schedule) : this(dataSource) {
        this.schedule=schedule
        val parser=SimpleDateFormat("yyyy-MM-dd hh:mm")
        val startTime=parser.parse(schedule.startDate)
        val endTime=parser.parse(schedule.endDate)
        title.set(schedule.name)
        startDate.set(DateTime(startTime.time))
        endDate.set(DateTime(endTime.time))
        isDelete.set(true)
        this.navigator = navigator
    }

    lateinit var schedule:Schedule
    val TAG = AddScheduleViewModel::class.java.simpleName
    var navigator: AddNavigator? = null
    val startDate = ObservableField<DateTime>()
    val endDate = ObservableField<DateTime>()
    val title = ObservableField<String>()
    var isClicked = true     //true=시작 false=종료
    val isDelete = ObservableBoolean(false)

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

    fun OnDateChanged(year: Int, month: Int, day: Int) {
        when (isClicked) {
            true -> {
                val time = startDate.get() ?: DateTime()
                val changeTime = DateTime(year, month, day, time.hourOfDay, time.minuteOfHour)
                startDate.set(changeTime)
            }

            false -> {
                val time = endDate.get() ?: DateTime()
                val changeTime = DateTime(year, month, day, time.hourOfDay, time.minuteOfHour)
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
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
        val currentGroupID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        getCompositeDisposable().add(
            getDataManager().saveSchedule(
                Schedule(
                    "",
                    startDate.get()?.toString(formatter) ?: "",
                    endDate.get()?.toString(formatter) ?: "",
                    title.get() ?: "",
                    currentGroupID
                )
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e(TAG, "Success")
                    navigator?.OnSaveSuccess()
                }, {
                    Log.e(TAG, "ErrorCode: ${it.message}")
                    navigator?.OnSaveFail(ErrorCode.fromCode(it.message!!.toInt()))
                })
        )
    }

    fun updateSchedule(){
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
        val currentGroupID = getDataManager().getItem<String>(PreferenceHelperImpl.CURRENT_GROUP_ID)
        getDataManager().updateSchedulers(
            Schedule(
                schedule.id,
                startDate.get()?.toString(formatter) ?: "",
                endDate.get()?.toString(formatter) ?: "",
                title.get() ?: "",
                currentGroupID
            ))
        navigator?.OnSaveSuccess()
    }

    fun deleteSchedule(){
        getCompositeDisposable().add(
            getDataManager().deleteSchedule(schedule.id)
                .subscribe ({
                    navigator?.OnSaveSuccess()
                },{
                    navigator?.OnSaveFail(ErrorCode.UNKNOWN)
                })
        )
    }
}