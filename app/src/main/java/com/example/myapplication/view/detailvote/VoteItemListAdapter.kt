package com.example.myapplication.view.detailvote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.data.model.VoteItem
import com.example.myapplication.databinding.ItemSelectVoteBinding
import com.example.myapplication.view.base.BaseViewHolder

class VoteItemListAdapter(val isPlural: Boolean) :
    ListAdapter<VoteItem, BaseViewHolder>(object : DiffUtil.ItemCallback<VoteItem>() {
        override fun areItemsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean = oldItem.id == newItem.id
    }) {
    val selectList = mutableListOf<Boolean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ItemSelectVoteBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.item_select_vote, parent, false
        )

        return VoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setList(list: MutableList<VoteItem>) {
        submitList(list.toMutableList())
        selectList.addAll(list.map { false })
    }

    inner class VoteItemViewHolder(val binding: ItemSelectVoteBinding) : BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.apply {
                select = selectList[position]
                name = getItem(position).name
                count = getItem(position).selected.toString()
                llContainer.setOnClickListener {
                    if (isPlural) {
                        if (selectList[position]) {
                            count = getItem(position).selected.toString()
                            selectList[position] = false
                            select = selectList[position]
                        } else {
                            count = (getItem(position).selected + 1).toString()
                            selectList[position] = true
                            select = selectList[position]
                        }
                    } else {
                        if (selectList[position]) {
                            count = getItem(position).selected.toString()
                            selectList[position] = false
                            select = selectList[position]
                        } else {
                            count = (getItem(position).selected + 1).toString()
                            notifyItemChanged(selectList.indexOf(true))
                            selectList.forEachIndexed { index, _ ->
                                selectList[index]=false
                            }
                            selectList[position] = true
                            select = selectList[position]
                        }
                    }


                }
            }
        }
    }
}