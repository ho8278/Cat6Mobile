package com.example.myapplication.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.myapplication.R

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
}