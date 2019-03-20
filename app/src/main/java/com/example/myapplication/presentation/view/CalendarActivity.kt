package com.example.myapplication.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityCalendarBinding
import com.google.android.material.appbar.AppBarLayout

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCalendarBinding>(this,R.layout.activity_calendar)
        binding.rvSchedule.layoutManager=LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.rvSchedule.adapter=TestAdapter2()
        binding.mcvCalendar.topbarVisible=false
        binding.ablToolbarcontainer.addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener{layout:AppBarLayout, offset ->
            if(Math.abs(offset)>=layout.getTotalScrollRange()){
                binding.ctlCollapsingbar.title="TEST"
            }
            else{
                binding.ctlCollapsingbar.title=""
            }
        })
    }
}
