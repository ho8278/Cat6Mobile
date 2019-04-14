package com.example.myapplication.view.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.databinding.ItemDatedividerBinding
import com.example.myapplication.databinding.ItemMychatBinding
import com.example.myapplication.databinding.ItemTheirchatBinding
import com.example.myapplication.view.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class ChatFragmentAdapter(context: Context) : ListAdapter<ChatInfo, BaseViewHolder>(ChatInfo.DIFF_CALLBACK) {

    private val TAG = ChatFragmentAdapter::class.java.simpleName
    private var chatInfoList: MutableList<ChatInfo> = mutableListOf()
    private lateinit var userId:String

    val VIEW_TYPE_ME = 0
    val VIEW_TYPE_YOU = 1

    init{
        DataManager.getInstance(context).getCurrentUser()
            .subscribe({user->
                userId=user.id
            },{
                Log.e(TAG,it.message)
            })
    }

    fun setList(changeList: MutableList<ChatInfo>) {
        chatInfoList=changeList
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
            TODO("고쳐야함")
            val dateFormat=SimpleDateFormat("hh-mm-ss",Locale.KOREA)
            binding.tvMessageBody.setText(chatInfoList[position].message)
            binding.tvMessageBody.setText(dateFormat.format(chatInfoList[position].sendDate))
        }

    }

    inner class YouViewHolder(val binding: ItemTheirchatBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            TODO("고쳐야함")
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