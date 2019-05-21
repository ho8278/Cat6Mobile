package com.example.myapplication.util

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.data.model.Team
import com.example.myapplication.view.calendar.ScheduleListAdapter
import com.example.myapplication.view.detailschedule.ScheduleViewPagerAdapter
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.main.ChatListAdapter
import com.example.myapplication.view.main.TeamListAdapter
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
    fun setChatItem(view: RecyclerView, item: MutableList<ChatInfo>?) {
        val adapter = view.adapter as ChatInfoListAdapter
        adapter.setList(item ?: mutableListOf())
        view.scrollToPosition(adapter.itemCount - 1)
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setScheduleItem(view: RecyclerView, item: MutableList<Schedule>?) {
        val adapter = view.adapter as ScheduleListAdapter
        adapter.setList(item ?: mutableListOf())
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setScheduleItem(view: ViewPager, item: MutableList<Schedule>?) {
        val adapter = view.adapter as ScheduleViewPagerAdapter
        adapter.setList(item ?: mutableListOf())
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setChatRoomItem(view: RecyclerView, item: MutableList<ChatRoom>?) {
        val adapter = view.adapter
        if(adapter != null){
            (adapter as ChatListAdapter).setList(item ?: mutableListOf())
        }
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setTeamList(view: RecyclerView, item: MutableList<Team>?) {
        val adapter = view.adapter as TeamListAdapter
        adapter.setList(item ?: mutableListOf())
    }

    @JvmStatic
    @BindingConversion
    fun convertBooleanToView(status:Boolean):Int{
        if(status)
            return View.VISIBLE
        else
            return View.GONE
    }

    @JvmStatic
    @BindingConversion
    fun convertDateTimeToString(dateTime: DateTime): String {
        if (dateTime.hourOfDay > 12) {
            val formatter = DateTimeFormat.forPattern("MM 월 dd 일   hh:mm")
            return dateTime.toString(formatter)
        } else {
            val formatter = DateTimeFormat.forPattern("MM 월 dd 일   hh:mm")
            return dateTime.toString(formatter)
        }
    }
}