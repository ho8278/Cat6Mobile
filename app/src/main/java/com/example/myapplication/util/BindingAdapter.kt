package com.example.myapplication.util

import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.*
import com.example.myapplication.view.addfriends.AddFriendAdapter
import com.example.myapplication.view.calendar.ScheduleListAdapter
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.detailschedule.ScheduleViewPagerAdapter
import com.example.myapplication.view.detailvote.VoteItemListAdapter
import com.example.myapplication.view.main.ChatListAdapter
import com.example.myapplication.view.main.MemberListAdapter
import com.example.myapplication.view.main.TeamListAdapter
import com.example.myapplication.view.vote.VoteListAdapter
import com.example.myapplication.view.references.Reference
import com.example.myapplication.view.references.ReferenceListAdapter
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
        var adapter = view.adapter
        if (adapter != null) {
            (adapter as ChatInfoListAdapter).setList(item ?: mutableListOf())
            Handler().postDelayed({
                view.scrollToPosition(adapter.itemCount-1)
            },300)
        }
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
    @BindingAdapter("bind_item")
    fun setUserList(view: RecyclerView, item: MutableList<User>?) {
        val adapter = view.adapter
        if(adapter != null){
            (adapter as MemberListAdapter).setList(item ?: mutableListOf())
        }
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setVoteList(view: RecyclerView, item: MutableList<Vote>?) {
        val adapter = view.adapter as VoteListAdapter
        adapter.setList(item ?: mutableListOf())
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setVoteItemList(view: RecyclerView, item: MutableList<VoteItem>?) {
        val adapter = view.adapter as VoteItemListAdapter
        adapter.setList(item ?: mutableListOf())
    }

    @JvmStatic
    @BindingAdapter("vote_select")
    fun setSelect(view: LinearLayout, isSelect: Boolean?) {
        if (isSelect ?: false) {
            view.background = ContextCompat.getDrawable(view.context, R.drawable.shape_vote_box_select)
        }else{
            view.background = ContextCompat.getDrawable(view.context, R.drawable.shape_vote_box_unselect)
        }
    }

    @JvmStatic
    @BindingAdapter("bind_dialog_item")
    fun setDialogUserList(view: RecyclerView, item: MutableList<User>?) {
        val adapter = view.adapter
        if (adapter != null) {
            (adapter as AddFriendAdapter).userList.clear()
            adapter.userList.addAll(item ?: mutableListOf())
            adapter.notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("isParticipate")
    fun setButtonDisable(view:Button, participate:Boolean){
        if(participate){
            view.isEnabled = false
            view.text = "투표 완료"
        }
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

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setReferenceList(view: RecyclerView, item: MutableList<Reference>?) {
        val adapter = view.adapter
        if(adapter != null){
            (adapter as ReferenceListAdapter).setList(item ?: mutableListOf())
        }
    }
}