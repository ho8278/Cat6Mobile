package com.example.myapplication.view.chat

import android.content.Context
import android.util.Log
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
import java.util.*

class ChatFragmentAdapter(val userId: String) : ListAdapter<ChatInfo, RecyclerView.ViewHolder>(ChatInfo.DIFF_CALLBACK) {

    private val TAG = ChatFragmentAdapter::class.java.simpleName
    private var chatInfoList: MutableList<ChatInfo> = mutableListOf()

    val VIEW_TYPE_ME = 0
    val VIEW_TYPE_YOU = 1
    val VIEW_TYPE_DIVIDE = 2

    fun setList(changeList: MutableList<ChatInfo>) {
        submitList(changeList)
    }

    fun addList(chatInfo: ChatInfo) {
        chatInfoList.add(chatInfo)
        submitList(chatInfoList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ME) {
            return DataBindingUtil.inflate<ItemMychatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_mychat,
                parent,
                false
            ).let{
                MeViewHolder(it,parent.context)
            }
        }
        else{
            return DataBindingUtil.inflate<ItemTheirchatBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_theirchat,
                parent,
                false
            ).let{
                YouViewHolder(it,parent.context)
            }
        }
    }

    override fun getItemCount(): Int = chatInfoList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        if (userId == chatInfoList.get(position).sendUserId) {
            return VIEW_TYPE_ME
        } else{
            return VIEW_TYPE_YOU
        }
    }

    inner class MeViewHolder(binding: ItemMychatBinding, context: Context) :
        BaseViewHolder<ItemMychatBinding, ChatListViewModel>(binding, context) {


        override fun getViewModel(dataManager: DataSource): ChatListViewModel {
            return ChatListViewModel(dataManager)
        }

        fun bind(chatInfo: ChatInfo) {
            //TODO:ChatInfo 바인딩
        }
    }

    inner class YouViewHolder(binding: ItemTheirchatBinding, context: Context) :
        BaseViewHolder<ItemTheirchatBinding, ChatListViewModel>(binding, context) {


        override fun getViewModel(dataManager: DataSource): ChatListViewModel {
            return ChatListViewModel(dataManager)
        }

        fun bind(chatInfo: ChatInfo) {
            //TODO:ChatInfo 바인딩
        }
    }

    inner class DivideViewHolder(binding: ItemDatedividerBinding, context: Context) :
        BaseViewHolder<ItemDatedividerBinding, DivideViewModel>(binding, context) {


        override fun getViewModel(dataManager: DataSource): DivideViewModel {
            return DivideViewModel(dataManager)
        }

        fun bind(date: Date) {
            //TODO:날짜 바인딩
        }
    }
}