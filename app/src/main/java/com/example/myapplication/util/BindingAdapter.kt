package com.example.myapplication.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.view.calendar.ScheduleListAdapter
import com.example.myapplication.view.detailschedule.ScheduleViewPagerAdapter
import com.example.myapplication.view.chat.ChatFragmentAdapter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String) {
        Log.d("Test", url)
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setChatItem(view: RecyclerView, item: MutableList<ChatInfo>) {
        val adapter = view.adapter as ChatFragmentAdapter
        adapter.setList(item)
        view.scrollToPosition(adapter.itemCount - 1)
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setScheduleItem(view: RecyclerView, item: MutableList<Schedule>) {
        val adapter = view.adapter as ScheduleListAdapter
        adapter.setList(item)
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setScheduleItem(view: ViewPager, item: MutableList<Schedule>) {
        val adapter = view.adapter as ScheduleViewPagerAdapter
        adapter.setList(item)
    }

    @JvmStatic
    @BindingConversion
    fun convertDateTimeToString(dateTime: DateTime): String {
        if (dateTime.hourOfDay > 12) {
            val formatter = DateTimeFormat.forPattern("MM 월 dd 일    오후 hh:mm")
            return dateTime.toString(formatter)
        } else {
            val formatter = DateTimeFormat.forPattern("MM 월 dd 일    오전 hh:mm")
            return dateTime.toString(formatter)
        }
    }
}