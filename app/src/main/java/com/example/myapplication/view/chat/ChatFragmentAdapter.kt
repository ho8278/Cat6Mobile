package com.example.myapplication.view.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.databinding.ItemDatedividerBinding
import com.example.myapplication.databinding.ItemMychatBinding
import com.example.myapplication.databinding.ItemTheirchatBinding
import com.example.myapplication.util.PreferenceUtil
import com.example.myapplication.view.base.BaseViewHolder
import com.google.firebase.iid.FirebaseInstanceId
import java.text.SimpleDateFormat
import java.util.*

class ChatFragmentAdapter(val userId: String) : ListAdapter<ChatInfo, BaseViewHolder>(ChatInfo.DIFF_CALLBACK) {

    private val TAG = ChatFragmentAdapter::class.java.simpleName
    private var chatInfoList: MutableList<ChatInfo> = mutableListOf()

    val VIEW_TYPE_ME = 0
    val VIEW_TYPE_YOU = 1

    init{
        chatInfoList.add(ChatInfo("1","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("2","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("3","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("4","2","chat1",Calendar.getInstance().time,"ffff"))
        chatInfoList.add(ChatInfo("5","2","chat1",Calendar.getInstance().time,"ffff"))
        notifyDataSetChanged()
    }

    fun setList(changeList: MutableList<ChatInfo>) {
        submitList(changeList)
    }

    fun addList(chatInfo: ChatInfo) {
        chatInfoList.add(chatInfo)
        submitList(chatInfoList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == VIEW_TYPE_ME) {
            return DataBindingUtil.inflate<ItemMychatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_mychat,
                parent,
                false
            ).let{
                MeViewHolder(it)
            }
        }
        else{
            return DataBindingUtil.inflate<ItemTheirchatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_theirchat,
                parent,
                false
            ).let{
                YouViewHolder(it)
            }
        }
    }

    override fun getItemCount(): Int = chatInfoList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (userId == chatInfoList.get(position).sendUserId) {
            return VIEW_TYPE_ME
        } else{
            return VIEW_TYPE_YOU
        }
    }

    inner class MeViewHolder(val binding: ItemMychatBinding) : BaseViewHolder(binding) {

        override fun bind(position: Int) {
            val dateFormat=SimpleDateFormat("hh-mm-ss",Locale.KOREA)
            binding.tvMessageBody.setText(chatInfoList[position].message)
            binding.tvMessageBody.setText(dateFormat.format(chatInfoList[position].sendDate))
        }

    }

    inner class YouViewHolder(val binding: ItemTheirchatBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {

            val dateFormat=SimpleDateFormat("hh-mm-ss",Locale.KOREA)
            binding.tvMessageBody.setText(chatInfoList[position].message)
            binding.tvMessageBody.setText(dateFormat.format(chatInfoList[position].sendDate))
        }
    }

    inner class DivideViewHolder(val binding: ItemDatedividerBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}