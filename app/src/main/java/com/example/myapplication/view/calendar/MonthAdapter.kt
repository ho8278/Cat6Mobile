package com.example.myapplication.view.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCalendarBinding
import kotlinx.android.synthetic.main.item_calendar.view.*
import org.joda.time.DateTime
import org.joda.time.LocalDate

class MonthAdapter(val viewModel:CalendarViewModel, val listener:OnDateClick) : PagerAdapter() {
    val monthSize = 301
    val monthList = List(monthSize) { it - monthSize / 2 }
    val currentTime = DateTime.now()


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == (`object` as ItemCalendarBinding).root

    override fun getCount(): Int = monthSize

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = DataBindingUtil.inflate<ItemCalendarBinding>(LayoutInflater.from(container.context),R.layout.item_calendar,container,false)
        view.root.viewTreeObserver.addOnGlobalLayoutListener(object:ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                val positionTime = currentTime.plusMonths(monthList[position])
                view.viewmodel = viewModel
                view.rvDate.layoutManager = GridLayoutManager(container.context,7, RecyclerView.VERTICAL,false)
                view.rvDate.adapter = DateAdapter(view.rvDate.height,positionTime,viewModel,listener)
                view.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        container.addView(view.root)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView((`object` as ItemCalendarBinding).root)
    }
}