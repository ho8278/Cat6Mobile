package com.example.myapplication.view.detailschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.ItemDetailScheduleBinding
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class DetailScheduleListAdapter(val selectedTime: DateTime, val listener: OnScheduleItemClick) :
    ListAdapter<Schedule, DetailScheduleListAdapter.DetailScheduleViewHolder>(object :
        DiffUtil.ItemCallback<Schedule>() {
        override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.startDate == newItem.startDate && oldItem.endDate == newItem.endDate

        override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.startDate == newItem.startDate && oldItem.endDate == newItem.endDate
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailScheduleViewHolder {
        val binding = DataBindingUtil.inflate<ItemDetailScheduleBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_detail_schedule,
            parent,
            false
        )

        return DetailScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailScheduleViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setList(list: List<Schedule>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA)
        val filteredList = list.filter {
            val time = dateFormat.run {
                DateTime(parse(it.startDate).time)
            }
            time.year == selectedTime.year && time.monthOfYear == selectedTime.monthOfYear && time.dayOfMonth == selectedTime.dayOfMonth
        }
        submitList(filteredList)
    }

    inner class DetailScheduleViewHolder(binding: ItemDetailScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvScheduleName
        val period = binding.tvPeriod
        val container = binding.clContainer
        fun bind(position: Int) {
            val item = getItem(position)
            name.text = item.name
            period.text = "${item.startDate} ~ ${item.endDate}"
            container.setOnClickListener {
                listener.onItemClick(item)
            }
        }
    }

    interface OnScheduleItemClick {
        fun onItemClick(schedule: Schedule)
    }
}