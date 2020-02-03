package com.example.myapplication.util

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView

fun TextView.animatedSetText(text:String){
    this.text = text
    val animator = AlphaAnimation(0f,1f).apply {
        duration = 500L
    }
    val translateAnimator = TranslateAnimation(
        Animation.RELATIVE_TO_SELF,0f, Animation.RELATIVE_TO_SELF,0f,
        Animation.RELATIVE_TO_SELF,-1f, Animation.RELATIVE_TO_SELF,0f).apply {
        duration = 500L
    }
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(translateAnimator)
    animationSet.addAnimation(animator)
    animationSet.fillAfter = true
    post{
        startAnimation(animationSet)
    }
}