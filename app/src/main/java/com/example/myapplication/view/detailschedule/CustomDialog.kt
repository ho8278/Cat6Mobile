package com.example.myapplication.view.detailschedule

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.myapplication.R
import com.example.myapplication.data.DataManager
import com.example.myapplication.data.model.Schedule
import com.example.myapplication.databinding.LayoutDetailScheduleBinding

class CustomDialog(context: Context, val list: MutableList<Schedule>, val listener: ScheduleChangeListener, style: Int) :
    Dialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    private fun initView(){
        val binding = DataBindingUtil.inflate<LayoutDetailScheduleBinding>(
            layoutInflater,
            R.layout.layout_detail_schedule,
            null,
            false
        )
        binding.vpSchedule.apply {
            pageMargin = 120
            adapter = ScheduleViewPagerAdapter(list, listener)
            setPageTransformer(false,
                ViewPagerAnimator(0.8f)
            )
        }

        binding.viewmodel = DetailScheduleViewModel(DataManager.getInstance(context.applicationContext), list)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.7f
        }
        window.attributes = layoutParams

        setContentView(binding.root)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawableResource(R.color.colorClear)
    }

    class ViewPagerAnimator(val smallerScale: Float) : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val absPosition = Math.abs(position)
            if (absPosition >= 1) {
                page.scaleY = smallerScale
            } else {
                page.scaleY = (smallerScale - 1) * absPosition + 1
            }
        }
    }
}