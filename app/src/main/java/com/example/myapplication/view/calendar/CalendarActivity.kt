package com.example.myapplication.view.calendar

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityCalendarBinding
import com.example.myapplication.view.base.BaseActivity
import com.example.myapplication.view.detailschedule.DetailScheduleFragment
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

        supportFragmentManager.beginTransaction().run {
            add(R.id.fl_fragment,DetailScheduleFragment(viewModel,dateTime))
            addToBackStack(null)
            commit()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == DetailScheduleFragment.DATA_CHANGE){
            viewModel.loadSchedule()
        }


        super.onActivityResult(requestCode, resultCode, data)
    }
}
