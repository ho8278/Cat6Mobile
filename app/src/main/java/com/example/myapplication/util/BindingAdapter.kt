package com.example.myapplication.util

import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.model.ChatInfo
import com.example.myapplication.view.chat.ChatFragmentAdapter

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView : ImageView, url: String) {
        Log.d("Test", url)
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("bind_item")
    fun setItem(view: RecyclerView, item:MutableList<ChatInfo>){
        val adapter = view.adapter as ChatFragmentAdapter
        adapter.setList(item)
    }

    @JvmStatic
    @BindingConversion
    fun convertBooleanToInt(isLoading:Boolean):Int{
        if(isLoading){
            return View.VISIBLE
        }
        else
            return View.GONE
    }
}