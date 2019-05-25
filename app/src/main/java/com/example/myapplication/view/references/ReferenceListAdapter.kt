/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.databinding.ItemReferenceBinding
import com.example.myapplication.view.base.BaseViewHolder

class ReferenceListAdapter(private val itemClickListener: ReferenceItemClickListener) :
    ListAdapter<Reference, ReferenceListAdapter.ReferenceViewHolder>(Reference.DIFF_UTIL) {

    private lateinit var referencesList: MutableList<Reference>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceViewHolder = ReferenceViewHolder(
        ItemReferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = referencesList.size

    override fun onBindViewHolder(holder: ReferenceViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener { itemClickListener.itemOnClicked(referencesList[position].path) }
    }

    fun setList(referenceList : MutableList<Reference>) {
        this.referencesList = referenceList
        submitList(ArrayList(referencesList))
    }


    inner class ReferenceViewHolder(val binding: ItemReferenceBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.tvReferenceItemName.text = referencesList[position].title
            binding.tvReferenceItemDate.text = referencesList[position].uploadDate.toString()
            // TODO : Category 설정 및 Image 설정
        }
    }
}
