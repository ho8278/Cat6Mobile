package com.example.myapplication.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.ChatRoom
import com.example.myapplication.databinding.ItemChatListBinding
import com.example.myapplication.view.base.BaseViewHolder

class ChatListAdapter(val listener:ChatRoomChangeListener) : RecyclerView.Adapter<BaseViewHolder>() {

    val chatList = mutableListOf<ChatRoom>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ItemChatListBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.item_chat_list, parent, false)
        return ChatListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setList(list:MutableList<ChatRoom>){
        val size = chatList.size
        chatList.clear()
        notifyItemRangeRemoved(0,size)
        chatList.addAll(list)
        notifyItemRangeInserted(0,list.size)
    }

    inner class ChatListViewHolder(val binding: ItemChatListBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.tvChatName.setText("# ${chatList[position].name}")
            binding.llContainer.setOnClickListener {
                listener.chatRoomChagned(chatList[position])
            }
        }
    }
}