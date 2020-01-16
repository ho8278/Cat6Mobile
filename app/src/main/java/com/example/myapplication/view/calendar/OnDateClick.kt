package com.example.myapplication.view.calendar

import android.view.View
import androidx.core.util.Pair
import com.example.myapplication.data.model.Schedule

interface OnDateClick {
    fun onDateClick(list:MutableList<Schedule>, selectedItem:Triple<Int,Int,Int>)
}