package com.example.myapplication.view.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ItemScheduleBinding

class ViewPagerAdapter(val list: List<Schedule>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding=DataBindingUtil.inflate<ItemScheduleBinding>(LayoutInflater.from(container.context), R.layout.item_schedule,container,false)
        binding.viewmodel= ScheduleItemViewModel(DataManager.getInstance(container.context),Schedule("11","11","22","TESTEST","z"))
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return list.size
    }
}