package com.example.myapplication.view.calendar

import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel

class ScheduleItemViewModel(dataSource: DataSource): BaseViewModel(dataSource){
    lateinit var description:ObservableField<String>
    constructor(dataSource: DataSource, schedule:Schedule):this(dataSource){
        description=ObservableField(schedule.name)
    }


}