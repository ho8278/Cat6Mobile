package com.example.myapplication.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemTheirchatBinding

class TestAdapter2:RecyclerView.Adapter<TestAdapter2.testViewHolder>(){
    val list:ArrayList<String> = ArrayList()

    init{
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
        list.add("aaaaaaaaaa")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): testViewHolder {
        val binding= ItemTheirchatBinding.inflate(
            LayoutInflater.from(parent.context), parent,false)

        return testViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: testViewHolder, position: Int) {
        holder.create(list.get(position))
    }

    inner class testViewHolder(val item:ItemTheirchatBinding): RecyclerView.ViewHolder(item.root){
        fun create(str:String){
            item.tvMessageBody.text=str
            item.tvTheirname.text=str
        }
    }
}