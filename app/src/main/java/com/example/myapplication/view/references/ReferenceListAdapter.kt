/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemReferenceBinding
import com.example.myapplication.view.base.BaseViewHolder

class ReferenceListAdapter(private val itemClickListener: ReferenceItemClickListener) :
    ListAdapter<Reference, ReferenceListAdapter.ReferenceViewHolder>(Reference.DIFF_UTIL) {

    private lateinit var referencesList: MutableList<Reference>
    private val imgRegex : Regex = Regex.fromLiteral(".*\\.(jpg|png)$")
    private val pdfRegex : Regex = Regex.fromLiteral(".*\\.(pdf)$")
    private val hwpRegex : Regex = Regex.fromLiteral(".*\\.(hwp)$")
    private val videoRegex : Regex = Regex.fromLiteral(".*\\.(wav|mp4|avi)$")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceViewHolder = ReferenceViewHolder(
        ItemReferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = referencesList.size

    override fun onBindViewHolder(holder: ReferenceViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnClickListener { itemClickListener.itemOnClicked(referencesList[position].title) }
    }

    fun setList(referenceList : MutableList<Reference>) {
        this.referencesList = referenceList
        submitList(ArrayList(referencesList))
    }

    inner class ReferenceViewHolder(val binding: ItemReferenceBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            binding.tvReferenceItemName.text = referencesList[position].title

            if(imgRegex.matches(referencesList[position].title)) {
                binding.ivReferenceItemImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_person_black_24dp))
            } else if(pdfRegex.matches(referencesList[position].title)) {
                binding.ivReferenceItemImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_arrow_back))
            } else if(hwpRegex.matches(referencesList[position].title)) {
                binding.ivReferenceItemImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_document))
            } else if(videoRegex.matches(referencesList[position].title)){
                binding.ivReferenceItemImg.setImageDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.ic_person_black_24dp))
            }
            // TODO : Category 설정 및 Image 설정
        }
    }
}
