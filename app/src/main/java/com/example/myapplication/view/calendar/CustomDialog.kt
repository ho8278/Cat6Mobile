package com.example.myapplication.view.calendar

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ActivityDetailScheduleBinding

class CustomDialog(context: Context,val list:List<Schedule>,style:Int) : Dialog(context,style){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=DataBindingUtil.inflate<ActivityDetailScheduleBinding>(layoutInflater,R.layout.activity_detail_schedule,null,false)
        binding.vpSchedule.adapter=ViewPagerAdapter(list)
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.7f
        }
        window.setAttributes(layoutParams);
        setContentView(binding.root)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawableResource(R.color.colorClear)

    }
}