package com.example.myapplication.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.User
import com.example.myapplication.databinding.ItemGroupMemberBinding
import com.example.myapplication.view.base.BaseViewHolder

class MemberListAdapter(val listener: MemberClickListener) : RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    private val memberList: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder =
        MemberViewHolder(
            ItemGroupMemberBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    fun setList(list:MutableList<User>){
        val size = memberList.size
        memberList.clear()
        notifyItemRangeRemoved(0,size)
        memberList.addAll(list)
        notifyItemRangeInserted(0,list.size)
    }

    inner class MemberViewHolder(itemView: ItemGroupMemberBinding) : BaseViewHolder(itemView) {

        private val item : ItemGroupMemberBinding = itemView

        override fun bind(position: Int) {
            val userTemp: String = memberList[position].nickname
            val url = memberList[position].profileLink
            item.member = userTemp
            item.url = url
            item.clContainer.setOnClickListener {
                listener.memberClicked(memberList[position].id)
            }
        }
    }
}