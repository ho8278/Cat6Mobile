package com.example.myapplication.view.web

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.core.content.ContextCompat


class FullscreenableChromeClient(activity: Activity) : WebChromeClient() {
    private var mActivity: Activity? = null

    private var mCustomView: View? = null
    private var mCustomViewCallback: WebChromeClient.CustomViewCallback? = null
    private var mOriginalOrientation: Int = 0

    private var mFullscreenContainer: FrameLayout? = null

    init {
        this.mActivity = activity
    }

    override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mCustomView != null) {
                callback.onCustomViewHidden()
                return
            }

            mOriginalOrientation = mActivity!!.requestedOrientation
            val decor = (mActivity as Activity).window.decorView as FrameLayout
            mFullscreenContainer = FullscreenHolder(mActivity as Activity)
            mFullscreenContainer!!.addView(view, COVER_SCREEN_PARAMS)
            decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS)
            mCustomView = view
            setFullscreen(true)
            mCustomViewCallback = callback
            //          mActivity.setRequestedOrientation(requestedOrientation);
        }

        super.onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        if (mCustomView == null) {
            return
        }

        setFullscreen(false)
        val decor = mActivity!!.window.decorView as FrameLayout
        decor.removeView(mFullscreenContainer)
        mFullscreenContainer = null
        mCustomView = null
        mCustomViewCallback!!.onCustomViewHidden()
        (mActivity as Activity).requestedOrientation = mOriginalOrientation
    }

    private fun setFullscreen(enabled: Boolean) {
        val win = mActivity!!.window
        val winParams = win.attributes
        val bits = WindowManager.LayoutParams.FLAG_FULLSCREEN
        if (enabled) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
            if (mCustomView != null) {
                mCustomView!!.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
            }
        }
        win.attributes = winParams
    }

    private class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {
        init {
            setBackgroundColor(ContextCompat.getColor(ctx, android.R.color.black))
        }

        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }
    }

    companion object {

        private val COVER_SCREEN_PARAMS =
            FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}