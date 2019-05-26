package com.example.myapplication.view.vote

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.data.model.Vote
import com.example.myapplication.databinding.ItemVoteBinding
import com.example.myapplication.view.base.BaseViewHolder
import com.example.myapplication.view.detailvote.DetailVote

class VoteListAdapter : ListAdapter<Vote,BaseViewHolder>(object:DiffUtil.ItemCallback<Vote>(){
    override fun areItemsTheSame(oldItem: Vote, newItem: Vote): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Vote, newItem: Vote): Boolean = oldItem.id == newItem.id
}){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ItemVoteBinding>(LayoutInflater.from(parent.context), R.layout.item_vote,parent,false)
        return VoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    val VOTE_ID = "VOTE_ID"
    val VOTE_PLURAL = "VOTE_PLURAL"
    fun setList(list:MutableList<Vote>){
        submitList(list.toMutableList())
    }

    inner class VoteItemViewHolder(val binding:ItemVoteBinding):BaseViewHolder(binding){
        override fun bind(position: Int) {
            binding.apply {
                tvVoteName.text = getItem(position).title
                tvStartDate.text = getItem(position).startDate
                tvEndDate.text = getItem(position).endDate
                clContianer.setOnClickListener {
                    val intent = Intent(root.context, DetailVote::class.java)
                    intent.putExtra(VOTE_ID,getItem(position).id)
                    intent.putExtra(VOTE_PLURAL,getItem(position).duplicate=="1")
                    root.context.startActivity(intent)
                }
            }

        }
    }
}