package com.example.myapplication.util

import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.*
import com.example.myapplication.view.addfriends.AddFriendAdapter
import com.example.myapplication.view.calendar.DateAdapter
import com.example.myapplication.view.calendar.MonthAdapter
import com.example.myapplication.view.calendar.ScheduleListAdapter
import com.example.myapplication.view.chat.ChatInfoListAdapter
import com.example.myapplication.view.detailschedule.ScheduleViewPagerAdapter
import com.example.myapplication.view.detailvote.VoteItemListAdapter
import com.example.myapplication.view.main.ChatListAdapter
import com.example.myapplication.view.main.MemberListAdapter
import com.example.myapplication.view.main.TeamListAdapter
import com.example.myapplication.view.references.Reference
import com.example.myapplication.view.references.ReferenceListAdapter
import com.example.myapplication.view.vote.VoteListAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
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
    @BindingAdapter("animated_textview")
    fun animateSetText(textview: TextView, teamName: String) {
        textview.animatedSetText(teamName)
    }

    @JvmStatic
    @BindingAdapter("android:textStyle")
    fun isCurrentTeam(view: AppCompatTextView, isCurrentTeam:Boolean) {
        if(isCurrentTeam)
            view.setTypeface(view.typeface, Typeface.BOLD)
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun animate_notice_container(view: MaterialCardView, isShow: Boolean) {
        if(view.height == 0){
            view.viewTreeObserver.addOnGlobalLayoutListener(object:ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val height = view.height
                    val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5f,view.context.resources.displayMetrics)
                    val animation = view.animate().setDuration(200L)
                    if(!isShow){
                        animation.translationY(-height.toFloat() - margin)
                            .start()
                    }else{
                        animation.translationY(height.toFloat() + margin)
                            .start()
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }else{
            val height = view.height
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5f,view.context.resources.displayMetrics)
            val animation = view.animate().setDuration(200L)
            if(!isShow){
                animation.translationY(-height.toFloat() - margin)
                    .start()
            }else{
                animation.translationY(height.toFloat() + margin)
                    .start()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("load_chat_user")
    fun loadChatUser(chipGroup: ChipGroup, userList:List<String>){
        chipGroup.removeAllViews()
        userList.forEach {
            val chip = Chip(chipGroup.context)
            chip.text = it
            //chip.setTextSize(TypedValue.COMPLEX_UNIT_SP,10f)
            chipGroup.addView(chip)
        }
    }

    @JvmStatic
    @BindingAdapter("load_add_chat_user")
    fun loadAddChatUser(chipGroup: ChipGroup, userList:List<String>){
        chipGroup.removeAllViews()
        userList.forEach {
            val chip = Chip(chipGroup.context)
            chip.text = it
            chipGroup.addView(chip)
        }
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
    @BindingAdapter("total_bind_item")
    fun setTotalScheduleItem(view: RecyclerView, item: List<Schedule>?) {
        if(item?.isEmpty() ?: true)
            return
        else{
            val adapter = view.adapter as DateAdapter
            adapter.setList(item ?: listOf())
        }
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
    fun convertDateTimeToString(dateTime: DateTime?): String {
        if(dateTime == null)
            return ""
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