package com.example.myapplication.view.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Schedule
import kotlinx.android.synthetic.main.item_date.view.*
import org.joda.time.DateTime

class DateAdapter(val height: Int, val monthCalendar: DateTime, val viewModel: CalendarViewModel) :
    RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
    val dateSize = 35
    val dateList = MutableList(dateSize) {
        val firstDate = monthCalendar.dayOfMonth().withMinimumValue().dayOfWeek

        val startTime = if (firstDate == 7)
            monthCalendar.dayOfMonth().withMinimumValue().plusDays(0)
        else
            monthCalendar.dayOfMonth().withMinimumValue().plusDays(-firstDate)
        startTime.plusDays(it).run {
            Triple(dayOfMonth,monthOfYear,year)
        }
    }
    private val CURRENT_MONTH = 1
    private val NOT_CURRENT_MONTH = 2

    override fun getItemViewType(position: Int): Int {
        if(dateList[position].second == monthCalendar.monthOfYear)
            return CURRENT_MONTH
        else
            return NOT_CURRENT_MONTH
    }

    fun setList(list:List<Schedule>){
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        view.layoutParams.height = height / 5
        if (viewType == NOT_CURRENT_MONTH) {
            view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.colorCalendarBackground))
        }
        view.rv_schedule.adapter = ScheduleListAdapter()
        view.rv_schedule.layoutManager = LinearLayoutManager(parent.context,RecyclerView.VERTICAL,false)
        return DateViewHolder(view)
    }

    override fun getItemCount(): Int = dateSize

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.textView.text = dateList[position].first.toString()
        (holder.recyclerView.adapter as ScheduleListAdapter).setList(
            dateList[position].run {
                viewModel.getSchedule(third,second,first)
            }
        )
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.tv_date)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_schedule)
    }
}