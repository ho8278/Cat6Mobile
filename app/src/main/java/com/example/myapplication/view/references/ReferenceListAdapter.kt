/*
 * Created by WonJongSeong on 2019-05-24
 */

package com.example.myapplication.view.references

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemReferenceBinding
import com.example.myapplication.view.base.BaseViewHolder

class ReferenceListAdapter : RecyclerView.Adapter<ReferenceListAdapter.ReferenceViewHolder>() {

    private val referencesList: MutableList<Reference> by lazy { ArrayList<Reference>() }

    init {
        referencesList.add(Reference("TEST1"))
        referencesList.add(Reference("TEST2"))
        referencesList.add(Reference("TEST3"))
        referencesList.add(Reference("TEST4"))
        referencesList.add(Reference("TEST5"))
        referencesList.add(Reference("TEST1"))
        referencesList.add(Reference("TEST2"))
        referencesList.add(Reference("TEST3"))
        referencesList.add(Reference("TEST4"))
        referencesList.add(Reference("TEST5"))
        referencesList.add(Reference("TEST1"))
        referencesList.add(Reference("TEST2"))
        referencesList.add(Reference("TEST3"))
        referencesList.add(Reference("TEST4"))
        referencesList.add(Reference("TEST5"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferenceViewHolder = ReferenceViewHolder(
        ItemReferenceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = referencesList.size

    override fun onBindViewHolder(holder: ReferenceViewHolder, position: Int) {
        holder.bind(position)

        holder.itemView.setOnClickListener {  }
    }


    inner class ReferenceViewHolder(val binding: ItemReferenceBinding) :
        BaseViewHolder(binding) {
        override fun bind(position: Int) {
            val fileName : String = referencesList[position].title
            // TODO : Category 설정 및 Image 설정
        }
    }
}
