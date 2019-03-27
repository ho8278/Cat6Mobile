package com.example.myapplication.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemGroupMemberBinding

class MemberListAdapter : ListAdapter<String, MemberListAdapter.MemberViewHolder> {

    private val memberList: MutableList<String> = ArrayList()

    constructor() : super(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = (oldItem == newItem)
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = (oldItem == newItem)
    })


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder =
        MemberViewHolder(
            ItemGroupMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val userTemp: String = memberList[position]
        holder.bind(userTemp)
    }

    fun addGroupMember(member: String) {
        memberList.add(member)
        submitList(memberList)
    }

    fun removeGroupMember(member: String) {
        memberList.remove(member)
        submitList(memberList)
    }

    fun clearGroupMember() {
        memberList.clear()
        submitList(memberList)
    }

    inner class MemberViewHolder(itemView: ItemGroupMemberBinding) : RecyclerView.ViewHolder(itemView.root) {

        private val item : ItemGroupMemberBinding = itemView

        fun bind(member: String) {
            item.member = member
            item.url = "https://designshack.net/wp-content/uploads/img-placeholder.jpg"
        }
    }
}