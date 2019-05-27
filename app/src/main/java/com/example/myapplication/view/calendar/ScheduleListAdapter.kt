package com.example.myapplication.view.calendar

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ItemScheduleBinding
import com.example.myapplication.view.base.BaseViewHolder
import com.example.myapplication.view.detailschedule.ScheduleChangeListener
import com.example.myapplication.view.main.AppInitialize
import java.util.*

class ScheduleListAdapter(val listener: OnItemClickListener, val viewModel:CalendarViewModel) : RecyclerView.Adapter<BaseViewHolder>(),
    ScheduleChangeListener {
    val TAG=ScheduleListAdapter::class.java.simpleName
    var scheduleList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemScheduleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val random = Random()
        val drawable = ShapeDrawable(OvalShape())
        drawable.paint.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

        binding.viewDot.background = drawable

        return ScheduleViewHolder(binding, AppInitialize.dataSource)
    }

    fun setList(scheduleList: MutableList<Schedule>) {
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun OnDelete(position: Int) {
        scheduleList.removeAt(position)
        viewModel.deleteSchedule(position)
        notifyItemRemoved(position)
    }

    override fun OnUpdate(position: Int, schedule: Schedule) {
        scheduleList[position] = schedule
        notifyItemChanged(position)
    }

    inner class ScheduleViewHolder(val binding: ItemScheduleBinding, val dataSource: DataSource) :
        BaseViewHolder(binding) {

        override fun bind(position: Int) {
            binding.viewmodel = ScheduleItemViewModel(dataSource, scheduleList[position])
            binding.clScheduleContainer.setOnClickListener {
                listener.OnClick(scheduleList, position)
            }
        }
    }
}