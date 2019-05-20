package com.example.myapplication.view.addvote

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAddVoteBinding
import com.example.myapplication.view.base.BaseViewHolder

class AddVoteAdapter(): RecyclerView.Adapter<BaseViewHolder>(){

    private val voteItemList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ItemAddVoteBinding>(LayoutInflater.from(parent.context), R.layout.item_add_vote,parent,false)
        return AddVoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return voteItemList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    fun addList(){
        Log.e("Adapter",voteItemList.toString())
        voteItemList.add("")
        notifyItemInserted(voteItemList.size)
    }

    fun getList():MutableList<String> = voteItemList

    inner class AddVoteViewHolder(val binding:ItemAddVoteBinding):BaseViewHolder(binding){
        override fun bind(position: Int) {
            binding.apply {
                etVoteItemName.setText(voteItemList[position])
                etVoteItemName.addTextChangedListener(object:TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        voteItemList[position] = s.toString()
                    }
                })
                ivDelete.setOnClickListener {
                    voteItemList.removeAt(position)
                    notifyDataSetChanged()
                }

            }
        }
    }
}