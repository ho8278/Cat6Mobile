package com.example.myapplication.view.calendar

import androidx.databinding.ObservableField
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel

class ScheduleItemViewModel(dataSource: DataSource): BaseViewModel(dataSource){
    lateinit var title:ObservableField<String>
    lateinit var description:ObservableField<String>
    constructor(dataSource: DataSource, schedule:Schedule):this(dataSource){
        title= ObservableField(schedule.name)
        description=ObservableField(schedule.name)
    }


}