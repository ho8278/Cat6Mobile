package com.example.myapplication.view.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class CustomConstraintLayout @JvmOverloads constructor(context: Context,attributeSet: AttributeSet) :ConstraintLayout(context,attributeSet){
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN)
            return true
        return super.onInterceptTouchEvent(ev)
    }
}