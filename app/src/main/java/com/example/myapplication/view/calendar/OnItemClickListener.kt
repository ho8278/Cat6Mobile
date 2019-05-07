package com.example.myapplication.view.calendar

import com.example.myapplication.data.model.Schedule

interface OnItemClickListener{
    fun OnClick(list:MutableList<Schedule>)
}