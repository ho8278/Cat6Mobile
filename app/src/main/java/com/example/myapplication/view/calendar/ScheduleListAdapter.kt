package com.example.myapplication.view.calendar

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Schedule
import kotlinx.android.synthetic.main.item_more_schedule.view.*
import kotlinx.android.synthetic.main.item_schedule.view.*
import java.util.*

class ScheduleListAdapter : RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder>() {
    val TAG = ScheduleListAdapter::class.java.simpleName
    var scheduleList = listOf<Schedule>()
    private val MORE_SCHEDULE = 0
    private val SCHEDULE_ITEM = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val view = if (viewType == SCHEDULE_ITEM){
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false).apply {
                val random = Random()
                val drawable = ShapeDrawable(OvalShape())
                drawable.paint.color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))

                view_dot.background = drawable
            }
        }
        else{
            LayoutInflater.from(parent.context).inflate(R.layout.item_more_schedule, parent, false).apply{
                tv_more_count.text = "+ ${scheduleList.size - 3}"
            }
        }

        return ScheduleViewHolder(view)
    }

    fun setList(scheduleList: List<Schedule>) {
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount-1)
            return MORE_SCHEDULE
        else
            return SCHEDULE_ITEM
    }

    override fun getItemCount(): Int {
        if(scheduleList.isEmpty())
            return 0
        else if(scheduleList.size > 3)
            return 4
        else
            return scheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        if(getItemViewType(position) == SCHEDULE_ITEM)
            holder.itemView.findViewById<TextView>(R.id.tv_schedule_name).text = scheduleList[position].name

    }

    inner class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}