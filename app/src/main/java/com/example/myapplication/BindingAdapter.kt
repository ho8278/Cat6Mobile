package com.example.myapplication

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(imageView : ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(imageView)
    }
}