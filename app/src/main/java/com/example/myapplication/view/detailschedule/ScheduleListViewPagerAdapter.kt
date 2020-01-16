package com.example.myapplication.view.detailschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemDetailScheduleListBinding
import com.example.myapplication.view.calendar.CalendarViewModel
import org.joda.time.DateTime

class ScheduleListViewPagerAdapter(
    val itemListener: DetailScheduleListAdapter.OnScheduleItemClick,
    val listener: OnFabClickListener,
    val selectedTime: DateTime,
    val viewModel: CalendarViewModel
) : PagerAdapter() {

    val monthSize = 301
    val list = List(monthSize) { it - monthSize / 2 }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<ItemDetailScheduleListBinding>(
            LayoutInflater.from(container.context),
            R.layout.item_detail_schedule_list,
            container,
            false
        )
        val day = selectedTime.plusDays(list[position])
        binding.apply {
            tvDay.text = "${day.dayOfMonth}일"
            tvDate.text = "${day.dayOfWeek().asText}"
            fabAddSchedule.setOnClickListener {
                listener.onClick(day)
            }
            rvSchedule.layoutManager = LinearLayoutManager(container.context)
            rvSchedule.adapter = DetailScheduleListAdapter(itemListener, viewModel, day)
        }
        binding.viewmodel = viewModel
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return list.size
    }


    interface OnFabClickListener {
        fun onClick(dateTime: DateTime)
    }
}