package com.example.myapplication.view.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityCalendarBinding
import com.example.myapplication.view.addschedule.AddScheduleActivity
import com.example.myapplication.view.base.BaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class CalendarActivity : BaseActivity<ActivityCalendarBinding, CalendarViewModel>(), OnItemClickListener {
    override val TAG: String
        get() = CalendarActivity::class.java.simpleName

    override fun getLayoutID(): Int {
        return R.layout.activity_calendar
    }

    override fun getViewModel(dataSource: DataSource): CalendarViewModel {
        return CalendarViewModel(dataSource)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = viewModel

        binding.fabAddSchedule.setOnClickListener {
            startAddActivity()
        }

        initCalendarView()
        initScheduleList()
    }

    private fun initScheduleList() {
        binding.rvSchedule.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvSchedule.adapter = ScheduleListAdapter(this)

        binding.ablToolbarcontainer.addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener { layout: AppBarLayout, offset ->
            viewModel.offsetChange(Math.abs(offset), layout.totalScrollRange)
        })
    }

    private fun initCalendarView() {
        binding.mcvCalendar.topbarVisible = false

        CalendarDay.today().also {
            viewModel.loadSchedule(it.year, it.month, it.day)
            binding.mcvCalendar.selectedDate = it
        }
        viewModel.OnMonthChanged(binding.mcvCalendar.currentDate.year, binding.mcvCalendar.currentDate.month)

        binding.mcvCalendar.setOnDateChangedListener { widget: MaterialCalendarView, date: CalendarDay, selected: Boolean ->
            viewModel.OnDateChanged(date.year, date.month, date.day)
        }
        binding.mcvCalendar.setOnMonthChangedListener { widget, date ->
            binding.mcvCalendar.selectedDate = CalendarDay.from(date.year, date.month, CalendarDay.today().day)
            viewModel.OnMonthChanged(date.year, date.month)
            viewModel.OnDateChanged(date.year, date.month, CalendarDay.today().day)
        }

        binding.fabAddSchedule.setOnClickListener {
            startAddActivity()
        }

    }

    private fun startAddActivity() {
        val intent = Intent(this, AddScheduleActivity::class.java)
        startActivity(intent)
    }

    override fun OnClick(list: List<Schedule>) {
        val customDialog=CustomDialog(this,list,R.style.customStyle)
        customDialog.show()
    }
}
