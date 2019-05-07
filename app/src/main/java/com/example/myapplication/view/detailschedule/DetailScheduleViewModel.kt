package com.example.myapplication.view.detailschedule

import androidx.databinding.ObservableArrayList
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.base.BaseViewModel

class DetailScheduleViewModel:BaseViewModel{

   var scheduleList:ObservableArrayList<Schedule>

   constructor(dataSource:DataSource, list:MutableList<Schedule>):super(dataSource){
      scheduleList= ObservableArrayList()
      scheduleList.addAll(list)
   }

}