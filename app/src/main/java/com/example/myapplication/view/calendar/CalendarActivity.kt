package com.example.myapplication.view.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityCalendarBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.detailschedule.CustomDialog
import com.example.myapplication.view.detailschedule.DetailScheduleActivity
import kotlinx.android.synthetic.main.activity_calendar.*
import org.joda.time.DateTime

class CalendarActivity : BaseActivity<ActivityCalendarBinding, CalendarViewModel>(),OnDateClick {
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
        viewModel.loadSchedule()
        vp_calendar.adapter = MonthAdapter(viewModel,this)
        vp_calendar.addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewModel.OnMonthChanged(position)
            }
        })
        vp_calendar.setCurrentItem(150)
    }

    override fun onDateClick(list: MutableList<Schedule>, selectedItem:Triple<Int,Int,Int>) {
        val dateTime = DateTime(selectedItem.third,selectedItem.second,selectedItem.first,0,0)
        val intent = Intent(this, DetailScheduleActivity::class.java)
        intent.putExtra("TEST",dateTime)
        startActivity(intent)


        /*
        val customDialog =
            CustomDialog(this, null, R.style.customStyle, dateTime, viewModel, this)
        customDialog.show()*/
    }

    /*
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
        binding.rvSchedule.adapter = DetailScheduleListAdapter(this,viewModel)

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
    }*/
}
