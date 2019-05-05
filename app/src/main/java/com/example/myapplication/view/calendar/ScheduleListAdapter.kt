package com.example.myapplication.view.calendar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ItemScheduleBinding
import com.example.myapplication.view.base.BaseViewHolder
import com.example.myapplication.view.detailschedule.DetailScheduleActivity
import java.util.*

class ScheduleListAdapter:RecyclerView.Adapter<BaseViewHolder>(){
    var scheduleList = mutableListOf<Schedule>()

    private val SCHEDULE_INFO="SCHEDULE_INFO"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding= ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)
        val random= Random()
        val drawable= ShapeDrawable(OvalShape())
        drawable.paint.color=Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))

        binding.viewDot.background=drawable
        binding.clScheduleContainer.setOnClickListener {
            //TODO:일정 상세정보 액티비티 생성
        }

        return ScheduleViewHolder(binding, DataManager.getInstance(parent.context))
    }

    fun setList(scheduleList:MutableList<Schedule>){
        this.scheduleList=scheduleList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)

    }

    inner class ScheduleViewHolder(val binding:ItemScheduleBinding, val dataSource:DataSource): BaseViewHolder(binding){

        override fun bind(position: Int) {
            binding.viewmodel=ScheduleItemViewModel(dataSource,scheduleList[position])
            binding.clScheduleContainer.setOnClickListener {
                val intent=Intent(binding.root.context,DetailScheduleActivity::class.java)
                intent.putExtra(SCHEDULE_INFO,scheduleList[position])
                binding.root.context.startActivity(intent)
            }
        }
    }
}